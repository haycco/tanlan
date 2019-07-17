package org.haycco.tanlan.common.exception;

import lombok.ToString;

/**
 * 业务类异常
 * @author haycco
 */
@ToString
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5514172903777941028L;
    /** 错误码 */
    private Integer errCode;
    /** 错误提示消息 */
    private String errMsg;
    /** 异常堆栈信息 */
    private Throwable caused;
    private Object data;

    public BusinessException(BusinessErrMsg bizExceptionEnum){
        super(bizExceptionEnum.getErrMsg());
        this.errCode = bizExceptionEnum.getErrCode();
        this.errMsg = bizExceptionEnum.getErrMsg();
    }

    public BusinessException(BusinessErrMsg bizExceptionEnum, Throwable caused){
        super(caused);
        this.errCode = bizExceptionEnum.getErrCode();
        this.errMsg = bizExceptionEnum.getErrMsg();
    }

    public BusinessException(BusinessErrMsg bizExceptionEnum, String[] formatMsg){
        super(String.format(bizExceptionEnum.getErrMsg(), formatMsg));
        this.errCode = bizExceptionEnum.getErrCode();
        this.errMsg = super.getMessage();
    }

    public BusinessException(BusinessErrMsg bizExceptionEnum, String formatMsg){
        super(String.format(bizExceptionEnum.getErrMsg(), formatMsg));
        this.errCode = bizExceptionEnum.getErrCode();
        this.errMsg = super.getMessage();
    }

    public BusinessException(Integer errCode,String errMsg){
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(BusinessErrMsg bizExceptionEnum, String formatMsg, Throwable caused){
        super(caused);
        this.errCode = bizExceptionEnum.getErrCode();
        this.errMsg = String.format(bizExceptionEnum.getErrMsg(), formatMsg);
    }

    public BusinessException withData(Object data) {
        this.data = data;
        return this;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Throwable getCaused() {
        return caused;
    }

    public Object getData() {
        return data;
    }

    public static BusinessException newInstanceExceptionWithData(BusinessErrMsg bizExceptionEnum,Object data){
        BusinessException businessException = new BusinessException(bizExceptionEnum);
        businessException.data =data;
        return businessException;
    }

    public static BusinessException newInstanceExceptionWithData(Integer errCode, String errMsg,Object data){
        BusinessException businessException = new BusinessException(errCode,errMsg);
        businessException.data =data;
        return businessException;
    }

}
