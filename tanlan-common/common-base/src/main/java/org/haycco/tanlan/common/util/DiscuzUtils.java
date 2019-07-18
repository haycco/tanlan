package org.haycco.tanlan.common.util;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 *@Description:  JAVA版本的Discuz加解密，性能为aes/des加解密150倍左右
 *
 *@author  haycco
*/
@Component
@Slf4j
public class DiscuzUtils {

    //加解密盐值
    @Value("${auth.signing.salt}")
    private String signingSalt;

    public enum DiscuzAuthcodeMode {
        ENCODE, DECODE
    }


    /**
     *@Description: 从字符串的指定位置截取指定长度的子字符串
     *@param: [str 原字符串, startIndex 子字符串的起始位置, length 子字符串的长度]
    */
    public static String CutString(String str, int startIndex, int length) {
        if (startIndex >= 0) {
            if (length < 0) {
                length = length * -1;
                if (startIndex - length < 0) {
                    length = startIndex;
                    startIndex = 0;
                } else {
                    startIndex = startIndex - length;
                }
            }

            if (startIndex > str.length()) {
                return "";
            }

        } else {
            if (length < 0) {
                return "";
            } else {
                if (length + startIndex > 0) {
                    length = length + startIndex;
                    startIndex = 0;
                } else {
                    return "";
                }
            }
        }

        if (str.length() - startIndex < length) {

            length = str.length() - startIndex;
        }

        return str.substring(startIndex, startIndex + length);
    }

    /**
     *@Description: 从字符串的指定位置开始截取到字符串结尾的了符串
     *@param: [str 原字符串, startIndex 子字符串的起始位置]
    */
    public static String CutString(String str, int startIndex) {
        return CutString(str, startIndex, str.length());
    }

    public static String MD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     *@Description: 用于 RC4 处理密码
     *@param: [pass 密码字串, kLen 密钥长度，一般为 256]
    */
    static private byte[] GetKey(byte[] pass, int kLen) {
        byte[] mBox = new byte[kLen];

        // 产生密匙簿
        for (int i = 0; i < kLen; i++) {
            mBox[i] = (byte) i;
        }

        // 核心加解密部分
        int j = 0;
        for (int i = 0; i < kLen; i++) {

            j = (j + ((mBox[i] + 256) % 256) + pass[i % pass.length])
                    % kLen;

            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
        }

        return mBox;
    }

    /**
     *@Description: 生成随机字符
     *@param: [lens 随机字符长度]
    */
    public static String RandomString(int lens) {
        char[] CharArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        int clens = CharArray.length;
        String sCode = "";
        Random random = new Random();
        for (int i = 0; i < lens; i++) {
            sCode += CharArray[Math.abs(random.nextInt(clens))];
        }
        return sCode;
    }

    /**
     *@Description: 使用 Discuz authcode 方法对字符串加密
     *@param: [source 原始字符串, key 密钥, expiry 加密字串有效时间，单位是秒]
    */
    public static String encode(String source, String key, int expiry) {
        return authcode(source, key, DiscuzAuthcodeMode.ENCODE, expiry);
    }

