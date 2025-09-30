package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.AuthRequest;
import com.iptucuman.biblioteca.dto.AuthResponse;
import com.iptucuman.biblioteca.dto.UsuarioRegistroConPasswordDTO;
import com.iptucuman.biblioteca.dto.UsuarioDetalleDTO;
import com.iptucuman.biblioteca.service.JwtService;
import com.iptucuman.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("DEBUG: Iniciando autenticación para: " + request.email());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            System.out.println("DEBUG: Autenticación exitosa");

            UserDetails user = userDetailsService.loadUserByUsername(request.email());
            System.out.println("DEBUG: Usuario cargado: " + user.getUsername());

            String token = jwtService.generateToken(user);
            System.out.println("DEBUG: Token generado: " + token.substring(0, 20) + "...");

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            System.out.println("DEBUG: Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            System.out.println("DEBUG: Error inesperado: " + e.getClass().getName());
            System.out.println("DEBUG: Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UsuarioRegistroConPasswordDTO dto) {
        try {
            // Verificar si el email ya existe
            if (usuarioService.existePorEmail(dto.email())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El email ya está registrado.");
            }

            // Registrar el usuario con password encriptado
            UsuarioDetalleDTO nuevoUsuario = usuarioService.registrarUsuarioConPassword(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            System.out.println("ERROR en registro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

}
