package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.CambiarPasswordDTO;
import com.iptucuman.biblioteca.dto.UsuarioActualizarDTO;
import com.iptucuman.biblioteca.dto.UsuarioDetalleDTO;
import com.iptucuman.biblioteca.dto.UsuarioRegistroDTO;
import com.iptucuman.biblioteca.dto.UsuarioRespuestaDTO;
import com.iptucuman.biblioteca.modelo.TipoUsuario;
import com.iptucuman.biblioteca.repository.UsuarioRepository;
import com.iptucuman.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    //inyección de la dependencia UsuarioService
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //Endpoint para listar usuarios activos=true con List
    /*
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDetalleDTO>> listarActivos() {
        List<UsuarioDetalleDTO> usuarios = usuarioService.listarUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }*/
    //Endpoint para listar usuarios activos=true con page
    @GetMapping("/activos")
    public ResponseEntity<Page<UsuarioDetalleDTO>> listarActivosPaginados(Pageable pageable) {
        Page<UsuarioDetalleDTO> usuarios = usuarioService.listarUsuariosActivosPaginados(pageable);
        return ResponseEntity.ok(usuarios);
    }

    //Endpoint para listar usuarios inactivos=false con page
    @GetMapping("/inactivos")
    public ResponseEntity<Page<UsuarioDetalleDTO>> listarInactivosPaginados(Pageable pageable) {
        Page<UsuarioDetalleDTO> usuarios = usuarioService.listarUsuariosInactivosPaginados(pageable);
        return ResponseEntity.ok(usuarios);
    }

    //Endpoint para listar TODOS los usuarios con LIST
    /*@GetMapping
    public ResponseEntity<List<UsuarioDetalleDTO>> listar() {
        List<UsuarioDetalleDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }*/
    @GetMapping
    public Page<UsuarioDetalleDTO> listarUsuarios(Pageable pageable) {
        return usuarioService.listarUsuarios(pageable);
    }

    //Endpoint para listar usuarios por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> obtenerPorId(@PathVariable Integer id) {
        UsuarioDetalleDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para registrar un usuario
    @PostMapping
    public ResponseEntity<UsuarioDetalleDTO> registrar(@RequestBody @Valid UsuarioRegistroDTO dto) {
        UsuarioDetalleDTO nuevoUsuario = usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    //Endpoint para eliminación lógica de un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    //Endpoint para actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> actualizar(@PathVariable Integer id,
                                                        @RequestBody @Valid UsuarioActualizarDTO dto) {

        UsuarioDetalleDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    //Endpoint para REACTIVAR un usuario
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Integer id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/dni")
    public ResponseEntity<?> buscarUsuarioPorDni(@RequestParam String dni) {
        if (!dni.matches("\\d{7,8}")) {
            return ResponseEntity
                    .badRequest()
                    .body("DNI inválido: debe contener solo dígitos y tener entre 7 y 8 caracteres.");
        }

        try {
            return ResponseEntity.ok(usuarioService.buscarPorDni(dni));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar/nombre-apellido")
    public ResponseEntity<List<UsuarioRespuestaDTO>> buscarPorNombreApellido(@RequestParam String texto) {
        return ResponseEntity.ok(usuarioService.buscarPorNombreApellido(texto));
    }

    @GetMapping("/buscar/activos/nombre-apellido-tipo")
    public ResponseEntity<List<UsuarioRespuestaDTO>> buscarActivosPorNombreApellidoYTipo(
            @RequestParam String texto,
            @RequestParam TipoUsuario tipo) {
        return ResponseEntity.ok(usuarioService.buscarActivosPorNombreApellidoYTipo(texto, tipo));
    }

    /*
    @GetMapping("/buscar/activos/tipo")
    public ResponseEntity<List<UsuarioRespuestaDTO>> buscarActivosPorTipo(@RequestParam TipoUsuario tipo) {
        return ResponseEntity.ok(usuarioService.buscarActivosPorTipo(tipo));
    }*/
    @GetMapping("/buscar/activos/tipo")
    public Page<UsuarioRespuestaDTO> buscarActivosPorTipo(
            @RequestParam TipoUsuario tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "apellido") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return usuarioService.buscarActivosPorTipo(tipo, pageable);
    }

    // 🔒 Endpoint para cambiar contraseña
    @PutMapping("/{id}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@PathVariable Integer id,
                                              @RequestBody @Valid CambiarPasswordDTO dto) {
        try {
            usuarioService.cambiarPassword(id, dto);
            return ResponseEntity.ok().body("Contraseña actualizada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }

}
