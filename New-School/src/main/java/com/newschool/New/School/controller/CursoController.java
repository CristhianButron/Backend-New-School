package com.newschool.New.School.controller;

import com.newschool.New.School.dto.curso.CursoDTO;
import com.newschool.New.School.dto.curso.CursoRequestDTO;
import com.newschool.New.School.dto.curso.CursoResponseDTO;
import com.newschool.New.School.service.CursoService;
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
@RequestMapping("/api/v1/cursos")
@CrossOrigin(origins = "*")
@Tag(name = "Cursos", description = "API para gestionar los cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    @Operation(summary = "Obtener todos los cursos")
    public ResponseEntity<List<CursoDTO>> listaCursos() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un curso por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<CursoDTO> getCursoById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(cursoService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/docente/{docenteId}")
    @Operation(summary = "Obtener cursos por docente")
    public ResponseEntity<List<CursoDTO>> getCursosByDocenteId(@PathVariable Integer docenteId) {
        return ResponseEntity.ok(cursoService.findByDocenteId(docenteId));
    }

    @GetMapping("/grado/{gradoId}")
    @Operation(summary = "Obtener cursos por grado")
    public ResponseEntity<List<CursoDTO>> getCursosByGradoId(@PathVariable Integer gradoId) {
        return ResponseEntity.ok(cursoService.findByGradoId(gradoId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Docente o grado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un curso con ese nombre")
    })
    public ResponseEntity<CursoResponseDTO> createCurso(@RequestBody CursoRequestDTO cursoRequestDTO) {
        try {
            CursoResponseDTO createdCurso = cursoService.createCurso(cursoRequestDTO);
            return new ResponseEntity<>(createdCurso, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un curso con ese nombre")
    })
    public ResponseEntity<CursoResponseDTO> updateCurso(
            @PathVariable Integer id,
            @RequestBody CursoRequestDTO cursoRequestDTO) {
        try {
            return ResponseEntity.ok(cursoService.updateCurso(id, cursoRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<Void> deleteCurso(@PathVariable Integer id) {
        try {
            cursoService.deleteCurso(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}