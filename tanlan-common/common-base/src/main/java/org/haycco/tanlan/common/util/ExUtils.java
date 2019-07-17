package org.haycco.tanlan.common.util;

import org.haycco.tanlan.common.exception.BusinessErrMsg;
import org.haycco.tanlan.common.exception.BusinessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 错误工具类
 *
 * @author tangtao
 **/
public class ExUtils {

    public static BusinessException build(BusinessErrMsg errEnum, Object... args) {
        if (args.length > 0) {
            return new BusinessException(errEnum.getErrCode(), String.format(errEnum.getErrMsg(), args));
        }
        return new BusinessException(errEnum);
    }

    public static <T> Mono<T> monoErr(BusinessErrMsg errEnum, Object... arg) {
        if (arg.length > 0) {
            return Mono.error(new BusinessException(errEnum.getErrCode(), String.format(errEnum.getErrMsg(), arg)));
        }
        return Mono.error(new BusinessException(errEnum));
    }

    public static <T> Flux<T> fluxErr(BusinessErrMsg errEnum, Object... arg) {
        if (arg.length > 0) {
            return Flux.error(new BusinessException(errEnum.getErrCode(), String.format(errEnum.getErrMsg(), arg)));
        }
        return Flux.error(new BusinessException(errEnum));
    }


}
