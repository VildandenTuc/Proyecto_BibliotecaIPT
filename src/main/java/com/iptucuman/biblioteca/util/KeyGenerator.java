package com.iptucuman.biblioteca.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Genera una clave secreta válida para HS256 (256 bits)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Convierte a Base64 para pegar en application.properties
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Nueva clave secreta (Base64): " + base64Key);
    }
}
