package org.haycco.tanlan.user.api;

import org.haycco.tanlan.user.api.params.PhoneLoginParams;
import org.haycco.tanlan.user.api.params.PhoneRegisterParams;
import org.haycco.tanlan.user.api.params.ResetPasswordParams;
import org.haycco.tanlan.user.api.params.UserParams;
import org.haycco.tanlan.user.api.vo.LoginVo;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * User micro-service REST-ful API for openfeign client
 *
 * @author haycco
 */
@ReactiveFeignClient(name = "tanlan-user-service")
public interface UserServiceClient {

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/currently/logged_user")
    Mono<UserVo> getCurrentlyLoggedUser();

    /**
     * 查询用户信息
     */
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Mono<UserVo> getUser(@PathVariable(value = "id") String id);

    /**
     * 更新用户信息
     */
    @PutMapping(value = "/users/{id}")
    Mono<UserVo> update(@PathVariable(value = "id") String id, @RequestBody @Validated UserParams userParams);

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    Mono<Boolean> remove(@PathVariable(value = "id") String id);

    /**
     * 手机号登录
     */
    @PostMapping("/users/login/phone")
    Mono<LoginVo> loginByPhone(@RequestBody @Validated PhoneLoginParams params);

    /**
     * 手机号注册
     */
    @PostMapping("/users/register")
    Mono<UserVo> registerByPhone(@RequestBody @Validated PhoneRegisterParams params);

    /**
     * 校验昵称
     */
    @GetMapping(value = "/users/check/{nickname}")
    Mono<Boolean> checkNickname(@PathVariable(value = "nickname") String nickname);

}
