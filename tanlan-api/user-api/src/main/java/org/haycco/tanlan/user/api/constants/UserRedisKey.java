package org.haycco.tanlan.user.api.constants;

/**
 * user module redis key unified definition
 *
 * @author haycco
 */
public interface UserRedisKey {

    /** 应用命名 */
    String KEY_APP = "user";

    /** key 分隔符 */
    String KEY_SEPARATOR = ":";

    interface UserInfo {

        /** 用户信息 hash结构  user:id */
        String BASEINFO = KEY_APP + KEY_SEPARATOR + "%s";

        /** hash key */
        String USERINFO_INFO = "info";

        /** 手机号映射id */
        String PHONE_ID_MAPPING = KEY_APP + KEY_SEPARATOR + "map:phone:%s";
    }

}
