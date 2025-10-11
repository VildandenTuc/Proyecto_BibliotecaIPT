package com.iptucuman.biblioteca.repository;

import com.iptucuman.biblioteca.modelo.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    Boolean existsByTitulo(String titulo);
    //List<Libro> findByCategoriaIdCategoria(Integer categoria);
    Page<Libro> findByCategoriaIdCategoria(Integer categoria, Pageable pageable);

    /*
    @Query("""
    SELECT l FROM Libro l
    WHERE LOWER(l.autores) LIKE LOWER(CONCAT('%', :autor, '%'))
    """)
    List<Libro> buscarPorAutor(String autor);
    */
    @Query("""
    SELECT l FROM Libro l
    WHERE LOWER(l.autores) LIKE LOWER(CONCAT('%', :autor, '%'))
    """)
    Page<Libro> buscarPorAutor(@Param("autor") String autor, Pageable pageable);

    @Query("""
    SELECT l FROM Libro l
    WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
    """)
    Page<Libro> buscarPorTitulo(@Param("titulo") String titulo, Pageable pageable);

    //List<Libro> findByDisponibleTrue();
    Page<Libro> findByDisponibleTrue(Pageable pageable);

    Page<Libro> findByDisponibleFalse(Pageable pageable);

}
