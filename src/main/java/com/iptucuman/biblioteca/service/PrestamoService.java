package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.dto.LibroPrestadoEstadisticaDTO;
import com.iptucuman.biblioteca.dto.PrestamoRegistroDTO;
import com.iptucuman.biblioteca.dto.PrestamoRespuestaDTO;
import com.iptucuman.biblioteca.modelo.Libro;
import com.iptucuman.biblioteca.modelo.Prestamo;
import com.iptucuman.biblioteca.modelo.Usuario;
import com.iptucuman.biblioteca.repository.LibroRepository;
import com.iptucuman.biblioteca.repository.PrestamoRepository;
import com.iptucuman.biblioteca.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;


    @Transactional
    public void registrarPrestamo(PrestamoRegistroDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Libro libro = libroRepository.findById(dto.idLibro())
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado."));

        if (libro.getEjemplares() <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles para este libro.");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(dto.fechaPrestamo());
        prestamo.setFechaDevolucionEsperada(dto.fechaDevolucionEsperada());
        prestamo.setDevuelto(false);
        prestamo.setFalta(false);

        // Descontar un ejemplar
        libro.setEjemplares(libro.getEjemplares() - 1);
        libroRepository.save(libro);

        if (prestamoRepository.existePrestamoActivoDelMismoLibro(dto.idUsuario(), dto.idLibro())) {
            throw new IllegalStateException("El usuario ya tiene un préstamo activo de este libro.");
        }

        prestamoRepository.save(prestamo);
    }

    public List<PrestamoRespuestaDTO> listarPrestamos(){
        return prestamoRepository.findAll()
                .stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                ))
                .toList();
    }

    @Transactional
    public void registrarDevolucion(Integer idPrestamo){
        //Forma que hice inicialmente
        /*
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        if (prestamo.getDevuelto()){
            throw new IllegalStateException("Préstamo ya devuelto");
        }

        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionReal(LocalDate.now());

        //Incrementar ejemplar al devolver
        Libro libro = prestamo.getLibro();
        libro.setEjemplares(libro.getEjemplares() + 1);
        libroRepository.save(libro);

        prestamoRepository.save(prestamo);
        */

        /*Segunda forma usando Optional*/
        Optional<Prestamo> prestamoOptional = prestamoRepository.findById(idPrestamo);

        if (prestamoOptional.isEmpty()) {
            throw new IllegalArgumentException("Préstamo no encontrado");
        }

        Prestamo prestamo = prestamoOptional.get();

        if (prestamo.getDevuelto()) {
            throw new IllegalStateException("El préstamo ya fue devuelto");
        }

        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionReal(LocalDate.now());

        // Aumentar stock del libro
        Libro libro = prestamo.getLibro();
        libro.setEjemplares(libro.getEjemplares() + 1);
        libroRepository.save(libro);

        prestamoRepository.save(prestamo);
    }

    public List<PrestamoRespuestaDTO> listarPrestamosPorUsuarios(Integer idUsuario){
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioIdUsuario(idUsuario);

        return prestamos.stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                ))
                .toList();
    }

    /*
    public List<PrestamoRespuestaDTO> listarPrestamosPorLibro(Integer idLibro){
        return prestamoRepository.findByLibroIdLibro(idLibro)
                .stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                ))
                .toList();
    }*/
    public Page<PrestamoRespuestaDTO> listarPrestamosPorLibro(Integer idLibro, Pageable pageable) {
        return prestamoRepository.findByLibroIdLibro(idLibro, pageable)
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                ));
    }


    public List<PrestamoRespuestaDTO> listarPrestamosNoDevueltos() {
        return prestamoRepository.findByDevueltoFalse().stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                ))
                .toList();
    }

    /*
    public List<PrestamoRespuestaDTO> buscarPrestamosPorUsuarioTexto(String texto) {
        return prestamoRepository.buscarPorNombreApellidoUsuario(texto).stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                )).toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorNombreApellidoUsuario(String texto, Pageable pageable) {
        return prestamoRepository
                .buscarPorNombreApellidoUsuario(texto, pageable)
                .map(this::mapearADTO);
    }

    /*
    public List<PrestamoRespuestaDTO> buscarPrestamosPorTituloLibro(String titulo) {
        return prestamoRepository.buscarPorTituloLibro(titulo).stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                )).toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorTituloLibro(String titulo, Pageable pageable) {
        return prestamoRepository.buscarPorTituloLibro(titulo, pageable)
                .map(this::mapearADTO);
    }

    /*
    public List<PrestamoRespuestaDTO> buscarPrestamosPorDniUsuario(String dni) {
        return prestamoRepository.buscarPorDniUsuario(dni).stream()
                .map(prestamo -> new PrestamoRespuestaDTO(
                        prestamo.getIdPrestamo(),
                        prestamo.getUsuario().getApellido(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        prestamo.getFechaDevolucionReal(),
                        prestamo.getDevuelto(),
                        prestamo.getFalta(),
                        prestamo.getObservacion()
                )).toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorDniUsuario(String dni, Pageable pageable) {
        return prestamoRepository.buscarPorDniUsuario(dni, pageable)
                .map(this::mapearADTO);
    }


    /*Busqueda por fecha*//*
    public List<PrestamoRespuestaDTO> buscarPorFechaPrestamo(LocalDate desde, LocalDate hasta) {
        return prestamoRepository.buscarPorFechaPrestamoEntre(desde, hasta).stream()
                .map(this::mapearADTO)
                .toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorFechaPrestamoEntre(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return prestamoRepository.buscarPorFechaPrestamoEntre(desde, hasta, pageable)
                .map(this::mapearADTO);
    }

    /*
    public List<PrestamoRespuestaDTO> buscarPorFechaDevolucionEsperada(LocalDate desde, LocalDate hasta) {
        return prestamoRepository.buscarPorFechaDevolucionEsperadaEntre(desde, hasta).stream()
                .map(this::mapearADTO)
                .toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorFechaDevolucionEsperadaEntre(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return prestamoRepository.buscarPorFechaDevolucionEsperadaEntre(desde, hasta, pageable)
                .map(this::mapearADTO);
    }

    /*
    public List<PrestamoRespuestaDTO> buscarVencidosNoDevueltos() {
        LocalDate hoy = LocalDate.now();
        return prestamoRepository.buscarVencidosNoDevueltos(hoy).stream()
                .map(this::mapearADTO)
                .toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarVencidosNoDevueltos(LocalDate hoy, Pageable pageable) {
        return prestamoRepository.buscarVencidosNoDevueltos(hoy, pageable)
                .map(this::mapearADTO);
    }

    /*
    public List<PrestamoRespuestaDTO> buscarPorFalta() {
        return prestamoRepository.findByFaltaTrue().stream()
                .map(this::mapearADTO)
                .toList();
    }*/
    public Page<PrestamoRespuestaDTO> buscarPorFalta(Pageable pageable) {
        return prestamoRepository.findByFaltaTrue(pageable)
                .map(this::mapearADTO);
    }


    public List<PrestamoRespuestaDTO> buscarPrestamosActivosPorUsuario(Integer idUsuario) {
        return prestamoRepository.findByUsuarioIdUsuarioAndDevueltoFalse(idUsuario).stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<PrestamoRespuestaDTO> buscarHistorialPorUsuario(Integer idUsuario) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioIdUsuario(idUsuario);
        return prestamos.stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<PrestamoRespuestaDTO> buscarVencimientosProximos(int dias) {
        LocalDate fechaObjetivo = LocalDate.now().plusDays(dias);
        List<Prestamo> prestamos = prestamoRepository.buscarPorVencimientoProximo(fechaObjetivo);
        return prestamos.stream().map(this::mapearADTO).toList();
    }


    public List<LibroPrestadoEstadisticaDTO> obtenerLibrosMasPrestados() {
        return prestamoRepository.obtenerLibrosMasPrestadosRaw()
                .stream()
                .map(obj -> new LibroPrestadoEstadisticaDTO(
                        (Integer) obj[0],
                        (String) obj[1],
                        (Long) obj[2]
                ))
                .toList();
    }


    /*************** OJO *********************************/
    /*Esta función es para uso general*/
    private PrestamoRespuestaDTO mapearADTO(Prestamo p) {
        return new PrestamoRespuestaDTO(
                p.getIdPrestamo(),
                p.getUsuario().getNombre(),
                p.getUsuario().getApellido(),
                p.getLibro().getTitulo(),
                p.getFechaPrestamo(),
                p.getFechaDevolucionEsperada(),
                p.getFechaDevolucionReal(),
                p.getDevuelto(),
                p.getFalta(),
                p.getObservacion()
        );
    }

}
