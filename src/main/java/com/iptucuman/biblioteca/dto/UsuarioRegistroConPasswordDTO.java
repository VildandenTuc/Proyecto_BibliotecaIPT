package com.iptucuman.biblioteca.dto;

import com.iptucuman.biblioteca.modelo.TipoUsuario;
import com.iptucuman.biblioteca.validation.PasswordSeguro;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UsuarioRegistroConPasswordDTO(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,

        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos numéricos.")
        String dni,

        @NotNull(message = "El tipo de usuario es obligatorio.")
        TipoUsuario tipoUsuario,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Debe ingresar un email válido.")
        String email,

        @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números.")
        String telefono,

        @NotBlank(message = "El password es obligatorio.")
        @PasswordSeguro
        String password,

        @NotBlank(message = "El rol es obligatorio.")
        String rol
) {
}