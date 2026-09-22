package com.tuckersoft.branchengine.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Component
public class JwtUtil {
    private final SecretKey key; private final long accessMs; private final long refreshMs;
    public JwtUtil(@Value("${jwt.secret}") String s,
                   @Value("${jwt.access-token-expiration-ms}") long a,
                   @Value("${jwt.refresh-token-expiration-ms}") long r) {
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        if (b.length < 32) throw new IllegalStateException("jwt.secret must be >= 32 bytes");
        this.key = Keys.hmacShaKeyFor(b); this.accessMs = a; this.refreshMs = r;
    }
    public String generateAccessToken(String u, List<String> roles) {
        return Jwts.builder().subject(u).claims(Map.of("roles", roles, "type", "access"))
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+accessMs)).signWith(key).compact();
    }
    public String generateRefreshToken(String u) {
        return Jwts.builder().subject(u).claim("type","refresh")
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+refreshMs)).signWith(key).compact();
    }
    public Claims parse(String t){return Jwts.parser().verifyWith(key).build().parseSignedClaims(t).getPayload();}
    public boolean isValid(String t){try{parse(t);return true;}catch(Exception e){return false;}}
    public List<String> getRoles(String t){Object r=parse(t).get("roles");return r instanceof List<?> l?l.stream().map(Object::toString).toList():List.of();}
    public String getUsername(String t){return parse(t).getSubject();}
    public String getType(String t){return (String)parse(t).get("type");}
    public long getAccessMs(){return accessMs;}
    public long getRefreshMs(){return refreshMs;}
}
