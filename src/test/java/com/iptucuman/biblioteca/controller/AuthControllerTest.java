package com.iptucuman.biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iptucuman.biblioteca.dto.UsuarioRegistroConPasswordDTO;
import com.iptucuman.biblioteca.dto.AuthRequest;
import com.iptucuman.biblioteca.modelo.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistroConPasswordSeguro() throws Exception {
        UsuarioRegistroConPasswordDTO dto = new UsuarioRegistroConPasswordDTO(
                "Juan",
                "Perez",
                "40123456",  // DNI único para test
                TipoUsuario.ALUMNO,
                "juan.perez.test@biblioteca.com",
                "3815551234",
                "Password123!",
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan.perez.test@biblioteca.com"));
    }

    @Test
    void testRegistroConPasswordDebil_DebeRechazar() throws Exception {
        UsuarioRegistroConPasswordDTO dto = new UsuarioRegistroConPasswordDTO(
                "Maria",
                "Lopez",
                "87654321",
                TipoUsuario.ALUMNO,
                "maria.lopez@test.com",
                "3815554321",
                "1234",  // Password débil
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistroSinMayusculas_DebeRechazar() throws Exception {
        UsuarioRegistroConPasswordDTO dto = new UsuarioRegistroConPasswordDTO(
                "Carlos",
                "Gomez",
                "11223344",
                TipoUsuario.DOCENTE,
                "carlos.gomez@test.com",
                "3815556789",
                "password123!",  // Sin mayúsculas
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistroSinNumeros_DebeRechazar() throws Exception {
        UsuarioRegistroConPasswordDTO dto = new UsuarioRegistroConPasswordDTO(
                "Ana",
                "Rodriguez",
                "55667788",
                TipoUsuario.ALUMNO,
                "ana.rodriguez@test.com",
                "3815559999",
                "Password!",  // Sin números
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistroSinCaracteresEspeciales_DebeRechazar() throws Exception {
        UsuarioRegistroConPasswordDTO dto = new UsuarioRegistroConPasswordDTO(
                "Luis",
                "Martinez",
                "99887766",
                TipoUsuario.DOCENTE,
                "luis.martinez@test.com",
                "3815551111",
                "Password123",  // Sin caracteres especiales
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistroEmailDuplicado_DebeRechazar() throws Exception {
        // Primer registro
        UsuarioRegistroConPasswordDTO dto1 = new UsuarioRegistroConPasswordDTO(
                "Pedro",
                "Sanchez",
                "12121212",
                TipoUsuario.ALUMNO,
                "pedro.sanchez@test.com",
                "3815552222",
                "Password123!",
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        // Segundo registro con mismo email
        UsuarioRegistroConPasswordDTO dto2 = new UsuarioRegistroConPasswordDTO(
                "Pablo",
                "Sanchez",
                "21212121",
                TipoUsuario.DOCENTE,
                "pedro.sanchez@test.com",  // Email duplicado
                "3815553333",
                "DifferentPass123!",
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginConUsuarioRecienRegistrado() throws Exception {
        // Registrar usuario
        UsuarioRegistroConPasswordDTO registroDto = new UsuarioRegistroConPasswordDTO(
                "Laura",
                "Fernandez",
                "33445566",
                TipoUsuario.ALUMNO,
                "laura.fernandez@test.com",
                "3815554444",
                "MyPassword123!",
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDto)))
                .andExpect(status().isCreated());

        // Login con el usuario recién creado
        AuthRequest loginRequest = new AuthRequest(
                "laura.fernandez@test.com",
                "MyPassword123!"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginConPasswordIncorrecto_DebeRechazar() throws Exception {
        // Registrar usuario
        UsuarioRegistroConPasswordDTO registroDto = new UsuarioRegistroConPasswordDTO(
                "Sofia",
                "Torres",
                "77889900",
                TipoUsuario.DOCENTE,
                "sofia.torres@test.com",
                "3815555555",
                "CorrectPassword123!",
                "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDto)))
                .andExpect(status().isCreated());

        // Login con password incorrecto
        AuthRequest loginRequest = new AuthRequest(
                "sofia.torres@test.com",
                "WrongPassword123!"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}