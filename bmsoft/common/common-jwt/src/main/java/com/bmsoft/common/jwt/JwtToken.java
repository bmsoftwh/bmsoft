package com.bmsoft.common.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@RefreshScope
public class JwtToken {
	
	private static final String CLAIM_KEY_GRANT = "grant";
	private static final String CLAIM_KEY_CLIENT = "client";
	private static final String CLAIM_KEY_TENNANT = "tenant";
	private static final String CLAIM_KEY_USER_ACCOUNT = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ipaddress = "ipaddress";
    
    public static final String GRANT_ACCESS = "accessToken";
    public static final String GRANT_REFRESH = "refreshToken";
	
    @Value("${token.secret:wrwtetetetet}")
	private String secretkey;
    
    @Value("${token.expire:30}")
	private long expiration;
    
    @Value("${token.refresh_expire:7}")
	private long refresh_expiration;
	
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	/**
     * 从token中获取创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
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

    /**
     * 从token中获取claims
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token)  {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretkey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
           throw e;
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     * @return
     */
    private Date generateAccessExpirationDate() {
    	Date date = new Date();
    	date.setTime(System.currentTimeMillis() + expiration * 60 * 1000);
        return date;
    }
    
    /**
     * 生成token的过期时间
     * @return
     */
    private Date generateRefreshExpirationDate() {
    	Date date = new Date();
    	date.setTime(System.currentTimeMillis() + refresh_expiration * 60 * 1000);
        return date;
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        boolean result= expiration.before(new Date());
        return result;
    }

    /**
     * 获取登录信息
     * @param token
     * @param grantType
     * @return
     */
    public UserAuth getUserFromToken(String token, String grantType) {
    	Claims claim = getClaimsFromToken(token);
    	if (claim.getExpiration().before(new Date()))
    		return null;
    	if (!claim.get(CLAIM_KEY_GRANT).equals(grantType))
    		return null;
    	UserAuth user = new UserAuth();
    	user.setClient((String)claim.get(CLAIM_KEY_CLIENT));
    	user.setUserId((String)claim.get(CLAIM_KEY_USER_ACCOUNT));
    	user.setTenant((String)claim.get(CLAIM_KEY_TENNANT));
        return user;
    }


    /**
     * 用户登录生成accessToken与refreshToken
     * @param userDetails
     * @return
     */
    public Map<String, String> generateUserToken(UserAuth userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CLIENT, userDetails.getClient());
        claims.put(CLAIM_KEY_TENNANT, userDetails.getTenant());
        claims.put(CLAIM_KEY_USER_ACCOUNT, userDetails.getUserId());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ipaddress, userDetails.getIpaddress());
        
        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put(GRANT_ACCESS, generateAccessToken(claims));
        tokens.put(GRANT_REFRESH, generateRefreshToken(claims));
        return tokens;
    }

    String generateAccessToken(Map<String, Object> claims) {
    	claims.put(CLAIM_KEY_GRANT, GRANT_ACCESS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateAccessExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secretkey)
                .compact();
    }
    
    String generateRefreshToken(Map<String, Object> claims) {
    	claims.put(CLAIM_KEY_GRANT, GRANT_REFRESH);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateRefreshExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secretkey)
                .compact();
    }

    /**
     * token 是否可刷新
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateAccessToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证token
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserAuth user) {
    	final Claims claims = getClaimsFromToken(token);
    	if (null == claims)return false;
    	String ipaddress = (String)claims.get(CLAIM_KEY_ipaddress);
    	Date expir = claims.getExpiration();
        return (user == null || user.getIpaddress().equals(ipaddress)) && expir.after(new Date());
    }
}
