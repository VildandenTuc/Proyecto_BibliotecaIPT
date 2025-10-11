package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.dto.LibroDetalleDTO;
import com.iptucuman.biblioteca.dto.LibroRegistroDTO;
import com.iptucuman.biblioteca.dto.LibroRespuestaDTO;
import com.iptucuman.biblioteca.modelo.Categoria;
import com.iptucuman.biblioteca.modelo.Libro;
import com.iptucuman.biblioteca.repository.CategoriaRepository;
import com.iptucuman.biblioteca.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;

    public LibroService(LibroRepository libroRepository, CategoriaRepository categoriaRepository){
        this.libroRepository = libroRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public LibroDetalleDTO registrarLibro(LibroRegistroDTO dto){
        if (libroRepository.existsByTitulo(dto.titulo())){
            throw new RuntimeException("Este libro ya está registrado");
        }

        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getActivo()) {
            throw new RuntimeException("No se puede registrar un libro en una categoría inactiva.");
        }

        Libro libro = new Libro();
        libro.setTitulo(dto.titulo());
        libro.setAutores(dto.autores());
        libro.setCategoria(categoria);
        libro.setEjemplares(dto.ejemplares());
        libro.setDisponible(dto.disponible());

        Libro libroGuardado = libroRepository.save(libro);

        return new LibroDetalleDTO(
                libroGuardado.getIdLibro(),
                libroGuardado.getTitulo(),
                libroGuardado.getAutores(),
                libroGuardado.getCategoria().getNombre(),
                libroGuardado.getEjemplares(),
                libroGuardado.getDisponible()
        );
    }

    /*FORMA con List lista todos los libros disponibles y no disponibles*/
    public List<LibroDetalleDTO> listarLibros(){
        return libroRepository.findAll().stream()
                .map(libro -> new LibroDetalleDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutores(),
                        libro.getCategoria().getNombre(),
                        libro.getEjemplares(),
                        libro.getDisponible()
                ))
                .collect(Collectors.toList());
    }

    /*Lista solo los libros que estan disponibles*/
    /*
    public List<LibroDetalleDTO> listarDisponibles() {
        return libroRepository.findByDisponibleTrue().stream()
                .map(libro -> new LibroDetalleDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutores(),
                        libro.getCategoria().getNombre(),
                        libro.getEjemplares(),
                        libro.getDisponible()
                )).toList();
    }*/
    public Page<LibroDetalleDTO> buscarLibrosDisponibles(Pageable pageable) {
        return libroRepository.findByDisponibleTrue(pageable)
                .map(this::mapearADetalleDTO);
    }

    /*Lista TODOS los libros con paginación (disponibles y no disponibles)*/
    public Page<LibroDetalleDTO> listarTodosConPage(Pageable pageable) {
        return libroRepository.findAll(pageable)
                .map(this::mapearADetalleDTO);
    }

    /*Lista solo los libros NO disponibles con paginación*/
    public Page<LibroDetalleDTO> listarNoDisponiblesConPage(Pageable pageable) {
        return libroRepository.findByDisponibleFalse(pageable)
                .map(this::mapearADetalleDTO);
    }

    /*Obtener Libro por ID*/
    public LibroDetalleDTO obtenerLibroPorId(Integer id){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        return new LibroDetalleDTO(
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutores(),
                libro.getCategoria().getNombre(),
                libro.getEjemplares(),
                libro.getDisponible()
        );
    }

    /*Actualizar libro por ID*/
    @Transactional
    public LibroDetalleDTO actualizarLibro(Integer id, LibroRegistroDTO dto){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getActivo()) {
            throw new RuntimeException("No se puede asignar una categoría inactiva.");
        }

        libro.setTitulo(dto.titulo());
        libro.setAutores(dto.autores());
        libro.setCategoria(categoria);
        libro.setEjemplares(dto.ejemplares());
        libro.setDisponible(dto.disponible());

        return new LibroDetalleDTO(
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutores(),
                libro.getCategoria().getNombre(),
                libro.getEjemplares(),
                libro.getDisponible()
        );
    }

    @Transactional
    public void eliminarLibro(Integer id){
        if (!libroRepository.existsById(id)){
            throw new RuntimeException("El libro no se encuentra.");
        }

        libroRepository.deleteById(id);
    }

    @Transactional
    public void eliminarLibroLogica(Integer id){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado."));

        libro.setDisponible(false);
    }

    @Transactional
    public void activarLibro(Integer id){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado."));

        libro.setDisponible(true);
    }

    /*
    public List<LibroRespuestaDTO> buscarPorCategoria(Integer categoria){
        return libroRepository.findByCategoriaIdCategoria(categoria)
                .stream()
                .map(libro -> new LibroRespuestaDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutores(),
                        libro.getCategoria().getNombre(),
                        libro.getEjemplares(),
                        libro.getDisponible()
                ))
                .toList();
    }*/
    public Page<LibroDetalleDTO> buscarPorCategoria(Integer idCategoria, Pageable pageable) {
        return libroRepository.findByCategoriaIdCategoria(idCategoria, pageable)
                .map(this::mapearADetalleDTO);
    }

    /*
    public List<LibroRespuestaDTO> buscarPorAutor(String autor){
        return libroRepository.buscarPorAutor(autor)
                .stream()
                .map(libro -> new LibroRespuestaDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutores(),
                        libro.getCategoria().getNombre(),
                        libro.getEjemplares(),
                        libro.getDisponible()
                ))
                .toList();
    }*/
    public Page<LibroDetalleDTO> buscarPorAutor(String autor, Pageable pageable) {
        return libroRepository.buscarPorAutor(autor, pageable)
                .map(this::mapearADetalleDTO);
    }

    public Page<LibroDetalleDTO> buscarPorTitulo(String titulo, Pageable pageable) {
        return libroRepository.buscarPorTitulo(titulo, pageable)
                .map(this::mapearADetalleDTO);
    }









    /*******************************************************************/
    //*********************Metodo llamado en varias ocaciones **********/
    private LibroDetalleDTO mapearADetalleDTO(Libro libro) {
        return new LibroDetalleDTO(
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutores(),
                libro.getCategoria().getNombre(),
                libro.getEjemplares(),
                libro.getDisponible()
        );
    }

}
