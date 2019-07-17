package org.haycco.tanlan.user.api.enums;


import org.haycco.tanlan.common.exception.BusinessErrMsg;

/**
 * User module exception code defined
 *
 * @author haycco
 **/
public enum UserErrEnum implements BusinessErrMsg {

    USER_NOT_EXISTS(1000, "用户不存在"),
    INVALID_CODE(1001, "验证码错误"),
    NICKNAME_REPEAT(1002, "昵称重复"),
    INVALID_PASSWORD(1005, "密码错误"),
    REPEAT_PHONE(1006, "手机号已注册"),
    REPEAT_ACCOUNT(1008, "该账号已注册"),
    UNLOGGED_USER(1012, "当前用户未登录"),
    DESTROYED_USER_ACCOUNT(1013, "用户已注销"),
    FORBIDDEN_USER_STATUS(1014, "用户已禁用"),
    ILLEGAL_OPERATION(1015, "非法的操作")
    ;

    private int errCode;
    private String errMsg;

    UserErrEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }
}
