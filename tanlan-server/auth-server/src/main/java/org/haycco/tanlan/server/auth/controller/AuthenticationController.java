package org.haycco.tanlan.server.auth.controller;

import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.ResponsePacket;
import org.haycco.tanlan.common.enums.BizExceptionEnum;
import org.haycco.tanlan.common.exception.BusinessException;
import org.haycco.tanlan.common.exception.HystrixFallbackException;
import org.haycco.tanlan.common.util.DiscuzUtils;
import org.haycco.tanlan.common.util.JacksonUtils;
import org.haycco.tanlan.server.auth.model.AuthRequest;
import org.haycco.tanlan.server.auth.model.AuthResponse;
import org.haycco.tanlan.server.auth.model.TokenResponse;
import org.haycco.tanlan.server.auth.utils.JWTUtil;
import org.haycco.tanlan.user.api.UserServiceClient;
import org.haycco.tanlan.user.api.enums.UserErrEnum;
import org.haycco.tanlan.user.api.params.PhoneLoginParams;
import org.haycco.tanlan.user.api.params.RefreshTokenParams;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "授权认证相关API")
@RestController
public class AuthenticationController {

    @Autowired
    private JWTUtil jwtUtil;

    @Resource
    private DiscuzUtils discuzUtils;

    @Resource
    private UserServiceClient userServiceClient;

    @Resource
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Value("${auth.backend.accessTokenTime}")
    private Long backendExpirationTime;

    @ApiOperation(value = "手机号登录")
    @RequestMapping(value = "/login/phone", method = RequestMethod.POST)
    public Mono<AuthResponse> login(@RequestBody AuthRequest ar) {
        log.info("login/phone, params = " + ar);
        return userServiceClient
            .loginByPhone(PhoneLoginParams
                .builder()
                .password(ar.getPassword())
                .phone(ar.getUsername())
                .build())
            .doOnError(throwable -> {
                String message;
                if (throwable.getCause() instanceof FeignException) {
                    message = ((FeignException) throwable.getCause()).contentUTF8();
                    log.error("查询用户【{}】信息异常,msg={}", ar.getUsername(), message);
                    //业务异常处理
                    ResponsePacket ex = JacksonUtils.toBean(message, ResponsePacket.class);
                    throw new BusinessException(ex.getCode(), ex.getMsg());
                } else {
                    log.error("查询用户【{}】信息异常频率过高或其他错误，已触发服务熔断,msg={}", ar.getUsername(), throwable.getMessage(), throwable);
                    throw new HystrixFallbackException(BizExceptionEnum.HYSTRIX_FALLBACK);
                }
            })
            .switchIfEmpty(Mono.error(new BusinessException(UserErrEnum.USER_NOT_EXISTS)))
            .map((userDetails) -> {
                AuthResponse authResponse = createAuthResponse(userDetails);
                authResponse.setRcToken(userDetails.getRcToken());
                return authResponse;
            });
    }

    @ApiOperation(value = "获取当前登录用户令牌")
    @PostMapping("/token")
    public Mono<TokenResponse> getToken(@RequestBody UserVo userVo) {
        TokenResponse response = new TokenResponse();
        Map<String, String> proMap = new HashMap<>(1);
        proMap.put("accountType", userVo.getAccountType().toString());
        response.setToken(jwtUtil.generateToken(userVo.getId(), userVo.getUsername(), proMap));
        response.setRefreshToken(jwtUtil.generateRefreshToken(userVo));
        return Mono.just(response);
    }

    @ApiOperation(value = "刷新当前登录用户令牌")
    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public Mono<ResponseEntity> refresh(@RequestBody @Validated RefreshTokenParams ar) {
        String refreshToke = ar.getRefreshToken();
        Claims claims = null;
        try {
            claims = jwtUtil.getAllClaimsFromToken(refreshToke);
        } catch (ExpiredJwtException e){
            log.error("refresh_token has expired [{}], e={}", refreshToke, e.getMessage());
            String jsonStr = JacksonUtils.toJsonWithSnake(ResponsePacket.onError(BizExceptionEnum.EXPIRED_TOKEN));
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonStr));
        } catch (Exception e) {
            log.error("illegal refreshToke [{}], e={}", refreshToke, e.getMessage());
            String jsonStr = JacksonUtils.toJsonWithSnake(ResponsePacket.onError(BizExceptionEnum.INVALID_TOKEN));
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonStr));
        }

        return userServiceClient.getUser(claims.get("id").toString())
            .switchIfEmpty(Mono.error(new BusinessException(UserErrEnum.USER_NOT_EXISTS)))
            .map(user -> ResponseEntity.ok(createAuthResponse(user)));

    }

    private AuthResponse createAuthResponse(UserVo userVo) {
        log.info("login response userVo :" + userVo);
        AuthResponse response = new AuthResponse();
        Map<String, String> proMap = new HashMap<>(1);
        proMap.put("accountType", userVo.getAccountType().toString());
        response.setToken(jwtUtil.generateToken(userVo.getId(), userVo.getUsername(), proMap));
        response.setRefreshToken(jwtUtil.generateRefreshToken(userVo));
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userVo, vo);
        response.setUser(vo);

        return response;
    }


}
