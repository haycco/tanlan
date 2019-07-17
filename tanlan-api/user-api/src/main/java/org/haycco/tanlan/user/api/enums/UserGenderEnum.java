package org.haycco.tanlan.user.api.enums;

/**
 * @author haycco
 **/
public enum UserGenderEnum {

    MALE(1, "男"),
    FEMALE(2, "女"),
    OTHER(3, "其他"),
    UNKNOWN(0, "未知");

    private Integer code;
    private String desc;

    UserGenderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
