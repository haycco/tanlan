package org.haycco.tanlan.common.exception;

import lombok.ToString;

/**
 * 网关服务熔断异常
 *
 * @author haycco
 */
@ToString
public class HystrixFallbackException extends RuntimeException {

    private Integer code;

    private String message;

    private Throwable cause;

    public HystrixFallbackException(BusinessErrMsg bizExceptionEnum) {
        super(bizExceptionEnum.getErrMsg());
        this.code = bizExceptionEnum.getErrCode();
        this.message = bizExceptionEnum.getErrMsg();
    }

    public HystrixFallbackException(BusinessErrMsg bizExceptionEnum, Throwable cause) {
        super(cause);
        this.code = bizExceptionEnum.getErrCode();
        this.message = bizExceptionEnum.getErrMsg();
        this.cause = cause;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
