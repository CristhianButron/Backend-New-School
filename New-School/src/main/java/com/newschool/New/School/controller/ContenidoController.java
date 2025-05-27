package com.newschool.New.School.controller;

import com.newschool.New.School.dto.contenido.ContenidoDTO;
import com.newschool.New.School.dto.contenido.ContenidoRequestDTO;
import com.newschool.New.School.dto.contenido.ContenidoResponseDTO;
import com.newschool.New.School.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contenidos")
@Tag(name = "Contenidos", description = "API para gestionar los contenidos de los cursos")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo contenido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<ContenidoResponseDTO> crear(@Valid @RequestBody ContenidoRequestDTO requestDTO) {
        ContenidoResponseDTO responseDTO = contenidoService.crear(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener contenido por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenido encontrado"),
        @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    public ResponseEntity<ContenidoDTO> obtenerPorId(
            @Parameter(description = "ID del contenido") @PathVariable Integer id) {
        return contenidoService.obtenerPorId(id)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado con ID: " + id));
    }

   

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar contenidos por tipo")
    public ResponseEntity<List<ContenidoDTO>> obtenerPorTipo(@PathVariable String tipo) {
        List<ContenidoDTO> contenidos = contenidoService.obtenerPorTipo(tipo);
        return new ResponseEntity<>(contenidos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar contenido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenido actualizado"),
        @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    public ResponseEntity<ContenidoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ContenidoRequestDTO requestDTO) {
        ContenidoResponseDTO responseDTO = contenidoService.actualizar(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contenido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contenido eliminado"),
        @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contenidoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Listar todos los contenidos")
    public ResponseEntity<List<ContenidoDTO>> obtenerTodos() {
        List<ContenidoDTO> contenidos = contenidoService.obtenerTodos();
        return new ResponseEntity<>(contenidos, HttpStatus.OK);
    }
}