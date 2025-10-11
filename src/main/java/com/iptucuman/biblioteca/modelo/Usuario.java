package com.iptucuman.biblioteca.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nombre;
    private String apellido;

    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos numéricos.")
    @Column(unique = true, nullable = false)
    private String dni;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Email(message = "Debe ingresar un email válido.")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números.")
    private String telefono;

    private Boolean activo = true; // Siempre se crea en true

    // 🔹 Campo requerido por Spring Security
    @Column(nullable = false)
    private String password;

    // 🔹 Rol del usuario, ejemplo: "ADMIN", "USER"
    @Column(nullable = false)
    private String rol;

}