    /**
     *@Description: 使用 Discuz authcode 方法对字符串加密
     *@param: [source 原始字符串, key 密钥]
    */
    public static String encode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.ENCODE, 0);

    }
    public String encode(String source) {
        return authcode(source,signingSalt , DiscuzAuthcodeMode.ENCODE, 0);

    }

    public static String decode(String source, String key,int expiry) {
        return authcode(source, key, DiscuzAuthcodeMode.DECODE, expiry);
    }
    /**
     *@Description: 使用 Discuz authcode 方法对字符串解密
     *@param: [source 原始字符串, key 密钥]
    */
    public static String decode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.DECODE, 0);
    }
    public String decode(String source) {
        return authcode(source, signingSalt, DiscuzAuthcodeMode.DECODE, 0);
    }

    /**
     *@Description: 使用 变形的 rc4 编码方法对字符串进行加密或者解密
     *@param: [source 原始字符串, key 密钥, operation 操作 加密还是解密, expiry 密文有效期,0代码永不过期]
    */
    private static String authcode(String source, String key,
            DiscuzAuthcodeMode operation, int expiry) {
        try {
            if (source == null || key == null) {
                return "";
            }
            // 动态密匙长度，相同的明文会生成不同密文就是依靠动态密匙
            int ckey_length = 4;
            String keya, keyb, keyc, cryptkey, result;
            // 密匙
            key = MD5(key);
            // 密匙a会参与加解密
            keya = MD5(CutString(key, 0, 16));
            // 密匙b会用来做数据完整性验证
            keyb = MD5(CutString(key, 16, 16));
            // 密匙c用于变化生成的密文
            keyc = ckey_length > 0 ? (operation == DiscuzAuthcodeMode.DECODE ? CutString(
                    source, 0, ckey_length) : RandomString(ckey_length))
                    : "";
            // 参与运算的密匙
            cryptkey = keya + MD5(keya + keyc);

            if (operation == DiscuzAuthcodeMode.DECODE) {
                byte[] temp;

                temp = Base64.getDecoder().decode(CutString(source, ckey_length));
                result = new String(RC4(temp, cryptkey));

//                //验证数据有效性PHP: substr($result, 0, 10) == 0，0代表永不过期
//                String timeStr = CutString(result, 0, 10);
//                boolean blank = StringUtils.isBlank(timeStr);
//                //验证数据有效性PHP: substr($result, 0, 10) - time() > 0
//                Long time = Long.valueOf(timeStr);
//                boolean expired = time < Long.valueOf(String.valueOf(System.currentTimeMillis()).substring(0 ,10));
//                if(blank || expired && time != 0){
//                    log.error("Token已经过期：{}",source);
//                    return "";
//                }
                if (CutString(result, 10, 16).equals(
                        CutString(MD5(CutString(result, 26) + keyb), 0, 16))) {
                    return CutString(result, 26);
                } else {
                    temp = Base64.getDecoder().decode(CutString(source + "=", ckey_length));
                    result = new String(RC4(temp, cryptkey));
                    if (CutString(result, 10, 16)
                            .equals(CutString(
                                    MD5(CutString(result, 26) + keyb), 0, 16))) {
                        return CutString(result, 26);
                    } else {
                        temp = Base64.getDecoder().decode(CutString(source + "==",
                                ckey_length));
                        result = new String(RC4(temp, cryptkey));
                        //验证数据完整性PHP: substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16)
                        boolean integrity = CutString(result, 10, 16).equals(
                                CutString(MD5(CutString(result, 26) + keyb), 0,
                                16));
                        if (integrity) {
                            return CutString(result, 26);
                        } else {
                            return "";
                        }
                    }
                }
            } else {
                //sprintf('%010d', $expiry ? $expiry + time() : 0).substr(md5($string.$keyb), 0, 16).$string;
                // 把动态密匙保存在密文里，这也是为什么同样的明文，生产不同密文后能解密的原因
                // 因为加密后的密文可能是一些特殊字符，复制过程可能会丢失，所以用base64编码
//                String expireTime = expiry == 0 ? "0000000000" :
//                        String.valueOf(System.currentTimeMillis() + expiry * 1000L).substring(0 ,10);
                source = "0000000000" + CutString(MD5(source + keyb), 0, 16)
                        + source;

                byte[] temp = RC4(source.getBytes("GBK"), cryptkey);

                return keyc + Base64.getEncoder().encodeToString(temp);

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }

    }

    /**
     *@Description: RC4 原始算法
     *@param: [input 原始字串数组, pass 密钥]
    */
    private static byte[] RC4(byte[] input, String pass) {
        if (input == null || pass == null) {
            return null;
        }

        byte[] output = new byte[input.length];
        byte[] mBox = GetKey(pass.getBytes(), 256);

        // 加密  
        int i = 0;
        int j = 0;

        for (int offset = 0; offset < input.length; offset++) {
            i = (i + 1) % mBox.length;
            j = (j + ((mBox[i] + 256) % 256)) % mBox.length;

            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
            byte a = input[offset];

            // byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];  
            // mBox[j] 一定比 mBox.Length 小，不需要在取模  
            byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];

            output[offset] = (byte) ((int) a ^ toInt(b));
        }

        return output;
    }

    private static int toInt(byte b) {
        return ((b + 256) % 256);
    }

    public static String getUnionId(String token,String key) {
        String decode = decode(token,key);
        if(StringUtils.isEmpty(decode)){
            return decode;
        }
        return StringUtils.split(decode,"\\.")[0];
    }

    public String getUnionId(String token) {
        String decode = decode(token);
        if(StringUtils.isEmpty(decode)){
            return decode;
        }
        return StringUtils.split(decode,"\\.")[0];
    }

    public static void main(String[] args) {
        DiscuzUtils discuzUtils = new DiscuzUtils();
        long lStart = System.currentTimeMillis();
        String test = "0BKut84LHtNy2Qre56sWWQiEiE.123123124235";
        String signingSalt = "a3c8ed73cb2b4ebca9b43c0bf4afa9648761408414c24f15b89ed317b2bc7221";
        for (int i = 0; i < 1; i++) {
            String afStr = DiscuzUtils.encode(test, signingSalt);
            System.out.println("加密后:" + afStr);
            String s = DiscuzUtils.decode(afStr, signingSalt);
            System.out.println("解码后：" + s);
            System.out.println("UID：" + discuzUtils.getUnionId(afStr,signingSalt));
        }

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加解密耗时：" + lUseTime + "毫秒");
        System.out.println(Long.valueOf(CutString(String.valueOf(System.currentTimeMillis()),0,10)));
        String deStr = DiscuzUtils
                .decode(
                        "e8m6aiUgYyrrczDco2vJPYjW8SAZQfC4Lgju5/InR/ZlXR26P/t+RCMXKElEJanVWIK8iNXoG4ojGauW1KN9wbcStcA=",
                        signingSalt);
        System.out.println("测试解码时效性:" + deStr);

    }

}  