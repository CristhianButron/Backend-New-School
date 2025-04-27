package com.newschool.New.School.controller;

import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.dto.docente.DocenteDTO;
import com.newschool.New.School.dto.docente.DocenteRequestDTO;
import com.newschool.New.School.dto.docente.DocenteResponseDTO;
import com.newschool.New.School.service.AuthService;
import com.newschool.New.School.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DocenteService docenteService;

    @GetMapping
    public ResponseEntity<List<DocenteDTO>> listaDocentes() {
        return ResponseEntity.ok(docenteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> getDocenteById(@PathVariable Integer id) {
        DocenteDTO docenteDTO = docenteService.findById(id);
        if (docenteDTO != null) {
            return ResponseEntity.ok(docenteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<DocenteDTO> getDocenteByUsuarioId(@PathVariable Integer usuarioId) {
        DocenteDTO docenteDTO = docenteService.findByUsuarioId(usuarioId);
        if (docenteDTO != null) {
            return ResponseEntity.ok(docenteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DocenteResponseDTO> createDocente(
            @RequestPart("docente") DocenteRequestDTO docenteRequestDTO,
            @RequestPart(value = "titulo", required = false) MultipartFile titulo) {
        try {
            if (titulo != null) {
                docenteRequestDTO.setTitulo(titulo.getBytes());
            }
            DocenteResponseDTO createdDocente = docenteService.createDocente(docenteRequestDTO);
            return new ResponseEntity<>(createdDocente, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteResponseDTO> updateDocente(
            @PathVariable Integer id,
            @RequestPart("docente") DocenteRequestDTO docenteRequestDTO,
            @RequestPart(value = "titulo", required = false) MultipartFile titulo) {
        try {
            if (titulo != null) {
                docenteRequestDTO.setTitulo(titulo.getBytes());
            }
            DocenteResponseDTO updatedDocente = docenteService.updateDocente(id, docenteRequestDTO);
            if (updatedDocente != null) {
                return ResponseEntity.ok(updatedDocente);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocente(@PathVariable Integer id) {
        boolean deleted = docenteService.deleteDocente(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/registro-completo")
    @Operation(
            summary = "Registrar un nuevo docente completo",
            description = "Crea un nuevo usuario con rol DOCENTE y sus datos específicos en una sola operación"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud mal formada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para el registro del docente",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo registro docente",
                                    summary = "Ejemplo de datos para registrar un docente",
                                    value = "{\n" +
                                            "  \"ci\": \"12345678\",\n" +
                                            "  \"nombre\": \"Juan\",\n" +
                                            "  \"apellido\": \"Pérez\",\n" +
                                            "  \"email\": \"juan.perez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"licenciatura\": \"Ingeniería en Sistemas\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> registrarDocenteCompleto(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // Aseguramos que el rol sea DOCENTE
            registerRequestDTO.setRol("DOCENTE");

            // Si no hay datosEspecificos, los inicializamos
            if (registerRequestDTO.getDatosEspecificos() == null) {
                registerRequestDTO.setDatosEspecificos(new HashMap<>());
            }

            // Verificamos que la licenciatura exista
            if (!registerRequestDTO.getDatosEspecificos().containsKey("licenciatura")) {
                throw new RuntimeException("La licenciatura es requerida");
            }

            // Usamos el servicio de autenticación para registrar
            return ResponseEntity.ok(authService.register(registerRequestDTO));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la solicitud: " + e.getMessage(), e);
        }
    }
}