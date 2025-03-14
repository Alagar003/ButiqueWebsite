package com.example.Boutique_Final.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static javax.crypto.Cipher.SECRET_KEY;

@Component
public class JwtUtil {

    @Value("${jwt.secret:default-secret}")
    private static String secretKey;

    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract expiration date from the token
    public static Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    // Extract custom claims (e.g., roles) from the token
    public static String extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class); // Assuming "role" is stored in the token
    }

    public static boolean isTokenValid(String token) {
        try {
            // Check if token is expired
            if (isTokenExpired(token)) {
                throw new RuntimeException("Token has expired");
            }

            // Extract claims to ensure token is valid
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Expired token: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Invalid token: " + e.getMessage());
        }
        return false;
    }

    // Check if the token is expired
    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

//    // Generate a token with custom claims (e.g., roles)
//    public static String generateToken(String username, String role) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("role", role) // Add custom claims
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10-hour validity
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }

    public static Claims extractClaims(String token) {
        System.out.println("Validating token with secret key: " + secretKey); // Debugging log
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    }


