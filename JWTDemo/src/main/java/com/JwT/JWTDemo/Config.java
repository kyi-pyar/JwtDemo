package com.JwT.JWTDemo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class Config implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Long jtw_valid=1L*60*60;
	
	@Value("${jwt.secret}")
	private String sceret;
	
	
	public <T>T getClaimfromToken(String token, Function<Claims,T> claimsResolver){
		Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
		
		
	}	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(sceret).parseClaimsJws(token).getBody();
	}
	public String getUserNamefromJWT(String token){
		return getClaimfromToken(token,Claims::getSubject);
	}
	public Date getExpirationDatefromToken(String token) {
		return getClaimfromToken(token, Claims::getExpiration);
	}
	private Boolean isTokenExpired(String token) {
		final Date expiration=getExpirationDatefromToken(token);
		return expiration.before(new Date());
	}
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims=new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jtw_valid * 1000))
				.signWith(SignatureAlgorithm.HS512, sceret).compact();
	}
	
	//validate token
		public Boolean validateToken(String token, UserDetails userDetails) {
			final String username = getUserNamefromJWT(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		}


}













