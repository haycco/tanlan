package org.haycco.tanlan.common;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author haycco
 **/
@Slf4j
public class UserContext {

    public final static String DEVICE_ID_KEY = "deviceId";
    public final static String USER_ID_KEY = "userId";
    public final static String ACCOUNT_TYPE_KEY = "accountType";

    private static ThreadLocal<Account> uidThreadLocal = new ThreadLocal<>();

    public static void setUser(String deviceId, String userId, Integer accountType) {
        uidThreadLocal.set(new Account(deviceId, userId, accountType));
    }

    public static Account getUser() {
        return uidThreadLocal.get();
    }

    public static String getDeviceId() {
        return Optional.ofNullable(uidThreadLocal.get()).map(Account::getDeviceId).orElse(null);
    }

    public static String getUserId() {
        return Optional.ofNullable(uidThreadLocal.get()).map(Account::getUserId).orElse(null);
    }

    public static Integer getAccountType() {
        return Optional.ofNullable(uidThreadLocal.get()).map(Account::getAccountType).orElse(null);
    }

    public static void clean() {
        log.trace("userContext.clean | userId=" + uidThreadLocal.get());
        uidThreadLocal.remove();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Account {

        private String deviceId;
        private String userId;
        private Integer accountType;
    }


}


