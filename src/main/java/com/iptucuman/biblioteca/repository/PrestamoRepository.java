package com.iptucuman.biblioteca.repository;

import com.iptucuman.biblioteca.dto.LibroPrestadoEstadisticaDTO;
import com.iptucuman.biblioteca.modelo.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    List<Prestamo> findByUsuarioIdUsuario(Integer idUsuario);
    //List<Prestamo> findByLibroIdLibro(Integer idLibro);
    Page<Prestamo> findByLibroIdLibro(Integer idLibro, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
       OR LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Prestamo> buscarPorNombreApellidoUsuario(String texto);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
       OR LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    Page<Prestamo> buscarPorNombreApellidoUsuario(String texto, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE LOWER(p.libro.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
    """)
    List<Prestamo> buscarPorTituloLibro(String titulo);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE LOWER(p.libro.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
    """)
    Page<Prestamo> buscarPorTituloLibro(@Param("titulo") String titulo, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE p.usuario.dni = :dni
    """)
    List<Prestamo> buscarPorDniUsuario(String dni);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE p.usuario.dni = :dni
    """)
    Page<Prestamo> buscarPorDniUsuario(String dni, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE (:desde IS NULL OR p.fechaPrestamo >= :desde)
      AND (:hasta IS NULL OR p.fechaPrestamo <= :hasta)
    """)
    List<Prestamo> buscarPorFechaPrestamoEntre(LocalDate desde, LocalDate hasta);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE (:desde IS NULL OR p.fechaPrestamo >= :desde)
      AND (:hasta IS NULL OR p.fechaPrestamo <= :hasta)
    """)
    Page<Prestamo> buscarPorFechaPrestamoEntre(LocalDate desde, LocalDate hasta, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE (:desde IS NULL OR p.fechaDevolucionEsperada >= :desde)
      AND (:hasta IS NULL OR p.fechaDevolucionEsperada <= :hasta)
    """)
    List<Prestamo> buscarPorFechaDevolucionEsperadaEntre(LocalDate desde, LocalDate hasta);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE (:desde IS NULL OR p.fechaDevolucionEsperada >= :desde)
      AND (:hasta IS NULL OR p.fechaDevolucionEsperada <= :hasta)
    """)
    Page<Prestamo> buscarPorFechaDevolucionEsperadaEntre(LocalDate desde, LocalDate hasta, Pageable pageable);

    /*
    @Query("""
    SELECT p FROM Prestamo p
    WHERE p.devuelto = false
      AND p.fechaDevolucionEsperada < :hoy
    """)
    List<Prestamo> buscarVencidosNoDevueltos(LocalDate hoy);*/
    @Query("""
    SELECT p FROM Prestamo p
    WHERE p.devuelto = false
      AND p.fechaDevolucionEsperada < :hoy
    """)
    Page<Prestamo> buscarVencidosNoDevueltos(@Param("hoy") LocalDate hoy, Pageable pageable);

    //List<Prestamo> findByFaltaTrue();
    Page<Prestamo> findByFaltaTrue(Pageable pageable);


    List<Prestamo> findByUsuarioIdUsuarioAndDevueltoFalse(Integer idUsuario);

    @Query("""
    SELECT COUNT(p) > 0 FROM Prestamo p
    WHERE p.usuario.idUsuario = :idUsuario
      AND p.libro.idLibro = :idLibro
      AND p.devuelto = false
    """)
    boolean existePrestamoActivoDelMismoLibro(Integer idUsuario, Integer idLibro);

    @Query("SELECT p FROM Prestamo p WHERE p.devuelto = false AND p.fechaDevolucionEsperada = :fecha")
    List<Prestamo> buscarPorVencimientoProximo(@Param("fecha") LocalDate fecha);

    @Query("""
    SELECT p.libro.idLibro, p.libro.titulo, COUNT(p)
    FROM Prestamo p
    GROUP BY p.libro.idLibro, p.libro.titulo
    ORDER BY COUNT(p) DESC
    """)
    List<Object[]> obtenerLibrosMasPrestadosRaw();

    List<Prestamo> findByDevueltoFalse();

}
