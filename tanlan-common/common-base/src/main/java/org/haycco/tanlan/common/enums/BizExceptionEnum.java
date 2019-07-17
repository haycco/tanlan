package org.haycco.tanlan.common.enums;

import org.haycco.tanlan.common.exception.BusinessErrMsg;

/**
 * 业务异常类型 范围定义: 5001-5999 公共错误码
 *
 * @author haycco
 */
public enum BizExceptionEnum implements BusinessErrMsg {

    UNKNOWN_ERROR(-1, "未知错误"),
    DB_FAIL(-2, "数据库异常"),

    BAD_REQUEST(400, "错误的请求"),
    INVALID_TOKEN(401, "身份验证失败"),
    INVALID_REFRESH_TOKEN(402, "非法的刷新令牌请求"),
    PERMISSIONS_ACCESS_DENIED(403, "客官你没有权限鸭"),
    NOT_FOUND(404, "访问地址不存在"),
    BLACK_LIST_REQUEST(423, "当前请求IP已被拉入黑名单"),
    TOO_MANY_REQUEST(429, "官人！用力太猛了，系统开启大招，排会儿队歇息一下吧。"),
    EXPIRED_TOKEN(438, "令牌过期了，请刷新令牌"),
    INTERNAL_SERVER_ERROR(500, "服务器迷路啦"),
    TIMEOUT_REQUEST(504, "响应超时"),

    INVALID_INPUT(5000, "参数解析失败"),
    INVALID_PARAMETER(5001, "参数校验失败"),
    PARAM_VALID_FAIL(5002, "参数验证失败：%s"),
    FILE_UPLOAD_FAIL(5003, "文件[%s]上传失败"),
    FILE_DOWNLOAD_FAIL(5004, "文件下载失败"),
    CREATE_ELASTIC_JOB_FAIL(5005, "添加任务[%s]失败"),
    SENSITIVE_WORDS(5006, "包含敏感词"),
    PARAM_FIELD_VALID_FAIL(5007, "参数[%s]验证失败：%s"),
    UNFINISHED_RETRY_LATER(5008, "未完成，稍后重试"),
    UNAVAILABLE_REGISTERED_APPLICATIONS(5404, "暂无可用的[%s]服务，请稍后再试！"),

    HYSTRIX_SERVER(5555, "服务熔断"),
    HYSTRIX_FALLBACK(5556, "系统繁忙, 请稍后再试啦！"),
    APP_VERSION_CONFIG_NOT_FOUND(5557, "[%s]版本信息未配置, 请稍后再试！"),

    MQ_SEND_FAIL(5558, "MQ消费发送失败"),

    MESSAGE_ACCOUNT_NOT_FOUND(5700, "消息账号不存在"),

    INVALID_LOGIN_USER(5559, "无法获取当前登录用户信息");

    private int errCode;
    private String errMsg;

    BizExceptionEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static BizExceptionEnum fromCode(int code) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errCode == code) {
                return c;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }

    public static BizExceptionEnum fromDesc(String desc) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errMsg.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
