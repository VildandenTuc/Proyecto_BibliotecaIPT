package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.dto.CategoriaDetalleDTO;
import com.iptucuman.biblioteca.dto.CategoriaRegistroDTO;
import com.iptucuman.biblioteca.dto.CategoriaRespuestaDTO;
import com.iptucuman.biblioteca.exception.DuplicateResourceException;
import com.iptucuman.biblioteca.modelo.Categoria;
import com.iptucuman.biblioteca.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaDetalleDTO registrarCategoria(CategoriaRegistroDTO categoriaRegistroDTO){
        categoriaRepository.findByNombre(categoriaRegistroDTO.nombre())
                .ifPresent(c -> {
                    throw new DuplicateResourceException("Ya existe una categoría con el nombre: " + categoriaRegistroDTO.nombre());
                });
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRegistroDTO.nombre());

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return new CategoriaDetalleDTO(categoriaGuardada.getIdCategoria(), categoriaGuardada.getNombre());
    }

    /*LISTAR categorías usando una List*/
    public List<CategoriaDetalleDTO> listarCategoriaConList(){
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> new CategoriaDetalleDTO(categoria.getIdCategoria(), categoria.getNombre()))
                .collect(Collectors.toList());
    }

    /*LISTAR categorías usando Page*/
    public Page<CategoriaDetalleDTO> listarCategoriaConPage(Pageable pageable){
        return categoriaRepository.findAll(pageable)
                .map(categoria -> new CategoriaDetalleDTO(categoria.getIdCategoria(), categoria.getNombre()));
    }

    public CategoriaDetalleDTO obtenerCategoriaPorId(Integer id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        return new CategoriaDetalleDTO(categoria.getIdCategoria(), categoria.getNombre());
    }

    @Transactional
    public CategoriaDetalleDTO actualizarCategoria(Integer id, CategoriaRegistroDTO dto){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar que no exista otra categoría con el mismo nombre
        categoriaRepository.findByNombre(dto.nombre())
                .ifPresent(c -> {
                    if (!c.getIdCategoria().equals(id)) {
                        throw new DuplicateResourceException("Ya existe otra categoría con el nombre: " + dto.nombre());
                    }
                });

        categoria.setNombre(dto.nombre());
        categoriaRepository.save(categoria);

        return new CategoriaDetalleDTO(categoria.getIdCategoria(), categoria.getNombre());
    }

    @Transactional
    public void eliminarCategoria(Integer id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoriaRepository.delete(categoria);

        /*Forma sugerida por ChatGPT*/
        /*
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
        */
    }

    public List<CategoriaRespuestaDTO> buscarActivasPorNombre(String nombre){
        return categoriaRepository.buscarActivasPorNombre(nombre).stream()
                .map(categoria -> new CategoriaRespuestaDTO(
                        categoria.getIdCategoria(),
                        categoria.getNombre(),
                        categoria.getActivo()
                ))
                .toList();
    }

    @Transactional
    public void eliminarCategoriaLogica(Integer id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        categoria.setActivo(false);
    }

    @Transactional
    public void activarCategoria(Integer id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        categoria.setActivo(true);
    }

    /*Listar categorias por ACTIVO = TRUE*/
    public List<CategoriaRespuestaDTO> listarActivas() {
        return categoriaRepository.findByActivoTrue().stream()
                .map(c -> new CategoriaRespuestaDTO(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getActivo()
                )).toList();
    }

    /*Listar TODAS las categorías con estado (activas e inactivas)*/
    public List<CategoriaRespuestaDTO> listarTodasConEstado() {
        return categoriaRepository.findAll().stream()
                .map(c -> new CategoriaRespuestaDTO(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getActivo()
                )).toList();
    }

}
