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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return libroService.buscarLibrosDisponibles(pageable);
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return libroService.buscarPorAutor(autor, pageable);
    }

}
