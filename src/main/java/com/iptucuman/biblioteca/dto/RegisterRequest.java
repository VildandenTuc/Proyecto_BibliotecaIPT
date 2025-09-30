package com.iptucuman.biblioteca.dto;

public record RegisterRequest(
        String username,
        String password,
        String email
) {}
