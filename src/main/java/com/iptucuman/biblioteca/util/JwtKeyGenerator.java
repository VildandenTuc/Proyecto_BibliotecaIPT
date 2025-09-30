package com.iptucuman.biblioteca.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Genera una clave segura para HS256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Convierte a Base64 para pegar en application.properties
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("Tu nueva clave segura para jwt.secret es:");
        System.out.println(base64Key);
    }
}
