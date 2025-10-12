package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    // 📌 Generar token para un usuario (incluye authorities/roles)
    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateTokenFromUserDetails(userDetails);
    }

    // 📌 Generar token para un usuario incluyendo el userId (NUEVO)
    public String generateTokenWithUserId(UserDetails userDetails, Integer userId) {
        return jwtUtil.generateTokenWithUserId(userDetails, userId);
    }

    // 📌 Obtener username desde el token
    // NOTA: Ahora retorna el userId como String (para compatibilidad con código antiguo)
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    // 📌 Extraer userId desde el token
    public Integer extractUserId(String token) {
        return jwtUtil.extractUserId(token);
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
