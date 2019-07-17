package org.haycco.tanlan.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.UserContext;
import org.haycco.tanlan.user.api.params.*;
import org.haycco.tanlan.user.api.vo.LoginVo;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.haycco.tanlan.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Users REST-ful API implements
 *
 * @author haycco
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = {"用户相关API"})
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "根据id查询用户信息")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<UserVo> getUser(@PathVariable("id") @ApiParam(value = "用户id") String id) {
        log.info("UserController.getUser  userId={} id={}", UserContext.getUserId(), id);
        return userService.getUser(id);
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping(value = "/{id}")
    public Mono<UserVo> update(@PathVariable("id") String id, @RequestBody @Validated UserParams userParams) {
        log.info("UserController.update | userParams = " + userParams);
        return userService.update(id, userParams);
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public Mono<Boolean> remove(@PathVariable("id") String id) {
        log.info("UserController.remove | id = " + id);
        return this.userService.remove(id);
    }

    @ApiOperation(value = "手机号注册")
    @PostMapping("/register")
    public Mono<UserVo> registerByPhone(@RequestBody @Validated PhoneRegisterParams params) {
        log.info("UserController.registerByPhone | PhoneRegisterParams = " + params);
        return userService.register(params);
    }

    @ApiOperation(value = "根据手机号登录查询用户信息")
    @PostMapping("/login/phone")
    public Mono<LoginVo> loginByPhone(@RequestBody @Validated PhoneLoginParams params) {
        log.info("UserController.loginByPhone | phone = {} password={}", params.getPhone(),
                params.getPassword());
        return userService.loginByPhone(params.getPhone(), params.getPassword());
    }

    @ApiOperation(value = "检测nickname是否可用")
    @GetMapping(value = "/check/{nickname}")
    public Mono<Boolean> checkNickname(@PathVariable(value = "nickname") String nickname) {
        log.info("UserController.checkNickName | nickName = " + nickname);
        return userService.checkNickname(nickname);
    }

}
