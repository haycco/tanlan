package org.haycco.tanlan.common.event;

import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 事件总线，非分布式，仅适用于单进程实例内
 */
public class EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);
    private static com.google.common.eventbus.EventBus eventBus;

    public static void create(Executor executor) {
        if (eventBus == null) {
            synchronized (EventBus.class) {
                if (eventBus == null) {
                    eventBus = new AsyncEventBus(executor, (exception, context) -> LOGGER.error("eventBus bus subscriber ex", exception));
                }
            }
        }
    }

    public static void post(Event event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }

}
