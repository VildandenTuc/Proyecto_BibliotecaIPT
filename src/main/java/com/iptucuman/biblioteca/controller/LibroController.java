package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.LibroDetalleDTO;
import com.iptucuman.biblioteca.dto.LibroRegistroDTO;
import com.iptucuman.biblioteca.dto.LibroRespuestaDTO;
import com.iptucuman.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService){
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<LibroDetalleDTO> registrar(@RequestBody @Valid LibroRegistroDTO dto){
        LibroDetalleDTO nuevo = libroService.registrarLibro(dto);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<LibroDetalleDTO>> listarConList(){
        return ResponseEntity.ok(libroService.listarLibros());
    }

    /*
    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroDetalleDTO>> listarDisponibles() {
        return ResponseEntity.ok(libroService.listarDisponibles());
    }*/
    @GetMapping("/disponibles")
    public Page<LibroDetalleDTO> buscarLibrosDisponibles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.buscarLibrosDisponibles(pageable);
    }

    @GetMapping("/todos")
    public Page<LibroDetalleDTO> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.listarTodosConPage(pageable);
    }

    @GetMapping("/no-disponibles")
    public Page<LibroDetalleDTO> listarNoDisponibles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.listarNoDisponiblesConPage(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDetalleDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(libroService.obtenerLibroPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDetalleDTO> actualizar(@PathVariable Integer id, @RequestBody @Valid LibroRegistroDTO dto){
        return ResponseEntity.ok(libroService.actualizarLibro(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/logica/{id}")
    public ResponseEntity<?> eliminarLógica(@PathVariable Integer id) {
        libroService.eliminarLibroLogica(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<Void> activarLibro(@PathVariable Integer id) {
        libroService.activarLibro(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping("/buscar/categoria/{id}")
    public ResponseEntity<List<LibroRespuestaDTO>> buscarPorCategoria(@PathVariable Integer id){
        return ResponseEntity.ok(libroService.buscarPorCategoria(id));
    }*/
    @GetMapping("/categoria/{idCategoria}")
    public Page<LibroDetalleDTO> buscarPorCategoria(
            @PathVariable Integer idCategoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.buscarPorCategoria(idCategoria, pageable);
    }

    /*
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<LibroRespuestaDTO>> buscarPorAutor(@RequestParam String autor){
        return ResponseEntity.ok(libroService.buscarPorAutor(autor));
    }*/
    @GetMapping("/autor")
    public Page<LibroDetalleDTO> buscarPorAutor(
            @RequestParam String autor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.buscarPorAutor(autor, pageable);
    }

    @GetMapping("/titulo")
    public Page<LibroDetalleDTO> buscarPorTitulo(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idLibro") String sort) {
        Pageable pageable = PageRequest.of(page, size, parsearOrdenamiento(sort));
        return libroService.buscarPorTitulo(titulo, pageable);
    }

    /**
     * Método helper para parsear el parámetro sort que puede venir como "campo" o "campo,direccion"
     */
    private Sort parsearOrdenamiento(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "idLibro");
        }

        String[] partes = sort.split(",");
        String campo = partes[0].trim();

        if (partes.length > 1) {
            String direccion = partes[1].trim();
            Sort.Direction direction = direccion.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return Sort.by(direction, campo);
        }

        return Sort.by(Sort.Direction.ASC, campo);
    }

}
