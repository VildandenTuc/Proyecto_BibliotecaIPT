package com.iptucuman.biblioteca.repository;

import com.iptucuman.biblioteca.modelo.Prestamo;
import com.iptucuman.biblioteca.modelo.TipoUsuario;
import com.iptucuman.biblioteca.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //busca solo usuarios activos usando List
    //List<Usuario> findByActivoTrue();

    //busca solo usuarios activos con Page
    Page<Usuario> findByActivoTrue(Pageable pageable);

    //busca solo usuarios inactivos con Page
    Page<Usuario> findByActivoFalse(Pageable pageable);

    Optional<Usuario> findByDni(String dni);
    boolean existsByDni(String dni);

    @Query("""
    SELECT u FROM Usuario u
    WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
       OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Usuario> buscarPorNombreApellido(String texto);

    @Query("""
    SELECT u FROM Usuario u
    WHERE (LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')))
      AND u.tipoUsuario = :tipo
      AND u.activo = true
    """)
    List<Usuario> buscarActivosPorNombreApellidoYTipo(String texto, TipoUsuario tipo);

    //List<Usuario> findByTipoUsuarioAndActivoTrue(TipoUsuario tipoUsuario);
    Page<Usuario> findByTipoUsuarioAndActivoTrue(TipoUsuario tipoUsuario, Pageable pageable);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
