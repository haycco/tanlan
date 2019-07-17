package org.haycco.tanlan.server.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.haycco.tanlan.user.api.vo.UserVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java Web Token 工具类
 *
 * @author liuyang
 */
@Component
public class JWTUtil {

	@Value("${auth.signing.key}")
	private String signingKey;
	
	@Value("${auth.client.accessTokenTime}")
	private Long expirationTime;

	@Value("${auth.client.refreshTokenTime}")
	private Long refreshExpirationTime;

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(signingKey.getBytes())).parseClaimsJws(token).getBody();
	}
	
	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public String generateToken(String id, String subject, Map<String, String> others) {
		Map<String, Object> claims = new ConcurrentHashMap<>();
		claims.put("id", id);
		if (others != null){
			for (Map.Entry<String, String> other : others.entrySet()) {
				claims.put(other.getKey(), other.getValue());
			}
		}
		return doGenerateToken(claims, subject,expirationTime);
	}

	public String generateRefreshToken(UserVo user){
		Map<String, Object> claims = new ConcurrentHashMap<>();
		claims.put("id", user.getId());
		claims.put("refresh", "refresh");
		return doRefreshGenerateToken(claims,user.getUsername());
	}

	private String doRefreshGenerateToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = refreshExpirationTime; //in second
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(signingKey.getBytes()))
				.compact();
	}

	private String doGenerateToken(Map<String, Object> claims, String username,Long expirationTimeLong) {
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(signingKey.getBytes()))
				.compact();
	}
	
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
