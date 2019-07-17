package org.haycco.tanlan.common.event;

/**
 * 用法：
 * (1) 在Spring中应用
 * (2) 定义事件消费者继承{@link EventConsumer}，继承自{@link EventConsumer}的事件消费者不需要再显示注册自已，即不需 {@link EventBus#register}
 * (3) 定义事件消费者接收处理消息的方法，并在方法上添加{@code @Subscribe }注解，如果允许EventBus并行发布事件，则添加{@code @AllowConcurrentEvents}注解
 * (3) 将定义的事件消费者注入Spring容器中
 * (4) 调用{@link EventBus#create}，初始化线程池，在应用上下文中只能初始化一次
 * (5) 调用{@link EventBus#post(Event)} 发布事件消息
 *
 * @author haycco
 */
public abstract class EventConsumer {

    public EventConsumer() {
        EventBus.register(this);
    }

}
