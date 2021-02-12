package com.sht.kitchen.utils;

import com.sht.kitchen.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Administrator
 */
@Slf4j
public class JwtUtils {
	private static final String SUBJECT = "Aaron-kitchen";
	private static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;
	private static final String APPSECRET = "e61451278486b545f860defcae13c19f";

	public static String geneJsonWebToken(User user) {
		String token;
		token = Jwts.builder().setSubject(SUBJECT)
				.claim("id", user.getId())
				/*.claim("openid", user.getOpenid())
				.claim("session_key", user.getSession_key())*/
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
				.signWith(SignatureAlgorithm.HS256, APPSECRET)
				.compact();

		return token;
	}

	public static Claims checkJWT(String token) {
		try {
			return Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			log.info("身份信息已过期");
			return null;
		} catch (Exception e) {
			log.info("无效的身份信息");
			return null;
		}
	}
}