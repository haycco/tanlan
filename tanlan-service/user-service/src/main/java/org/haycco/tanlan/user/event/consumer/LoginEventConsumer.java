package org.haycco.tanlan.user.event.consumer;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.event.EventConsumer;
import org.haycco.tanlan.user.event.LoginEvent;
import org.haycco.tanlan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haycco
 **/
@Slf4j
@Component
public class LoginEventConsumer extends EventConsumer {


    @Autowired
    UserRepository userRepository;

    @Subscribe
    public void loginEvent(LoginEvent event) {
        log.info("LoginEventConsumer.loginEvent loginEvent=" + event);

        //修改最后登录时间
        this.userRepository.findById(event.getUser().getId())
                .doOnNext(user -> {
                    if (user.getLastLoggedInTime() == null) {
                        //发送im
                        Map<String, Object> map = new HashMap<>(1);
                        map.put("content", "欢迎"+event.getUser().getNickname()+"的到来");
                    }
                })
                .doOnNext(user -> user.setLastLoggedInTime(event.getNow()))
                .doOnNext(user -> log.info(user.getNickname() + "登录到系统！"))
                .flatMap(user -> this.userRepository.save(user))
                .doOnSuccess(user -> log.info(user.getNickname() + "登录系统成功"))
                .subscribe();
    }
}
