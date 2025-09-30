package com.iptucuman.biblioteca.dto;

import com.iptucuman.biblioteca.modelo.TipoUsuario;

public record UsuarioDetalleDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String dni,
        TipoUsuario tipoUsuario,
        String email,
        String telefono,
        Boolean activo
) {
}
