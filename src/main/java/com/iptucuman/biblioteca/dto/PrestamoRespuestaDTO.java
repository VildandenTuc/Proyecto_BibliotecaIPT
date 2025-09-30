package com.iptucuman.biblioteca.dto;

import java.time.LocalDate;

public record PrestamoRespuestaDTO(
        Integer idPrestamo,
        String nombreUsuario,
        String apellidoUsuario,
        String tituloLibro,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionEsperada,
        LocalDate fechaDevolucionReal,
        Boolean devuelto,
        Boolean falta,
        String observacion
) {
}
