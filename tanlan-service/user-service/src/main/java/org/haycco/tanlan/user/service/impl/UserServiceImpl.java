package org.haycco.tanlan.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.haycco.tanlan.common.exception.BusinessException;
import org.haycco.tanlan.common.util.ExUtils;
import org.haycco.tanlan.user.api.enums.UserErrEnum;
import org.haycco.tanlan.user.api.enums.UserGenderEnum;
import org.haycco.tanlan.user.api.enums.UserStatusEnum;
import org.haycco.tanlan.user.api.enums.UserTypeEnum;
import org.haycco.tanlan.user.api.params.PhoneRegisterParams;
import org.haycco.tanlan.user.api.params.UserParams;
import org.haycco.tanlan.user.api.vo.LoginVo;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.haycco.tanlan.user.cache.UserCacheManager;
import org.haycco.tanlan.user.model.User;
import org.haycco.tanlan.user.repository.UserRepository;
import org.haycco.tanlan.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author haycco
 **/
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCacheManager userCacheManager;

    @Override
    public Mono<UserVo> getUser(String id) {
        return this.userCacheManager.getUserById(id)
            .map(this::transferVO);
    }

    @Override
    public Mono<Boolean> remove(String id) {
        return this.userCacheManager.delete(id)
            .flatMap(aBoolean -> this.userRepository.findByIdAndStatusNot(id, UserStatusEnum.DESTROY.getCode()))
            .switchIfEmpty(Mono.error(new BusinessException(UserErrEnum.USER_NOT_EXISTS)))
            .flatMap(user -> {
                user.setStatus(UserStatusEnum.DESTROY.getCode());
                return this.userRepository.save(user);
            })
            .hasElement();
    }

    @Override
    public Mono<UserVo> register(PhoneRegisterParams params) {
        return Mono.defer(
                () -> this.userRepository.findByPhoneAndStatusNot(params.getPhone(), UserStatusEnum.DESTROY.getCode()))
            .doOnNext(user -> {
                throw new BusinessException(UserErrEnum.REPEAT_PHONE);
            })
            .switchIfEmpty(this.createPhoneRegisterUser(params, UserTypeEnum.NORMAL))
            .flatMap(this.userRepository::insert)
            .map(this::transferVO);
    }

    private String getString(String source, String defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }

        return source;
    }

    private Mono<? extends User> createPhoneRegisterUser(PhoneRegisterParams params, UserTypeEnum type) {
        return Mono.defer(() -> {
            User user = User.builder()
                .phone(params.getPhone())
                .status(UserStatusEnum.ENABLE.getCode())
                .type(type.getCode())
                .password(passwordEncoder.encode(params.getPassword()))
                .build();
            user.setCreateDate(LocalDateTime.now());
            if (params.getGender() == null) {
                user.setGender(UserGenderEnum.UNKNOWN.getCode());
            } else {
                user.setGender(params.getGender());
            }

            return Mono.just(user);
        });
    }

    @Override
    public Mono<UserVo> getUserByPhone(String phone) {
        return findUserByPhone(phone).map(this::transferVO);
    }

    private Mono<User> findUserByPhone(String phone) {
        return this.userCacheManager.getUserByPhone(phone);
    }

    private void checkUserStatus(User user) {
        log.debug("check user status ={}", user);
        if (user.getStatus() == UserStatusEnum.DESTROY.getCode()) {
            throw ExUtils.build(UserErrEnum.DESTROYED_USER_ACCOUNT);
        } else if (user.getStatus() == UserStatusEnum.FORBIDDEN.getCode()) {
            throw ExUtils.build(UserErrEnum.FORBIDDEN_USER_STATUS);
        }
    }

    @Override
    public Mono<LoginVo> loginByPhone(String phone, String password) {
        return Mono.defer(() -> findUserByPhone(phone))
            .doOnNext(user -> checkUserStatus(user))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(UserErrEnum.USER_NOT_EXISTS))))
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(UserErrEnum.INVALID_PASSWORD))))
            .map(this::transferLoginVO);
    }

    private LoginVo transferLoginVO(User user) {
        LoginVo vo = new LoginVo();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public Mono<UserVo> update(String id, UserParams userParams) {
        return this.userCacheManager.getUserById(id)
            .switchIfEmpty(Mono.error(new BusinessException(UserErrEnum.USER_NOT_EXISTS)))
            .flatMap(user -> Mono.defer(() -> this.userRepository.save(user)))
            .map(this::transferVO);
    }

    @Override
    public Mono<Boolean> checkNickname(String nickName) {
        return Mono.defer(() -> this.userRepository.findByNickname(nickName))
            .doOnNext(user -> {
                log.warn("nickname {} is repeat!", nickName);
                throw new BusinessException(UserErrEnum.NICKNAME_REPEAT);
            })
            .then(Mono.just(true));
    }

    private UserVo transferVO(User user) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);

        return vo;
    }
}
