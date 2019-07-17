package org.haycco.tanlan.user.event.consumer;

import com.google.common.eventbus.Subscribe;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.event.EventConsumer;
import org.haycco.tanlan.user.event.RegisterEvent;
import org.springframework.stereotype.Component;

/**
 * @author haycco
 **/
@Slf4j
@Component
public class RegisterEventConsumer extends EventConsumer {

    @Subscribe
    public void registerEvent(RegisterEvent event) {
        log.info(".RegisterEventConsumer registerEvent=" + event);


    }
}
