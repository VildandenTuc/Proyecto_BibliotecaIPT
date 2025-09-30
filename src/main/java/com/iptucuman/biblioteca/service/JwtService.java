package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    // 📌 Generar token para un usuario
    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    // 📌 Obtener username desde el token
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    // 📌 Validar el token contra un usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = jwtUtil.extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 📌 Validar expiración
    private boolean isTokenExpired(String token) {
        return jwtUtil.extractExpiration(token).before(new java.util.Date());
    }
}
