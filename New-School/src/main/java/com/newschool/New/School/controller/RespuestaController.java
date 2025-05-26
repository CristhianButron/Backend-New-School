package com.newschool.New.School.controller;

import com.newschool.New.School.dto.respuesta.RespuestaDTO;
import com.newschool.New.School.dto.respuesta.RespuestaRequestDTO;
import com.newschool.New.School.dto.respuesta.RespuestaResponseDTO;
import com.newschool.New.School.service.RespuestaService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/respuestas")
@CrossOrigin(origins = "*")
@Tag(name = "Respuestas", description = "API para gestionar las respuestas a las tareas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @GetMapping
    @Operation(summary = "Obtener todas las respuestas")
    public ResponseEntity<List<RespuestaDTO>> listaRespuestas() {
        return ResponseEntity.ok(respuestaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una respuesta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta encontrada"),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada")
    })
    public ResponseEntity<RespuestaDTO> getRespuestaById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(respuestaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tarea/{tareaId}")
    @Operation(summary = "Obtener respuestas por tarea")
    public ResponseEntity<List<RespuestaDTO>> getRespuestasByTareaId(@PathVariable Integer tareaId) {
        return ResponseEntity.ok(respuestaService.findByTareaId(tareaId));
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener respuestas por estudiante")
    public ResponseEntity<List<RespuestaDTO>> getRespuestasByEstudianteId(@PathVariable Integer estudianteId) {
        return ResponseEntity.ok(respuestaService.findByEstudianteId(estudianteId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Respuesta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarea o estudiante no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una respuesta para esta tarea del estudiante")
    })
    public ResponseEntity<RespuestaResponseDTO> createRespuesta(
            @RequestPart("respuesta") RespuestaRequestDTO respuestaRequestDTO,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        try {
            if (archivo != null) {
                respuestaRequestDTO.setArchivo(new String(archivo.getBytes()));
            }
            RespuestaResponseDTO createdRespuesta = respuestaService.crear(respuestaRequestDTO);
            return new ResponseEntity<>(createdRespuesta, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada")
    })
    public ResponseEntity<RespuestaResponseDTO> updateRespuesta(
            @PathVariable Integer id,
            @RequestPart("respuesta") RespuestaRequestDTO respuestaRequestDTO,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        try {
            if (archivo != null) {
                respuestaRequestDTO.setArchivo(new String(archivo.getBytes()));
            }
            RespuestaResponseDTO updatedRespuesta = respuestaService.actualizar(id, respuestaRequestDTO);
            if (updatedRespuesta != null) {
                return ResponseEntity.ok(updatedRespuesta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Respuesta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada")
    })
    public ResponseEntity<Void> deleteRespuesta(@PathVariable Integer id) {
        try {
            respuestaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calificar/{id}")
    @Operation(summary = "Calificar una respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta calificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada")
    })
    public ResponseEntity<RespuestaResponseDTO> calificarRespuesta(
            @PathVariable Integer id,
            @RequestParam Double calificacion) {
        try {
            RespuestaResponseDTO respuestaCalificada = respuestaService.calificar(id, calificacion);
            return ResponseEntity.ok(respuestaCalificada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}