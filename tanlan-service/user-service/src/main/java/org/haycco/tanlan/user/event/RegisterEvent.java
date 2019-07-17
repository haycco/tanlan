package org.haycco.tanlan.user.event;

import lombok.Data;
import org.haycco.tanlan.common.event.Event;
import org.haycco.tanlan.user.model.User;

import java.time.LocalDateTime;

/**
 * @author haycco
 **/
@Data
public class RegisterEvent implements Event {

    private User user;

    private LocalDateTime now = LocalDateTime.now();

    public RegisterEvent(User user) {
        this.user = user;
    }
}
