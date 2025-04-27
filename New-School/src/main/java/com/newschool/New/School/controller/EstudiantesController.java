package com.newschool.New.School.controller;

import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.dto.estudiante.EstudianteDTO;

import com.newschool.New.School.dto.estudiante.EstudianteRequestDTO;
import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.service.AuthService;
import com.newschool.New.School.service.EstudianteService;
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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiante")
@CrossOrigin(origins = "*")
public class EstudiantesController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listaEstudiantes() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> getEstudianteById(@PathVariable Integer id) {
        EstudianteDTO estudianteDTO = estudianteService.findById(id);
        if (estudianteDTO != null) {
            return ResponseEntity.ok(estudianteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EstudianteDTO> getEstudianteByUsuarioId(@PathVariable Integer usuarioId) {
        EstudianteDTO estudianteDTO = estudianteService.findByUsuarioId(usuarioId);
        if (estudianteDTO != null) {
            return ResponseEntity.ok(estudianteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> createEstudiante(
            @RequestPart("estudiante") EstudianteRequestDTO estudianteRequestDTO,
            @RequestPart(value = "certificado", required = false) MultipartFile certificado) {
        try {
            if (certificado != null) {
                estudianteRequestDTO.setCertificadoNacimiento(certificado.getBytes());
            }
            EstudianteResponseDTO createdEstudiante = estudianteService.createEstudiante(estudianteRequestDTO);
            return new ResponseEntity<>(createdEstudiante, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(
            @PathVariable Integer id,
            @RequestPart("estudiante") EstudianteRequestDTO estudianteRequestDTO,
            @RequestPart(value = "certificado", required = false) MultipartFile certificado) {
        try {
            if (certificado != null) {
                estudianteRequestDTO.setCertificadoNacimiento(certificado.getBytes());
            }
            EstudianteResponseDTO updatedEstudiante = estudianteService.updateEstudiante(id, estudianteRequestDTO);
            if (updatedEstudiante != null) {
                return ResponseEntity.ok(updatedEstudiante);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Integer id) {
        boolean deleted = estudianteService.deleteEstudiante(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/registro-completo")
    @Operation(
            summary = "Registrar un nuevo estudiante completo",
            description = "Crea un nuevo usuario con rol ESTUDIANTE y sus datos específicos en una sola operación"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud mal formada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para el registro del estudiante",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo registro estudiante",
                                    summary = "Ejemplo de datos para registrar un estudiante",
                                    value = "{\n" +
                                            "  \"ci\": \"87654321\",\n" +
                                            "  \"nombre\": \"Maria\",\n" +
                                            "  \"apellido\": \"González\",\n" +
                                            "  \"email\": \"maria.gonzalez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"fechaNacimiento\": \"2000-01-15\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> registrarEstudianteCompleto(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // Aseguramos que el rol sea ESTUDIANTE
            registerRequestDTO.setRol("ESTUDIANTE");

            // Si no hay datosEspecificos, los inicializamos
            if (registerRequestDTO.getDatosEspecificos() == null) {
                registerRequestDTO.setDatosEspecificos(new HashMap<>());
            }

            // Verificamos que la fecha de nacimiento exista
            if (!registerRequestDTO.getDatosEspecificos().containsKey("fechaNacimiento")) {
                throw new RuntimeException("La fecha de nacimiento es requerida");
            }

            // Usamos el servicio de autenticación para registrar
            return ResponseEntity.ok(authService.register(registerRequestDTO));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la solicitud: " + e.getMessage(), e);
        }
    }
}