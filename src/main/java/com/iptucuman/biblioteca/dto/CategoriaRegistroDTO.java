package com.iptucuman.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRegistroDTO(
        @NotBlank(message = "El nombre no puede estar vacío") String nombre
) {
}
