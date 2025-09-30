package com.iptucuman.biblioteca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRegistroDTO(

        @NotBlank String titulo,
        @Size(max = 250) String autores,
        @NotNull Integer idCategoria,
        @NotNull @Min(0) Integer ejemplares,
        @NotNull Boolean disponible
) {
}
