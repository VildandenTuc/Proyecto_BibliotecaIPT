package com.iptucuman.biblioteca.dto;

import com.iptucuman.biblioteca.modelo.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UsuarioActualizarDTO(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotNull TipoUsuario tipoUsuario,
        @Email String email,
        @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números.") String telefono
) {
}
