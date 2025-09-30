package com.iptucuman.biblioteca.repository;

import com.iptucuman.biblioteca.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombre(String name);

    @Query("""
    SELECT c FROM Categoria c
    WHERE c.activo = true
      AND LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    List<Categoria> buscarActivasPorNombre(String nombre);

    List<Categoria> findByActivoTrue();


}
