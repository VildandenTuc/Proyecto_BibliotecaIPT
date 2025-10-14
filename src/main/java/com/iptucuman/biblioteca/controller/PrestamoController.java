package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.LibroPrestadoEstadisticaDTO;
import com.iptucuman.biblioteca.dto.PrestamoDevolucionDTO;
import com.iptucuman.biblioteca.dto.PrestamoRegistroDTO;
import com.iptucuman.biblioteca.dto.PrestamoRespuestaDTO;
import com.iptucuman.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<Void> registrar(@RequestBody @Valid PrestamoRegistroDTO dto){
        prestamoService.registrarPrestamo(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PrestamoRespuestaDTO>> listar(){
        return ResponseEntity.ok(prestamoService.listarPrestamos());
    }

    @GetMapping("/todos")
    public Page<PrestamoRespuestaDTO> listarTodosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Ordenar siempre por fecha de préstamo descendente (más nuevos primero)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.listarPrestamosPaginado(pageable);
    }

    @PutMapping("/devolver")
    public ResponseEntity<Void> devolverPrestamo(@RequestBody @Valid PrestamoDevolucionDTO dto){
        prestamoService.registrarDevolucion(dto.idPrestamo());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<List<PrestamoRespuestaDTO>> listarPorUsuario(@PathVariable Integer id){
        return ResponseEntity.ok(prestamoService.listarPrestamosPorUsuarios(id));
    }

    /*
    @GetMapping("/libros/{id}")
    public ResponseEntity<List<PrestamoRespuestaDTO>> listarPorLibro(@PathVariable Integer id){
        return ResponseEntity.ok(prestamoService.listarPrestamosPorLibro(id));
    }*/
    @GetMapping("/libros/{idLibro}")
    public Page<PrestamoRespuestaDTO> listarPorLibro(
            @PathVariable Integer idLibro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaPrestamo,desc") String[] sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.listarPrestamosPorLibro(idLibro, pageable);
    }

    @GetMapping("/nodevueltos")
    public ResponseEntity<List<PrestamoRespuestaDTO>> listarNoDevueltos() {
        return ResponseEntity.ok(prestamoService.listarPrestamosNoDevueltos());
    }

    /*
    @GetMapping("/buscar/usuario")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarPorUsuario(@RequestParam String texto) {
        return ResponseEntity.ok(prestamoService.buscarPrestamosPorUsuarioTexto(texto));
    }*/
    @GetMapping("/buscar/usuario")
    public Page<PrestamoRespuestaDTO> buscarPorNombreApellidoUsuario(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.buscarPorNombreApellidoUsuario(texto, pageable);
    }

    /*
    @GetMapping("/buscar/libro")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(prestamoService.buscarPrestamosPorTituloLibro(titulo));
    }*/
    @GetMapping("/buscar/libro")
    public Page<PrestamoRespuestaDTO> buscarPorTituloLibro(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.buscarPorTituloLibro(titulo, pageable);
    }

    /*
    @GetMapping("/buscar/dni")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        if (!dni.matches("\\d{7,8}")) {
            return ResponseEntity
                    .badRequest()
                    .body("DNI inválido: debe contener solo dígitos y tener entre 7 y 8 caracteres.");
        }

        return ResponseEntity.ok(prestamoService.buscarPrestamosPorDniUsuario(dni));
    }*/
    @GetMapping("/buscar/dni")
    public Page<PrestamoRespuestaDTO> buscarPorDniUsuario(
            @RequestParam String dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.buscarPorDniUsuario(dni, pageable);
    }

    /*
    @GetMapping("/buscar/fecha-prestamo")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarPorFechaPrestamo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return ResponseEntity.ok(prestamoService.buscarPorFechaPrestamo(desde, hasta));
    }*/
    @GetMapping("/buscar/fecha-prestamo")
    public Page<PrestamoRespuestaDTO> buscarPorFechaPrestamoEntre(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.buscarPorFechaPrestamoEntre(desde, hasta, pageable);
    }

    /*
    @GetMapping("/buscar/fecha-devolucion-esperada")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarPorFechaDevolucionEsperada(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return ResponseEntity.ok(prestamoService.buscarPorFechaDevolucionEsperada(desde, hasta));
    }*/
    @GetMapping("/buscar/fecha-devolucion-esperada")
    public Page<PrestamoRespuestaDTO> buscarPorFechaDevolucionEsperadaEntre(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaDevolucionEsperada") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return prestamoService.buscarPorFechaDevolucionEsperadaEntre(desde, hasta, pageable);
    }

    /*
    @GetMapping("/vencidos-no-devueltos")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarVencidosNoDevueltos() {
        return ResponseEntity.ok(prestamoService.buscarVencidosNoDevueltos());
    }*/
    @GetMapping("/buscar/vencidos-no-devueltos")
    public Page<PrestamoRespuestaDTO> buscarVencidosNoDevueltos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        LocalDate hoy = LocalDate.now();
        return prestamoService.buscarVencidosNoDevueltos(hoy, pageable);
    }


    /*
    @GetMapping("/con-falta")
    public ResponseEntity<List<PrestamoRespuestaDTO>> buscarPorFalta() {
        return ResponseEntity.ok(prestamoService.buscarPorFalta());
    }*/
    @GetMapping("/buscar/faltas")
    public Page<PrestamoRespuestaDTO> buscarPorFalta(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fechaPrestamo")));
        return prestamoService.buscarPorFalta(pageable);
    }


    @GetMapping("/activos/usuario/{idUsuario}")
    public ResponseEntity<List<PrestamoRespuestaDTO>> obtenerPrestamosActivosPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(prestamoService.buscarPrestamosActivosPorUsuario(idUsuario));
    }

    @GetMapping("/historial/usuario/{idUsuario}")
    public ResponseEntity<List<PrestamoRespuestaDTO>> obtenerHistorialPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(prestamoService.buscarHistorialPorUsuario(idUsuario));
    }

    @GetMapping("/contador/usuario/{idUsuario}")
    public ResponseEntity<Long> contarPrestamosActivosPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(prestamoService.contarPrestamosActivosPorUsuario(idUsuario));
    }

    @GetMapping("/vencimientos-proximos")
    public List<PrestamoRespuestaDTO> obtenerVencimientosProximos(@RequestParam int dias) {
        return prestamoService.buscarVencimientosProximos(dias);
    }

    @GetMapping("/estadisticas/libros-mas-prestados")
    public ResponseEntity<List<LibroPrestadoEstadisticaDTO>> obtenerLibrosMasPrestados() {
        return ResponseEntity.ok(prestamoService.obtenerLibrosMasPrestados());
    }


    //**************Metodos generales********************
    /*
    private Sort.Order[] parseSort(String[] sort) {
        return Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .toArray(Sort.Order[]::new);
    }*/
    //Version segura del parseSort mejor que la anterior
    private List<Sort.Order> parseSort(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] parts = param.split(",");
            if (parts.length == 2) {
                orders.add(new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]));
            } else if (parts.length == 1) {
                // Si solo hay campo sin dirección, asumir ascendente
                orders.add(new Sort.Order(Sort.Direction.ASC, parts[0]));
            }
        }
        // Si no hay criterios válidos, ordenar por fechaPrestamo descendente por defecto
        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "fechaPrestamo"));
        }
        return orders;
    }



}
