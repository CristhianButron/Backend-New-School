package com.newschool.New.School.controller;

import com.newschool.New.School.dto.inscripcion.InscripcionGradoDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoRequestDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoResponseDTO;
import com.newschool.New.School.service.InscripcionGradoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@Validated
@Tag(name = "Inscripciones a Grados", description = "API para gestionar las inscripciones de estudiantes a grados")
public class InscripcionGradoController {

    private final InscripcionGradoService inscripcionService;

    @Autowired
    public InscripcionGradoController(InscripcionGradoService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva inscripción", description = "Registra una nueva inscripción de un estudiante a un grado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente",
                    content = @Content(schema = @Schema(implementation = InscripcionGradoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de inscripción inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudiante o grado no encontrado"),
            @ApiResponse(responseCode = "409", description = "El estudiante ya está inscrito en esta gestión")
    })
    public ResponseEntity<InscripcionGradoResponseDTO> crear(
            @Valid @RequestBody InscripcionGradoRequestDTO requestDTO) {
        try {
            InscripcionGradoResponseDTO responseDTO = inscripcionService.crear(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("ya está inscrito")) {
                throw new RuntimeException("Conflicto: " + e.getMessage());
            }
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID", description = "Busca y retorna los detalles de una inscripción específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción encontrada",
                    content = @Content(schema = @Schema(implementation = InscripcionGradoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<InscripcionGradoDTO> obtenerPorId(
            @Parameter(description = "ID de la inscripción a buscar", required = true)
            @PathVariable Integer id) {
        return inscripcionService.obtenerPorId(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Listar inscripciones por estudiante", description = "Obtiene todas las inscripciones de un estudiante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones recuperada correctamente")
    })
    public ResponseEntity<List<InscripcionGradoDTO>> obtenerPorEstudiante(
            @Parameter(description = "ID del estudiante", required = true)
            @PathVariable Integer estudianteId) {
        List<InscripcionGradoDTO> inscripciones = inscripcionService.obtenerPorEstudiante(estudianteId);
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    @GetMapping("/grado/{gradoId}")
    @Operation(summary = "Listar inscripciones por grado", description = "Obtiene todas las inscripciones a un grado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones recuperada correctamente")
    })
    public ResponseEntity<List<InscripcionGradoDTO>> obtenerPorGrado(
            @Parameter(description = "ID del grado", required = true)
            @PathVariable Integer gradoId) {
        List<InscripcionGradoDTO> inscripciones = inscripcionService.obtenerPorGrado(gradoId);
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    @GetMapping("/gestion/{gestion}")
    @Operation(summary = "Listar inscripciones por año de gestión", description = "Obtiene todas las inscripciones realizadas en un año escolar específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones recuperada correctamente")
    })
    public ResponseEntity<List<InscripcionGradoDTO>> obtenerPorGestion(
            @Parameter(description = "Año de gestión", required = true, example = "2025")
            @PathVariable Integer gestion) {
        List<InscripcionGradoDTO> inscripciones = inscripcionService.obtenerPorGestion(gestion);
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inscripción", description = "Actualiza los datos de una inscripción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = InscripcionGradoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Inscripción, estudiante o grado no encontrado")
    })
    public ResponseEntity<InscripcionGradoResponseDTO> actualizar(
            @Parameter(description = "ID de la inscripción a actualizar", required = true)
            @PathVariable Integer id,
            @Valid @RequestBody InscripcionGradoRequestDTO requestDTO) {
        InscripcionGradoResponseDTO responseDTO = inscripcionService.actualizar(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todas las inscripciones", description = "Obtiene la lista completa de inscripciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones recuperada correctamente")
    })
    public ResponseEntity<List<InscripcionGradoDTO>> obtenerTodos() {
        List<InscripcionGradoDTO> inscripciones = inscripcionService.obtenerTodos();
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inscripción", description = "Elimina una inscripción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la inscripción a eliminar", required = true)
            @PathVariable Integer id) {
        inscripcionService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}