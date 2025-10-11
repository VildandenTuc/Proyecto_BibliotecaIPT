package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.CategoriaDetalleDTO;
import com.iptucuman.biblioteca.dto.CategoriaRegistroDTO;
import com.iptucuman.biblioteca.dto.CategoriaRespuestaDTO;
import com.iptucuman.biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaDetalleDTO> registrar(@RequestBody @Valid CategoriaRegistroDTO dto){
        CategoriaDetalleDTO nueva = categoriaService.registrarCategoria(dto);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaRespuestaDTO>> listarConList(){
        return ResponseEntity.ok(categoriaService.listarTodasConEstado());
    }

    @GetMapping("/paginas")
    public ResponseEntity<Page<CategoriaDetalleDTO>> listarConPage(Pageable pageable){
        Page<CategoriaDetalleDTO> categorias = categoriaService.listarCategoriaConPage(pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDetalleDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDetalleDTO> actualizarPorId(@PathVariable Integer id, @RequestBody @Valid CategoriaRegistroDTO dto){
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Integer id){
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/activas")
    public ResponseEntity<List<CategoriaRespuestaDTO>> buscarActivasPorNombre(@RequestParam String nombre){
        return ResponseEntity.ok(categoriaService.buscarActivasPorNombre(nombre));
    }

    @DeleteMapping("/eliminacion-logica/{id}")
    public ResponseEntity<Void> eliminarPorIdLogica(@PathVariable Integer id){
        categoriaService.eliminarCategoriaLogica(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<Void> activarCategoria(@PathVariable Integer id){
        categoriaService.activarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaRespuestaDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listarActivas());
    }

}
