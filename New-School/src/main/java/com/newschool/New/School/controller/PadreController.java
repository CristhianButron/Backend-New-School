package com.newschool.New.School.controller;

import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.padre.PadreRequestDTO;
import com.newschool.New.School.dto.padre.PadreResponseDTO;
import com.newschool.New.School.service.AuthService;
import com.newschool.New.School.service.PadreService;
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

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/padres")
@CrossOrigin(origins = "*")
public class PadreController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PadreService padreService;

    @GetMapping
    public ResponseEntity<List<PadreDTO>> listaPadres() {
        return ResponseEntity.ok(padreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PadreDTO> getPadreById(@PathVariable Integer id) {
        PadreDTO padreDTO = padreService.findById(id);
        if (padreDTO != null) {
            return ResponseEntity.ok(padreDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PadreDTO> getPadreByUsuarioId(@PathVariable Integer usuarioId) {
        PadreDTO padreDTO = padreService.findByUsuarioId(usuarioId);
        if (padreDTO != null) {
            return ResponseEntity.ok(padreDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PadreResponseDTO> createPadre(@RequestBody PadreRequestDTO padreRequestDTO) {
        PadreResponseDTO createdPadre = padreService.createPadre(padreRequestDTO);
        return new ResponseEntity<>(createdPadre, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PadreResponseDTO> updatePadre(
            @PathVariable Integer id,
            @RequestBody PadreRequestDTO padreRequestDTO) {
        PadreResponseDTO updatedPadre = padreService.updatePadre(id, padreRequestDTO);
        if (updatedPadre != null) {
            return ResponseEntity.ok(updatedPadre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePadre(@PathVariable Integer id) {
        boolean deleted = padreService.deletePadre(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/registro-completo")
    @Operation(
            summary = "Registrar un nuevo padre completo",
            description = "Crea un nuevo usuario con rol PADRE y sus datos específicos en una sola operación"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Padre registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud mal formada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para el registro del padre",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo registro padre",
                                    summary = "Ejemplo de datos para registrar un padre",
                                    value = "{\n" +
                                            "  \"ci\": \"45678912\",\n" +
                                            "  \"nombre\": \"Roberto\",\n" +
                                            "  \"apellido\": \"Gómez\",\n" +
                                            "  \"email\": \"roberto.gomez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"parentesco\": \"Padre\",\n" +
                                            "    \"estudianteId\": \"1\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> registrarPadreCompleto(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // Aseguramos que el rol sea PADRE
            registerRequestDTO.setRol("PADRE");

            // Si no hay datosEspecificos, los inicializamos
            if (registerRequestDTO.getDatosEspecificos() == null) {
                registerRequestDTO.setDatosEspecificos(new HashMap<>());
            }

            // Verificamos que el parentesco exista
            if (!registerRequestDTO.getDatosEspecificos().containsKey("parentesco")) {
                throw new RuntimeException("El parentesco es requerido");
            }

            // Usamos el servicio de autenticación para registrar
            return ResponseEntity.ok(authService.register(registerRequestDTO));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la solicitud: " + e.getMessage(), e);
        }
    }
}