package org.haycco.tanlan.common.lock;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.haycco.tanlan.common.enums.BizExceptionEnum;
import org.haycco.tanlan.common.exception.BusinessException;
import org.haycco.tanlan.common.util.ExUtils;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis 分布式锁切面
 *
 * @author haycco
 **/
@Slf4j
@Aspect
@Component
public class RLockAspect implements Ordered {

    @Autowired
    private RedissonReactiveClient redissonReactiveClient;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Scheduler scheduler = Schedulers.fromExecutor(new ThreadPoolExecutor(
            64,
            64,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(5000),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(null, r, "unlock-t-" + threadNumber.getAndIncrement(), 0);
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    ));

    @Around("@annotation(monoLock)")
    Mono<?> monoLock(ProceedingJoinPoint pjp, MonoLock monoLock) {
        return monoLocks(pjp, new MonoLocks() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return MonoLocks.class;
            }

            @Override
            public MonoLock[] value() {
                return new MonoLock[]{monoLock};
            }
        });
    }

    @Around("@annotation(monoLocks)")
    Mono monoLocks(ProceedingJoinPoint pjp, MonoLocks monoLocks) {

        long startTryLock = System.currentTimeMillis();

        //获取方法名称
        String method = pjp.getSignature().getName();
        long tid = Thread.currentThread().getId();

        MonoLock[] locks = monoLocks.value();
        RLock[] rLocks = new RLock[locks.length];


        long default_expires = 2000;

        String[] keys = new String[locks.length];

        for (int i = 0; i < locks.length; i++) {
            MonoLock monoLock = locks[i];
            //解析 redis key
            String key = parseRedisKey(monoLock.value(), getParamMap(pjp));
            rLocks[i] = redissonClient.getLock(key);
            keys[i] = key;
            if (monoLock.expire() > default_expires) {
                default_expires = monoLock.expire();
            }
        }

        long wait = 300 * locks.length;
        long expire = default_expires;

        RLockReactive multiLock = redissonReactiveClient.getMultiLock(rLocks);

        return multiLock.tryLock(wait, expire, TimeUnit.MILLISECONDS, tid)
                .publishOn(scheduler)
                .flatMap(lockSuccess -> {
                    if (!lockSuccess) {
                        log.error("获取锁-超时 >>> wait:[{}] locks:{} tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                                wait, keys, tid, method, expire, System.currentTimeMillis() - startTryLock);
                        throw ExUtils.build(BizExceptionEnum.HYSTRIX_FALLBACK);
                    }

                    log.debug("获取锁-成功 >>> locks:{} tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                            keys, tid, method, expire, System.currentTimeMillis() - startTryLock);
                    try {
                        return (Mono<?>) pjp.proceed();
                    } catch (Throwable throwable) {
                        return Mono.error(throwable);
                    }
                })
                .doFinally(e -> {
                    multiLock.unlock(tid)
                            .publishOn(scheduler)
                            .doOnSuccess(u -> {
                                log.debug("释放锁-成功 >>> locks:{} tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                                        keys, tid, method, expire, System.currentTimeMillis() - startTryLock);
                            })
                            .onErrorResume(throwable -> {
                                log.error("释放锁-失败 >>> locks:{} tid:[{}] method:[{}] expire:[{}] cost:[{}] ex:{}",
                                        keys, tid, method, expire, System.currentTimeMillis() - startTryLock, throwable.getMessage());
                                return Mono.empty();
                            })
                            .subscribe();
                })
                ;
    }

    @Around("@annotation(fluxLock)")
    public Flux<?> fluxLock(ProceedingJoinPoint pjp, FluxLock fluxLock) {

        //获取方法名称
        String method = pjp.getSignature().getName();

        long tid = Thread.currentThread().getId();
        long expire = fluxLock.expire();
        long wait = expire;

        String key = parseRedisKey(fluxLock.value(), getParamMap(pjp));
        RLockReactive lock = redissonReactiveClient.getLock(key);

        long startTryLock = System.currentTimeMillis();

        // 同步加锁
        return lock.tryLock(wait, expire, TimeUnit.MILLISECONDS, tid)
                .doOnError(ex -> log.error("lock failed >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}] ex:{}",
                        key, tid, method, expire, System.currentTimeMillis() - startTryLock, ex.getMessage()))
                .flatMap(acquired -> {
                    if (!acquired) {
                        log.error("try lock timeout >>> wait:[{}] lock:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]", wait, key, tid, method, expire,
                                System.currentTimeMillis() - startTryLock);
                        return ExUtils.monoErr(BizExceptionEnum.HYSTRIX_FALLBACK);
                    }
                    return Mono.just(acquired);
                })
                .doOnSuccess(e -> log.debug("lock success >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                        key, tid, method, expire, System.currentTimeMillis() - startTryLock))
                .flatMapMany(e -> {
                    try {
                        return (Flux<?>) pjp.proceed();
                    } catch (Throwable throwable) {
                        return Flux.error(throwable);
                    }
                })
                // 异步释放锁
                .doFinally(result -> lock.unlock(tid)
                        .doOnError(ex -> log.error("unlock failed >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}] ex:{}",
                                key, tid, method, expire, System.currentTimeMillis() - startTryLock, ex.getMessage()))
                        .doOnSuccess(e -> log.debug("unlock success >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                                key, tid, method, expire, System.currentTimeMillis() - startTryLock))
                        .subscribe())
                ;
    }

    @Around("@annotation(redisLock)")
    public Object redisLock(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {

        return redisLocks(pjp, new RedisLocks() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return RedisLocks.class;
            }

            @Override
            public RedisLock[] value() {
                return new RedisLock[]{redisLock};
            }
        });
    }

    @Around("@annotation(redisLocks)")
    public Object redisLocks(ProceedingJoinPoint pjp, RedisLocks redisLocks) throws Throwable {
        //获取方法名称
        String method = pjp.getSignature().getName();

        long tid = Thread.currentThread().getId();
        long startTryLock = System.currentTimeMillis();

        List<LockInfo> rLocks = new ArrayList<>(redisLocks.value().length);

        for (RedisLock redisLock : redisLocks.value()) {
            long expire = redisLock.expire();
            long wait = expire;

            String key = parseRedisKey(redisLock.value(), getParamMap(pjp));
            RLock lock = redissonClient.getLock(key);
            try {
                // 同步加锁
                boolean acquired = lock.tryLock(wait, expire, TimeUnit.MILLISECONDS);
                // 超时未获取
                if (!acquired) {
                    log.error("try lock timeout >>> wait:[{}] lock:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]", wait, key, tid, method, expire,
                            System.currentTimeMillis() - startTryLock);
                    throw new BusinessException(BizExceptionEnum.HYSTRIX_FALLBACK);
                }

                rLocks.add(LockInfo.builder().rLock(lock).expire(expire).key(key).build());
                log.debug("lock success >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                        key, tid, method, expire, System.currentTimeMillis() - startTryLock);
            } catch (Exception ex) {
                log.error("lock failed >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}] ex:{}",
                        key, tid, method, expire, System.currentTimeMillis() - startTryLock, ex.getMessage());
                throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
            }
        }
        try {
            return pjp.proceed();
        } finally {
            for (LockInfo lockInfo : rLocks) {
                if (lockInfo.getRLock().isLocked()) {
                    // 异步解锁
                    RFuture<Void> unlockFuture = lockInfo.getRLock().unlockAsync(tid);
                    //包装一下，异步等待执行结果
                    Mono.fromCallable(() -> unlockFuture.get())
                            // 订阅时使用elastic调度器切换线程
                            .doOnError(ex -> log.error("unlock failed >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}] ex:{}",
                                    lockInfo.getKey(), tid, method, lockInfo.getExpire(), System.currentTimeMillis() - startTryLock, ex.getMessage()))
                            .doOnSuccess(e -> log.debug("unlock success >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                                    lockInfo.getKey(), tid, method, lockInfo.getExpire(), System.currentTimeMillis() - startTryLock))
                            .subscribeOn(scheduler)
                            .subscribe();
                } else {
                    log.warn("is not locked >>> key:[{}] tid:[{}] method:[{}] expire:[{}] cost:[{}]",
                            lockInfo.getKey(), tid, method, lockInfo.getExpire(), System.currentTimeMillis() - startTryLock);
                }
            }
        }

    }

    @Builder
    @Data
    static class LockInfo {

        private RLock rLock;
        private String key;
        private long expire;
    }

    /**
     * 反射出入参
     */
    private Map<String, Object> getParamMap(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();

        HashMap<String, Object> map = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], args[i]);
        }
        return map;
    }

    /**
     * 解析RedisKey
     */
    private String parseRedisKey(String key, Map<String, Object> map) {
        if (key.contains("$.")) {
            for (String s : key.split(":")) {
                if (s.startsWith("$.")) {
                    String param = s.replaceAll("\\$\\.", "");
                    key = key.replaceFirst("\\$\\." + param, getParamValue(param, map));
                }
            }
        }
        return key;
    }

    /**
     * 获取入参的值
     */
    private String getParamValue(String param, Map<String, Object> map) {
        if (param.contains(".")) {
            String paramName = param.substring(0, param.indexOf(".")).trim();
            return getParamValue(param.substring(param.indexOf(".") + 1), BeanMap.create(map.get(paramName)));
        }
        return map.get(param).toString();
    }


}
