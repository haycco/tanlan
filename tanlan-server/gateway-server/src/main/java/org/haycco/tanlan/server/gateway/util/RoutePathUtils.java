package org.haycco.tanlan.server.gateway.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haycco
 */
public class RoutePathUtils {

    public static final Pattern pattern = Pattern.compile("(?<=-)[^-]+(?=-)");
    public static final String PATH_API = "/api/";

    public static String getBaseUri(final String serviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_API);
        Matcher matcher = pattern.matcher(serviceId.toLowerCase());
        // 截取服务名中间的名字
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

}
