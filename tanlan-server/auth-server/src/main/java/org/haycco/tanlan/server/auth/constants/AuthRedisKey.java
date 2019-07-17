package org.haycco.tanlan.server.auth.constants;

public interface AuthRedisKey {

    /** 应用命名 */
    String KEY_APP = "auth";

    /** key 分隔符 */
    String KEY_SEPARATOR = ":";

    interface Wx {
        String JSAPI_TICKET = KEY_APP + KEY_SEPARATOR + "jsapi_ticket";
    }

}
