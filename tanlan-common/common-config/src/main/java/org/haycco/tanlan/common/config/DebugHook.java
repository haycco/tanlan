package org.haycco.tanlan.common.config;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

/**
 *@Description: 全局调试模式配置，追溯详细的异步响应式编程调用链异常信息
 * 否则到时候一堆异步的异常链下来，让你水土不服 疯狂呕吐
 * 代价：性能消耗,生产环境或压测环境不配置
 * 可以使用Publisher子集的checkpoint()方法冗余在代码中代替，这个成本低，但代码冗余高,checkpoint("",true)等价于此配置
 * 还可以使用log() 操作符。将其加到操作链上，偷窥操作链的动作过程: Flux.range(1, 10).log().take(3).subscribe();
 *
 *
 *@author  haycco
*/
//@Configuration
//@Profile({"grey","test","dev","local"})
@Slf4j
public class DebugHook {

    public DebugHook() {
        log.info("开启调试模式");
        Hooks.onOperatorDebug();
    }
}
