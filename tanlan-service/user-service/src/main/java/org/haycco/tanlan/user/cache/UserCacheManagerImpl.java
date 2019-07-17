package org.haycco.tanlan.user.cache;

import org.apache.commons.lang3.StringUtils;
import org.haycco.tanlan.user.api.enums.UserStatusEnum;
import org.haycco.tanlan.user.model.User;
import org.haycco.tanlan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.haycco.tanlan.user.api.constants.UserRedisKey.UserInfo.*;

/**
 * @author haycco
 **/
@Component
public class UserCacheManagerImpl implements UserCacheManager {

    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> getUserById(String id) {
        String key = String.format(BASEINFO, id);
        return reactiveRedisTemplate.opsForHash().get(key, USERINFO_INFO)
            .switchIfEmpty(Mono.defer(() -> this.userRepository.findByIdAndStatusNot(id, UserStatusEnum.DESTROY.getCode()))
                .flatMap(user -> this.cacheUser(user).map(aBoolean -> user))
            );
    }

    @Override
    public Mono<Boolean> cacheUser(User user) {
        String key = String.format(BASEINFO, user.getId());
        return reactiveRedisTemplate.opsForHash().put(key, USERINFO_INFO, user)
                .flatMap(res -> addPhoneMapping(user.getPhone(), user.getId()));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        String key = String.format(BASEINFO, id);
        return reactiveRedisTemplate.opsForHash().delete(key);
    }

    @Override
    public Mono<Boolean> deleteBaseInfo(String id) {
        String key = String.format(BASEINFO, id);
        return reactiveRedisTemplate.opsForHash()
            .remove(key, USERINFO_INFO)
            .map(o -> Long.parseLong(o.toString()) >= 0);
    }

    @Override
    public Mono<Boolean> deleteByPhone(String phone) {
        String mapping = String.format(PHONE_ID_MAPPING, phone);
        return this.reactiveRedisTemplate.opsForValue().get(mapping)
            .flatMap(id -> this.reactiveRedisTemplate.opsForValue().delete(mapping)
                .then(this.deleteBaseInfo(id.toString()))
            );
    }

    @Override
    public Mono<Boolean> deletePhoneMapping(String phone) {
        String mapping = String.format(PHONE_ID_MAPPING, phone);
        return this.reactiveRedisTemplate.opsForValue().delete(mapping);
    }

    @Override
    public Mono<User> getUserByPhone(String phone) {
        //1： 通过phone查找id  2 通过id查用户信息
        return this.reactiveRedisTemplate.opsForValue()
            .get(String.format(PHONE_ID_MAPPING, phone))
            .flatMap(id -> this.getUserById(id.toString()))
            .switchIfEmpty(userRepository.findByPhoneAndStatusNot(phone, UserStatusEnum.DESTROY.getCode())
                .doOnNext(user -> this.cacheUser(user).subscribe()));
    }

    private Mono<Boolean> addPhoneMapping(String phone, String id) {
        if (StringUtils.isNotBlank(phone)) {
            return this.reactiveRedisTemplate.opsForValue().set(String.format(PHONE_ID_MAPPING, phone), id);
        } else {
            return Mono.just(false);
        }
    }
}
