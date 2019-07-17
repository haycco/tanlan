package org.haycco.tanlan.common.config;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author liuyang
 * @Description
 * @Date 2019/2/21 14:18
 **/
@Configuration
@ConditionalOnProperty(value = "password.enable", havingValue = "true")
public class PBKDF2EncoderConfig {

    @Value("${password.secret}")
    private String secret;

    @Value("${password.iteration}")
    private Integer iteration;

    @Value("${password.keylength}")
    private Integer keyLength;

    @Bean
    public PasswordEncoder PBKDF2Encoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence cs) {
                try {
                    byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                            .generateSecret(new PBEKeySpec(cs.toString().toLowerCase().toCharArray(), secret.getBytes(), iteration, keyLength))
                            .getEncoded();
                    return Base64.getEncoder().encodeToString(result);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean matches(CharSequence cs, String string) {
                return encode(cs).equals(string);
            }
        };
    }

//    public static void main(String[] args) {
//        try {
//            String secret = "03354ca1000a49bb97802d35c497a84a";
//            int iteration = 33, keyLength = 512;
//            String pwd = "1234abcd";
//            String md5pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
//            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
//                    .generateSecret(new PBEKeySpec(md5pwd.toCharArray(), secret.getBytes(), iteration, keyLength))
//                    .getEncoded();
//            System.out.println(Base64.getEncoder().encodeToString(result));
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
}
