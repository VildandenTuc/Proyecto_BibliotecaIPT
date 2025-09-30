package com.iptucuman.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

public record PrestamoDevolucionDTO(
        @NotNull Integer idPrestamo
) {
}
