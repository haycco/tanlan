package org.haycco.tanlan.user.service;

import org.haycco.tanlan.user.api.params.*;
import org.haycco.tanlan.user.api.vo.LoginVo;
import org.haycco.tanlan.user.api.vo.UserVo;
import reactor.core.publisher.Mono;

/**
 * @author haycco
 **/
public interface IUserService {

    Mono<UserVo> register(PhoneRegisterParams user);

    Mono<UserVo> getUserByPhone(String phone);

    Mono<UserVo> update(String id, UserParams userParams);

    Mono<Boolean> checkNickname(String nickname);

    Mono<UserVo> getUser(String id);

    Mono<Boolean> remove(String id);

    Mono<LoginVo> loginByPhone(String phone, String password);

}
