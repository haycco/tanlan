package org.haycco.tanlan.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.HashMap;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.enums.BizExceptionEnum;
import org.haycco.tanlan.common.exception.BusinessException;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author haycco
 */
@Slf4j
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ResponsePacket<T> implements Serializable {

    private static final long serialVersionUID = -854581420972112544L;
    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MSG = "";

    private String msg = "";

    private int code;

    private T data;

    public ResponsePacket() {
    }

    /**
     * 成功, 无返回结果
     */
    public static ResponsePacket onSuccess() {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(SUCCESS_CODE);
        responsePacket.setMsg(SUCCESS_MSG);

        return responsePacket;
    }

    /**
     * 成功, 返回自定义的 vo
     *
     * @param data
     * @param <T>
     */
    public static <T> ResponsePacket<T> onSuccess(T data) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(SUCCESS_CODE);
        responsePacket.setMsg(SUCCESS_MSG);
        responsePacket.setData(data);

        return responsePacket;
    }

    /**
     * 服务熔断, 无返回结果
     */
    public static <T> ResponsePacket<T> onHystrix(T data) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(BizExceptionEnum.HYSTRIX_SERVER.getErrCode());
        responsePacket.setMsg(BizExceptionEnum.HYSTRIX_SERVER.getErrMsg());
        responsePacket.setData(data);
        return responsePacket;
    }

    /**
     * 服务熔断, 无返回结果
     */
    public static ResponsePacket onHystrix() {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(BizExceptionEnum.HYSTRIX_SERVER.getErrCode());
        responsePacket.setMsg(BizExceptionEnum.HYSTRIX_SERVER.getErrMsg());
        responsePacket.setData(new HashMap<>(0));
        return responsePacket;
    }

    /**
     * 服务熔断, 无返回结果
     */
    public static ResponsePacket onHystrix(Throwable t) {
        throw new BusinessException(BizExceptionEnum.HYSTRIX_SERVER);
    }

    /**
     * [真的]服务熔断, 无返回结果
     */
    public static ResponsePacket hystrix() {
        throw new BusinessException(BizExceptionEnum.HYSTRIX_SERVER);
    }

    /**
     * 错误, 无返回结果, 错误码为服务器内部错误(500)
     */
    public static ResponsePacket onError() {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrCode());
        responsePacket.setMsg(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg());

        return responsePacket;
    }

    /**
     * 错误, 错误码为服务器内部错误(500), 输入data参数，避免类型转换错误
     */
    public static <T> ResponsePacket onError(T data) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrCode());
        responsePacket.setMsg(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg());
        responsePacket.setData(data);

        return responsePacket;
    }

    /**
     * 错误, 无返回结果, 错误码为自定义的业务异常
     *
     * @param bizExceptionEnum
     */
    public static ResponsePacket onError(BizExceptionEnum bizExceptionEnum) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(bizExceptionEnum.getErrCode());
        responsePacket.setMsg(bizExceptionEnum.getErrMsg());

        return responsePacket;
    }

    public static ResponsePacket onError(BizExceptionEnum bizExceptionEnum, Object data) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(bizExceptionEnum.getErrCode());
        responsePacket.setMsg(bizExceptionEnum.getErrMsg());
        responsePacket.setData(data);

        return responsePacket;
    }

    /**
     * 错误, 返回结果为 null, 错误码为自定义的 code 和 msg
     * @param code
     * @param msg
     * @return
     */
    public static ResponsePacket onError(int code, String msg) {
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setCode(code);
        responsePacket.setMsg(msg);

        return responsePacket;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return this.code == SUCCESS_CODE;
    }

}
