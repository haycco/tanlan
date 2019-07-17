package org.haycco.tanlan.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.UserContext;
import org.haycco.tanlan.common.util.ExUtils;
import org.haycco.tanlan.user.api.enums.UserErrEnum;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.haycco.tanlan.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Currently logged User REST-ful API implements
 *
 * @author haycco
 */
@Slf4j
@RestController
@Api(tags = {"当前登录用户相关API"})
public class CurrentlyLoggedUserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/currently/logged_user")
    public Mono<UserVo> getCurrentlyLoggedUser(){
        String userId = UserContext.getUserId();
        if(StringUtils.isEmpty(userId)) {
            return ExUtils.monoErr(UserErrEnum.UNLOGGED_USER);
        } else {
            return userService.getUser(userId).switchIfEmpty(ExUtils.monoErr(UserErrEnum.USER_NOT_EXISTS));
        }
    }
}
