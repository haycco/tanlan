package org.haycco.tanlan.user.cache;

import org.haycco.tanlan.user.model.User;
import reactor.core.publisher.Mono;

/**
 * @author haycco
 **/
public interface UserCacheManager {

    Mono<User> getUserById(String id);

    Mono<Boolean> cacheUser(User user);

    Mono<Boolean> delete(String id);

    Mono<Boolean> deleteBaseInfo(String id);

    Mono<Boolean> deleteByPhone(String phone);

    Mono<Boolean> deletePhoneMapping(String phone);

    Mono<User> getUserByPhone(String phone);


}
