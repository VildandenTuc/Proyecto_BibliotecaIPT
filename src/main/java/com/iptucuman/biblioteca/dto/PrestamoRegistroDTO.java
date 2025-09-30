package com.iptucuman.biblioteca.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrestamoRegistroDTO(
        @NotNull Integer idUsuario,
        @NotNull Integer idLibro,
        @NotNull LocalDate fechaPrestamo,
        @NotNull @Future LocalDate fechaDevolucionEsperada
) {

}
