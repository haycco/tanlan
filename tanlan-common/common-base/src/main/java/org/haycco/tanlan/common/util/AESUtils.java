package org.haycco.tanlan.common.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;
import java.util.Base64.Decoder;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;


/**
 * AES128 算法 CBC 模式 PKCS7Padding 填充模式
 *
 * 其中CBC模式需要添加一个参数iv--对称解密算法初始向量 iv 要实现用PKCS7Padding填充，需要用到bouncycastle组件来实现
 *
 * @author haycco
 */
public class AESUtils {

    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    // 算法名称
    public static final String KEY_ALGORITHM = "DES";
    public static final String DEFAULT_CHARSET = "UTF8";
    // 加解密算法/模式/填充方式
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    // 默认对称解密算法初始向量 iv
    private static byte[] iv = {0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37,
            0x30, 0x38};
    //默认秘钥
    private static String sampleSessionKey = "tiihtNczf5v6AKRyjwEUhQ==";

    /**
     * 解密数据
     *
     * @param content 数据Base64编码
     * @param key 秘钥Base64编码
     * @param iv 向量Base64编码
     * @return 数据原文
     */
    public static String decrypt(String content, String key, String iv) {
        return decrypt(content, key, iv, DEFAULT_CHARSET);
    }

    /**
     * 解密数据
     *
     * @param content 数据Base64编码
     * @param key 秘钥Base64编码
     * @param iv 向量Base64编码
     * @param charset 字符编码
     * @return 数据原文
     */
    public static String decrypt(String content, String key, String iv, String charset) {
        Decoder d = Base64.getDecoder();
        byte[] bytes = decryptOfDiyIV(d.decode(content), d.decode(key), d.decode(iv));
        if (bytes != null && bytes.length != 0) {
            return new String(bytes, Charset.forName(charset));
        }
        return null;
    }

    /**
     * 加密方法 ---自定义对称解密算法初始向量 iv
     *
     * @param content 要加密的字符串
     * @param keyBytes 加密密钥
     * @param ivs 自定义对称解密算法初始向量 iv
     * @return 加密的结果
     */
    public static byte[] encryptOfDiyIV(byte[] content, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
//        logger.debug("加密向量IV：" + new String(Base64Utils.encode(ivs)));
        try {
            // 如果密钥不足16位，那么就补足
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JDK加密的密钥格式
            Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivs));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.doFinal(content));
            encryptedText = bos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("加密异常：" + e.getMessage());
        }
        return encryptedText;
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes 解密密钥
     * @param ivs 自定义对称解密算法初始向量 iv
     */
    public static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
//        logger.debug("解密向量IV：" + new String(Base64Utils.encode(ivs)));
        try {
            // 如果密钥不足16位，那么就补足
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JDK加密的密钥格式
            Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.doFinal(encryptedData));
            encryptedText = bos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("解密异常：" + e.getMessage());
        }
        return encryptedText;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // 明文
        String sourceData = "0BKut84LHtNy2Qre56sWWQiEiE.123123124235";
        for (int i = 0; i < 1; i++) {
//             加密
            byte[] bytes = AESUtils.encryptOfDiyIV(sourceData.getBytes(), sampleSessionKey.getBytes(), iv);
            String encryptedData = ByteUtils.toHexString(bytes);
            System.out.println(encryptedData);

//         解密
            byte[] dec = AESUtils.decryptOfDiyIV(
                    ByteUtils.fromHexString("a50ab32c040ee25dfd503a4a59de575de79e2e2adc948db57d3b76fed08ca9b8"),
                    sampleSessionKey.getBytes(), iv);
            System.out.println(new String(dec));
        }
        System.out.println("cost: " + (System.currentTimeMillis() - start));
    }

}
