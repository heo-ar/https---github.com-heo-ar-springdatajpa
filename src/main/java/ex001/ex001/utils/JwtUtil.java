package ex001.ex001.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String getUsername(String token, String secretKey){
        return getClaims(token, secretKey).get("username", String.class);
    }

    public static String getRole(String token, String secretKey){
        return getClaims(token, secretKey).get("role", String.class);
    }

    public static Claims getClaims(String token, String secretKey){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isExpried(String token, String secretKey){
        return getClaims(token, secretKey).getExpiration().before(new Date());
    }

    public static String createJwt(String username, String secretKey, String role){
        Long expiredMs = 1000 * 60L * 30;
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ✅ 로그인 시 사용할 기본 토큰 생성 메서드
    public static String createToken(String userId, String secretKey, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
