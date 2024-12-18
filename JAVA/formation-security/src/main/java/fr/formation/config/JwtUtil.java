package fr.formation.config;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String KEY = "19D6IjLAudjoZMxFHnp/Owq2SKapi7JRqGhUo82TrAMF9JBz7ATG4SnDLulvQqI2";
    // private static final String KEY = RandomStringUtils.randomAlphanumeric(64);

    private JwtUtil() { }

    public static String generate(String username) {
        // Création de la clé de signature
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
        Date now = new Date();

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + 36_000_000))
            .claim("info1", "information 1")
            .claim("info2", "information 2")
            .signWith(key)
            .compact();
    }

    public static boolean isValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
            
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            System.out.println(claims.get("info1", String.class));

            return true;
        }

        catch (Exception ex) {
            return false;
        }
    }
}