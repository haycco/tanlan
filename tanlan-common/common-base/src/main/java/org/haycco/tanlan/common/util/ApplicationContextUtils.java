package org.haycco.tanlan.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/27 17:45
 **/
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> cls) {
        return context.getBean(cls);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
