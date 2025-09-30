package com.iptucuman.biblioteca.dto;


public record LibroRespuestaDTO(
        Integer idLibro,
        String titulo,
        String autores,
        String categoria,
        Integer ejemplares,
        Boolean disponible
) {
}
