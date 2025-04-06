package com.newschool.New.School.controller;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.service.GradoService;
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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/grados")
@Tag(name = "Grados", description = "API para gestionar los grados escolares")
public class GradoController {

    private final GradoService gradoService;

    @Autowired
    public GradoController(GradoService gradoService) {
        this.gradoService = gradoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los grados", description = "Recupera una lista de todos los grados escolares disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grados recuperada con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class)))
    })
    public ResponseEntity<List<GradoDTO>> getAllGrados() {
        return ResponseEntity.ok(gradoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un grado por ID", description = "Recupera un grado escolar específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Grado no encontrado")
    })
    public ResponseEntity<GradoDTO> getGradoById(
            @Parameter(description = "ID del grado a buscar", required = true) @PathVariable Integer id) {
        try {
            GradoDTO gradoDTO = gradoService.findById(id);
            return ResponseEntity.ok(gradoDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/descripcion/{descripcion}")
    @Operation(summary = "Buscar grado por descripción", description = "Busca un grado escolar por su descripción exacta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Grado no encontrado")
    })
    public ResponseEntity<GradoDTO> getGradoByDescripcion(
            @Parameter(description = "Descripción del grado a buscar", required = true) @PathVariable String descripcion) {
        try {
            GradoDTO gradoDTO = gradoService.findByDescripcion(descripcion);
            return ResponseEntity.ok(gradoDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/nivel")
    @Operation(summary = "Filtrar grados por nivel", description = "Filtra los grados según sean de primaria o secundaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grados filtrada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class)))
    })
    public ResponseEntity<List<GradoDTO>> getGradosByNivel(
            @Parameter(description = "true para primaria, false para secundaria", required = true)
            @RequestParam Boolean esPrimaria) {
        return ResponseEntity.ok(gradoService.findByPrimariaSencundaria(esPrimaria));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo grado", description = "Crea un nuevo grado escolar en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grado creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de grado inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un grado con la misma descripción")
    })
    public ResponseEntity<GradoDTO> createGrado(
            @Parameter(description = "Datos del grado a crear", required = true)
            @Valid @RequestBody GradoDTO gradoDTO) {
        try {
            GradoDTO createdGrado = gradoService.save(gradoDTO);
            return new ResponseEntity<>(createdGrado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si el mensaje indica que ya existe, es un conflicto
            if (e.getMessage().contains("Ya existe un grado")) {
                throw new RuntimeException("Conflicto: " + e.getMessage());
            }
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un grado", description = "Actualiza la información de un grado escolar existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grado actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Grado no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de grado inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe otro grado con la misma descripción")
    })
    public ResponseEntity<GradoDTO> updateGrado(
            @Parameter(description = "ID del grado a actualizar", required = true) @PathVariable Integer id,
            @Parameter(description = "Nuevos datos del grado", required = true) @Valid @RequestBody GradoDTO gradoDTO) {
        try {
            GradoDTO updatedGrado = gradoService.update(id, gradoDTO);
            return ResponseEntity.ok(updatedGrado);
        } catch (RuntimeException e) {
            // Si el mensaje indica que ya existe, es un conflicto
            if (e.getMessage().contains("Ya existe otro grado")) {
                throw new RuntimeException("Conflicto: " + e.getMessage());
            }
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un grado", description = "Elimina un grado escolar del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grado no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar el grado porque tiene dependencias")
    })
    public ResponseEntity<Void> deleteGrado(
            @Parameter(description = "ID del grado a eliminar", required = true) @PathVariable Integer id) {
        try {
            gradoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}