package com.iptucuman.biblioteca.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${biblioteca.jwt.secret}")
    private String secretKey;

    @Value("${biblioteca.jwt.expiration}")
    private long jwtExpirationInMs;

    // 🔑 Genera la clave segura para HS256
    private Key getSigningKey() {
        // Usar directamente la clave como bytes UTF-8
        System.out.println("DEBUG JWT: secretKey value = '" + secretKey + "'");
        System.out.println("DEBUG JWT: secretKey length = " + (secretKey != null ? secretKey.length() : "null"));
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        System.out.println("DEBUG JWT: keyBytes length = " + keyBytes.length + " bits = " + (keyBytes.length * 8));
        return Keys.hmacShaKeyFor(keyBytes); // siempre >= 256 bits
    }


    // 📌 Generar un nuevo token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 📌 Extraer el username (subject) desde el token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 📌 Extraer cualquier claim genérico
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 📌 Extraer todos los claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 📌 Validar si el token pertenece a ese usuario y no está expirado
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 📌 Chequear expiración
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 📌 Extraer fecha de expiración
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
