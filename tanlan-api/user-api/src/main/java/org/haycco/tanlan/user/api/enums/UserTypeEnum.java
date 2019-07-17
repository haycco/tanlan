package org.haycco.tanlan.user.api.enums;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author haycco
 **/
public enum UserTypeEnum {

    NORMAL(0, "普通用户"),
    BACKEND(1, "后台用户");

    private int code;
    private String desc;

    UserTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<Integer, UserTypeEnum> codeToEnum = Stream.of(values()).collect(toMap(UserTypeEnum::getCode, identity()));

    public static UserTypeEnum ofCode(Integer code) {
        return codeToEnum.get(code);
    }
}
