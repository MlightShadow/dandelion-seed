package com.company.project.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.company.project.dto.AuthorizedInfo;

@Component
public class JWTUtil {
	public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

	private static final String CLAIM_KEY_USER_ID = "user_id";
	private static final String CLAIM_KEY_AUTHORITIES = "scope";

	private Map<String, String> tokenMap = new ConcurrentHashMap<>(32);

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long access_token_expiration;

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	public AuthorizedInfo getUserFromToken(String token) {
		AuthorizedInfo userDetail;
		try {
			final Claims claims = getClaimsFromToken(token);
			String userId = getUserIdFromToken(token);
			String username = claims.getSubject();
			userDetail = new AuthorizedInfo(userId, username, "");
		} catch (Exception e) {
			userDetail = null;
		}
		return userDetail;
	}

	public String getUserIdFromToken(String token) {
		String userId;
		try {
			final Claims claims = getClaimsFromToken(token);
			userId = String.valueOf(claims.get(CLAIM_KEY_USER_ID));
		} catch (Exception e) {
			userId = "";
		}
		return userId;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = claims.getIssuedAt();
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public String generateAccessToken(AuthorizedInfo userDetail) {
		Map<String, Object> claims = generateClaims(userDetail);
		claims.put(CLAIM_KEY_AUTHORITIES, authoritiesToArray(userDetail.getAuthorities()).get(0));
		return generateAccessToken(userDetail.getUsername(), claims);
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		AuthorizedInfo userDetail = (AuthorizedInfo) userDetails;
		final String userId = getUserIdFromToken(token);
		final String username = getUsernameFromToken(token);
		// final Date created = getCreatedDateFromToken(token);
		return (userId .equals(userDetail.getId()) && username.equals(userDetail.getUsername()) && !isTokenExpired(token)
		// && !isCreatedBeforeLastPasswordReset(created,
		// userDetail.getLastPasswordResetDate())
		);
	}

	public void putToken(String id, String token) {
		tokenMap.put(id, token);
	}

	public void deleteToken(String id) {
		tokenMap.remove(id);
	}

	public boolean containToken(String id, String token) {
		if (id != null && tokenMap.containsKey(id) && tokenMap.get(id).equals(token)) {
			return true;
		}
		return false;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate(long expiration) {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private Map<String, Object> generateClaims(AuthorizedInfo userDetail) {
		Map<String, Object> claims = new HashMap<>(16);
		claims.put(CLAIM_KEY_USER_ID, userDetail.getId());
		return claims;
	}

	private String generateAccessToken(String subject, Map<String, Object> claims) {
		return generateToken(subject, claims, access_token_expiration);
	}

	private List<?> authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
		List<String> list = new ArrayList<>();
		for (GrantedAuthority ga : authorities) {
			list.add(ga.getAuthority());
		}
		return list;
	}

	private String generateToken(String subject, Map<String, Object> claims, long expiration) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setId(UUID.randomUUID().toString())
				.setIssuedAt(new Date()).setExpiration(generateExpirationDate(expiration))
				.compressWith(CompressionCodecs.DEFLATE).signWith(SIGNATURE_ALGORITHM, secret).compact();
	}

}
