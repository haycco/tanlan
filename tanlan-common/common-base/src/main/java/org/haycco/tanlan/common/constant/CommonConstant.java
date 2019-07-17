package org.haycco.tanlan.common.constant;

/**
 * 公共常量定义
 *
 * @author haycco
 */
public class CommonConstant {

    /** 公共扫描的包路径范围 */
    public final static String SCAN_BASE_PACKAGES = "org.haycco.tanlan.**";
    /** 手机号正则表达式校验 */
    public final static String MOBILE_REGEX = "^1[3|4|5|6|7|8|9][0-9]\\d{8}|200\\d{8}|300\\d{8}$";
    /** 日期正则表达式校验 */
    public final static String DATE_REGEX = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])";
    /** 给MD5加密撒把盐混交MD5 让破解的无从下手*/
    public final static String MD5_SLAT = "&%tanlan-***&&%%$$#@";
    /** 运营手机号200开头 */
    public static final String OPERATE_PRI = "200";
    /** 马甲手机号300开头 */
    public static final String VEST_PRI = "300";
    /** 版本号 1.0.0 */
    public static final String VERSION_1_0_0 = "1.0.0";

}
