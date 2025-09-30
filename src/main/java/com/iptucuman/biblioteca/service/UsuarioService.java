package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.dto.UsuarioActualizarDTO;
import com.iptucuman.biblioteca.dto.UsuarioDetalleDTO;
import com.iptucuman.biblioteca.dto.UsuarioRegistroDTO;
import com.iptucuman.biblioteca.dto.UsuarioRegistroConPasswordDTO;
import com.iptucuman.biblioteca.dto.UsuarioRespuestaDTO;
import com.iptucuman.biblioteca.exception.ResourceNotFoundException;
import com.iptucuman.biblioteca.modelo.TipoUsuario;
import com.iptucuman.biblioteca.modelo.Usuario;
import com.iptucuman.biblioteca.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring inyecta automáticamente el repositorio cuando crea este servicio
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registrar usuario (sin password - mantener para compatibilidad)
    public UsuarioDetalleDTO registrarUsuario(UsuarioRegistroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setDni(dto.dni());
        usuario.setTipoUsuario(dto.tipoUsuario());
        usuario.setEmail(dto.email());
        usuario.setTelefono(dto.telefono());
        usuario.setActivo(true);
        // Password por defecto si no se especifica
        usuario.setPassword(passwordEncoder.encode("changeme"));
        usuario.setRol("USER");

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioDetalleDTO(
                guardado.getIdUsuario(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getDni(),
                guardado.getTipoUsuario(),
                guardado.getEmail(),
                guardado.getTelefono(),
                guardado.getActivo()
        );
    }

    // Registrar usuario con password (nuevo método)
    public UsuarioDetalleDTO registrarUsuarioConPassword(UsuarioRegistroConPasswordDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setDni(dto.dni());
        usuario.setTipoUsuario(dto.tipoUsuario());
        usuario.setEmail(dto.email());
        usuario.setTelefono(dto.telefono());
        usuario.setActivo(true);

        // Encriptar el password antes de guardarlo
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setRol(dto.rol());

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioDetalleDTO(
                guardado.getIdUsuario(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getDni(),
                guardado.getTipoUsuario(),
                guardado.getEmail(),
                guardado.getTelefono(),
                guardado.getActivo()
        );
    }

    // Listar TODOS los usuarios usando List
    /*public List<UsuarioDetalleDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioDetalleDTO(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getDni(),
                        u.getTipoUsuario(),
                        u.getEmail(),
                        u.getTelefono(),
                        u.getActivo()
                ))
                .collect(Collectors.toList());
    }*/
    //Forma usando Page
    public Page<UsuarioDetalleDTO> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(u -> new UsuarioDetalleDTO(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getDni(),
                        u.getTipoUsuario(),
                        u.getEmail(),
                        u.getTelefono(),
                        u.getActivo()
                ));
    }


    //Listar usuarios por activos=true con List
    /*
    public List<UsuarioDetalleDTO> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(u -> new UsuarioDetalleDTO(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getDni(),
                        u.getTipoUsuario(),
                        u.getEmail(),
                        u.getTelefono(),
                        u.getActivo()
                ))
                .collect(Collectors.toList());
    }*/

    //Listar usuarios por activos=true con paginado
    public Page<UsuarioDetalleDTO> listarUsuariosActivosPaginados(Pageable pageable) {
        return usuarioRepository.findByActivoTrue(pageable)
                .map(u -> new UsuarioDetalleDTO(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getDni(),
                        u.getTipoUsuario(),
                        u.getEmail(),
                        u.getTelefono(),
                        u.getActivo()
                ));
    }

    public UsuarioDetalleDTO obtenerUsuarioPorId(Integer id) {
        /*
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));*/
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));


        return new UsuarioDetalleDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getTipoUsuario(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getActivo()
        );
    }

    public void desactivarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    //Actualizar datos de usuario
    public UsuarioDetalleDTO actualizarUsuario(Integer id, UsuarioActualizarDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Actualizamos solo si los campos no están vacíos o nulos
        if (dto.nombre() != null && !dto.nombre().isBlank()) {
            usuario.setNombre(dto.nombre());
        }
        if (dto.apellido() != null && !dto.apellido().isBlank()) {
            usuario.setApellido(dto.apellido());
        }
        if (dto.tipoUsuario() != null) {
            usuario.setTipoUsuario(dto.tipoUsuario());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            usuario.setEmail(dto.email());
        }
        if (dto.telefono() != null && !dto.telefono().isBlank()) {
            usuario.setTelefono(dto.telefono());
        }
        Usuario actualizado = usuarioRepository.save(usuario);

        return new UsuarioDetalleDTO(
                actualizado.getIdUsuario(),
                actualizado.getNombre(),
                actualizado.getApellido(),
                actualizado.getDni(),
                actualizado.getTipoUsuario(),
                actualizado.getEmail(),
                actualizado.getTelefono(),
                actualizado.getActivo()
        );
    }

    //Activar usuario que fue dado de baja anteriormente
    public void activarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getActivo()) {
            throw new RuntimeException("El usuario ya está activo.");
        }

        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public UsuarioRespuestaDTO buscarPorDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un usuario con ese DNI."));

        return new UsuarioRespuestaDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getTipoUsuario(),
                usuario.getActivo()
        );
    }

    public List<UsuarioRespuestaDTO> buscarPorNombreApellido(String texto) {
        return usuarioRepository.buscarPorNombreApellido(texto).stream()
                .map(usuario -> new UsuarioRespuestaDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getTipoUsuario(),
                        usuario.getActivo()
                )).toList();
    }

    public List<UsuarioRespuestaDTO> buscarActivosPorNombreApellidoYTipo(String texto, TipoUsuario tipo) {
        return usuarioRepository.buscarActivosPorNombreApellidoYTipo(texto, tipo).stream()
                .map(usuario -> new UsuarioRespuestaDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getTipoUsuario(),
                        usuario.getActivo()
                )).toList();
    }

    /*
    public List<UsuarioRespuestaDTO> buscarActivosPorTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuarioAndActivoTrue(tipoUsuario).stream()
                .map(usuario -> new UsuarioRespuestaDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getTipoUsuario(),
                        usuario.getActivo()
                )).toList();
    }*/
    public Page<UsuarioRespuestaDTO> buscarActivosPorTipo(TipoUsuario tipoUsuario, Pageable pageable) {
        return usuarioRepository.findByTipoUsuarioAndActivoTrue(tipoUsuario, pageable)
                .map(usuario -> new UsuarioRespuestaDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getTipoUsuario(),
                        usuario.getActivo()
                ));
    }


    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
