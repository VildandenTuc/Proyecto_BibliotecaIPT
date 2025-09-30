package com.iptucuman.biblioteca.dto;

public record LibroDetalleDTO(
        Integer idLibro,
        String titulo,
        String autores,
        String nombreCategoria,
        Integer ejemplares,
        Boolean disponible
) {
}
