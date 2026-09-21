package com.ticket267.hackathon1.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-expiration-ms}") long accessExpMs,
                   @Value("${jwt.refresh-token-expiration-ms}") long refreshExpMs) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) throw new IllegalStateException("jwt.secret must be >= 32 bytes");
        this.key = Keys.hmacShaKeyFor(bytes);
        this.accessExpMs = accessExpMs;
        this.refreshExpMs = refreshExpMs;
    }

    public String generateAccessToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("roles", roles, "type", "access"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpMs))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpMs))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }

    public boolean isValid(String token) {
        try { parse(token); return true; }
        catch (JwtException | IllegalArgumentException e) { return false; }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Object r = parse(token).get("roles");
        return r instanceof List<?> l ? l.stream().map(Object::toString).toList() : List.of();
    }

    public String getUsername(String token) { return parse(token).getSubject(); }
    public String getType(String token)     { return (String) parse(token).get("type"); }

    public long getAccessExpMs()  { return accessExpMs; }
    public long getRefreshExpMs() { return refreshExpMs; }
}
