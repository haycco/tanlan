package org.haycco.tanlan.user.api.enums;

import java.util.Arrays;

/**
 * @author haycco
 **/
public enum UserStatusEnum {

    ENABLE(0, "启用"),
    FORBIDDEN(1, "禁用"),
    DESTROY(2, "已销毁"),
    UNKNOWN(-1, "未知");

    private int code;
    private String desc;

    UserStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserStatusEnum of(int code) {
        return Arrays.stream(UserStatusEnum.values())
            .filter(a -> a.getCode() == code)
            .findAny()
            .orElse(UNKNOWN);
    }

}
