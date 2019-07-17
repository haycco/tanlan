package org.haycco.tanlan.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * redis分片工具
 *
 *@author  haycco
*/
@Slf4j
public class RedisUtils {

    /** HASH分片大小, 必须为2的幂次方 */
    private static int HASH_SEGMENT_SIZE = 2 << 12;

    /**
     * 生成HASH分片key
     *
     * @param segmentSeed 分片计算种子
     * @param keyPrefixs key的前缀字符串序列，按先手顺序组装
     * @return 分片缓存key
     */
    public static String buildHashSegmentKey(String segmentSeed, String... keyPrefixs) {
        StringBuilder key = new StringBuilder();
        for (String keyPrefix : keyPrefixs) {
            key.append(keyPrefix);
            key.append(":");
        }

        key.append(Integer.toString(hash(segmentSeed) & (HASH_SEGMENT_SIZE - 1)));
        return key.toString();
    }

    private static final int hash(Object key) {
        int h;

        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
        System.out.println(HASH_SEGMENT_SIZE);
        System.out.println(buildHashSegmentKey("127351", "a"));
        System.out.println(buildHashSegmentKey("127352", "a"));
        System.out.println(buildHashSegmentKey("127353", "a"));
        System.out.println(buildHashSegmentKey("127354", "a"));
        System.out.println(buildHashSegmentKey("5d0df6976b69060001b9de61", "pick:pick"));
    }
}
