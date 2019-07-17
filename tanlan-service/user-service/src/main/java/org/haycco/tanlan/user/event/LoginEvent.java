package org.haycco.tanlan.user.event;

import lombok.Data;
import lombok.ToString;
import org.haycco.tanlan.common.event.Event;
import org.haycco.tanlan.user.api.vo.UserVo;

import java.time.LocalDateTime;

/**
 * @author haycco
 **/
@Data
@ToString
public class LoginEvent implements Event {

    private UserVo user;

    private LocalDateTime now = LocalDateTime.now();

    public LoginEvent(UserVo user) {
        this.user = user;
    }

}
