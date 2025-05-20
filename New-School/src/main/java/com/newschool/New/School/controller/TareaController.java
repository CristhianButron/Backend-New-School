package com.newschool.New.School.controller;

import com.newschool.New.School.dto.tarea.TareaDTO;
import com.newschool.New.School.dto.tarea.TareaRequestDTO;
import com.newschool.New.School.dto.tarea.TareaResponseDTO;
import com.newschool.New.School.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tareas")
@CrossOrigin(origins = "*")
@Tag(name = "Tareas", description = "API para gestionar las tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    @Operation(summary = "Obtener todas las tareas")
    public ResponseEntity<List<TareaDTO>> listaTareas() {
        return ResponseEntity.ok(tareaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarea por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaDTO> getTareaById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(tareaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Obtener tareas por curso")
    public ResponseEntity<List<TareaDTO>> getTareasByCursoId(@PathVariable Integer cursoId) {
        return ResponseEntity.ok(tareaService.findByCursoId(cursoId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<TareaResponseDTO> createTarea(@RequestBody TareaRequestDTO tareaRequestDTO) {
        try {
            TareaResponseDTO createdTarea = tareaService.crear(tareaRequestDTO);
            return new ResponseEntity<>(createdTarea, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaResponseDTO> updateTarea(
            @PathVariable Integer id,
            @RequestBody TareaRequestDTO tareaRequestDTO) {
        try {
            return ResponseEntity.ok(tareaService.actualizar(id, tareaRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> deleteTarea(@PathVariable Integer id) {
        try {
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}