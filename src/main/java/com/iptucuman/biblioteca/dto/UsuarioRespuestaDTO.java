package com.iptucuman.biblioteca.dto;

import com.iptucuman.biblioteca.modelo.TipoUsuario;

public record UsuarioRespuestaDTO(
        Integer id,
        String nombre,
        String apellido,
        String dni,
        String email,
        String telefono,
        TipoUsuario tipoUsuario,
        Boolean activo
) {
}
