package org.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils
{

    // It must be at least 256 bits (32 characters) long for HMAC-SHA256
    private final String SECRET_KEY = "ThisIsAMySuperSecretKeyForEcommerceApplicationLearnSpringSecurity";

    // Token valid for 24 hours (in milliseconds)
    private final long EXPIRATION_TIME = 86400000;

    // Generates the cryptographic key from our secret string
    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Creates the JWT payload containing the user's email (subject) and expiration
    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        if(role!=null){
            role = role.replaceFirst("^ROLE_","");
        }
        if(role!=null){
            claims.put("role", role);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // Extracts the email from the token's payload
    public String extractEmail(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }


    // Extracts specific data (claims) from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
    {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // JWT signature is verified using secret key
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Checks if the token belongs to the user and has not expired
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) ; //&& !isTokenExpired(token)
    }
//
//    private boolean isTokenExpired(String token)
//    {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
}
