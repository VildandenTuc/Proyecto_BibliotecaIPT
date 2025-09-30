package com.iptucuman.biblioteca.dto;

import com.iptucuman.biblioteca.modelo.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UsuarioRegistroDTO(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,

        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos numéricos.")
        String dni,

        @NotNull(message = "El tipo de usuario es obligatorio.")
        TipoUsuario tipoUsuario,

        @Email(message = "Debe ingresar un email válido.")
        String email,

        @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números.")
        String telefono
) {
}
