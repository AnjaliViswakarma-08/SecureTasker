package com.securetasker.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final Key signingKey;
  private final long expirationMs;

  public JwtUtil(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration}") long expirationMs) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(String subject) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractSubject(String token) {
    return extractAllClaims(token).getSubject();
  }

  public boolean isTokenValid(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
