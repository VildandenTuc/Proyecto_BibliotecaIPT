package com.iptucuman.biblioteca.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> manejarRecursoNoEncontrado(ResourceNotFoundException ex) {
        return new ResponseEntity<>(generarCuerpoError(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> manejarIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(generarCuerpoError(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> manejarTipoArgumento(MethodArgumentTypeMismatchException ex) {
        String mensaje = "Parámetro inválido: " + ex.getName();
        return new ResponseEntity<>(generarCuerpoError(mensaje, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> manejarRecursoDuplicado(DuplicateResourceException ex) {
        return new ResponseEntity<>(
                generarCuerpoError(ex.getMessage(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> manejarViolacionIntegridad(DataIntegrityViolationException ex) {
        String mensaje = "Ya existe un registro con esos datos únicos";

        // Detectar tipo específico de violación según el mensaje de error
        String errorMsg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (errorMsg.contains("dni")) {
            mensaje = "Ya existe un usuario con ese DNI";
        } else if (errorMsg.contains("email")) {
            mensaje = "Ya existe un usuario con ese email";
        } else if (errorMsg.contains("nombre") && errorMsg.contains("categorias")) {
            mensaje = "Ya existe una categoría con ese nombre";
        }

        return new ResponseEntity<>(
                generarCuerpoError(mensaje, HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarErroresGenerales(Exception ex) {
        return new ResponseEntity<>(generarCuerpoError("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> generarCuerpoError(String mensaje, HttpStatus estado) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("message", mensaje);
        return cuerpo;
    }
}
