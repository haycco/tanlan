package org.haycco.tanlan.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis 分布式锁注解 返回mono
 *
 * @author haycco
 **/
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MonoLocks.class)
public @interface MonoLock {

    /**
     * 锁名称
     */
    String value();

    /**
     * 锁失效时间, 毫秒(ms)
     */
    long expire() default 3000;
}
