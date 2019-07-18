package org.haycco.tanlan.server.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.Date;

/**
 * @author haycco
 **/
@Configuration
public class TokenCheck {

    @Value("${auth.signing.key}")
    private String signingKey;

    public static void main(String[] args) {
//        "a3c8ed73cb2b4ebca9b43c0bf4afa9648761408414c24f15b89ed317b2bc7221"
        TokenInfo check = new TokenCheck().check("eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjEyMyIsInN1YiI6InNzZHMiLCJpYXQiOjE1NTAxNDA4NjQsImV4cCI6MTU1MDIyNzI2NH0.YvpGKXwC62MTb1HRDlJGHrNtA9PB4aKr_Fl41qpXb9hD9IcHEDXDPFKmJakIUo50p0TqIHJkNmas5xBwdMgasA");
    }

    public TokenInfo check(String token) {
        TokenInfo tokenInfo = new TokenInfo();
        try {
            Claims claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(signingKey.getBytes())).parseClaimsJws(token).getBody();;
            if (claims.getExpiration().before(new Date())) {
                throw new RuntimeException("Token has expired");
            }

            tokenInfo.setId(claims.get("id").toString());
            tokenInfo.setUsername(claims.getSubject());
            tokenInfo.setExp(claims.getExpiration().getTime());
        } catch (Exception e) {
            throw new RuntimeException("Token has expired");
        }

        tokenInfo.setActive(true);
        return tokenInfo;
    }


    @Data
    static class TokenInfo {

        private String username;

        private String id;

        private boolean active;

        private Long exp;
    }

}
