package com.iptucuman.biblioteca;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("changeme");
        System.out.println("Hash generado: " + hashed);
    }
}
