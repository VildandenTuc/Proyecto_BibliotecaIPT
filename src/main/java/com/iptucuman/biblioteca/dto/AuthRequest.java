package com.iptucuman.biblioteca.dto;

public record AuthRequest(
        String email,
        String password
) {}
