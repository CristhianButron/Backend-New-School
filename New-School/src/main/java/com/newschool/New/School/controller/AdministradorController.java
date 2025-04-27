package com.newschool.New.School.controller;

import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorDTO;
import com.newschool.New.School.dto.administrador.AdministradorRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorResponseDTO;
import com.newschool.New.School.service.AdministradorService;
import com.newschool.New.School.service.AuthService;
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
@RequestMapping("/api/v1/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> listaAdministradores() {
        return ResponseEntity.ok(administradorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministradorById(@PathVariable Integer id) {
        AdministradorDTO administradorDTO = administradorService.findById(id);
        if (administradorDTO != null) {
            return ResponseEntity.ok(administradorDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<AdministradorDTO> getAdministradorByUsuarioId(@PathVariable Integer usuarioId) {
        AdministradorDTO administradorDTO = administradorService.findByUsuarioId(usuarioId);
        if (administradorDTO != null) {
            return ResponseEntity.ok(administradorDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> createAdministrador(@RequestBody AdministradorRequestDTO administradorRequestDTO) {
        AdministradorResponseDTO createdAdministrador = administradorService.createAdministrador(administradorRequestDTO);
        return new ResponseEntity<>(createdAdministrador, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> updateAdministrador(
            @PathVariable Integer id,
            @RequestBody AdministradorRequestDTO administradorRequestDTO) {
        AdministradorResponseDTO updatedAdministrador = administradorService.updateAdministrador(id, administradorRequestDTO);
        if (updatedAdministrador != null) {
            return ResponseEntity.ok(updatedAdministrador);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Integer id) {
        boolean deleted = administradorService.deleteAdministrador(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/registro-completo")
    @Operation(
            summary = "Registrar un nuevo administrador completo",
            description = "Crea un nuevo usuario con rol ADMIN y sus datos específicos en una sola operación"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud mal formada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para el registro del administrador",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo registro administrador",
                                    summary = "Ejemplo de datos para registrar un administrador",
                                    value = "{\n" +
                                            "  \"ci\": \"98765432\",\n" +
                                            "  \"nombre\": \"Carlos\",\n" +
                                            "  \"apellido\": \"Ramírez\",\n" +
                                            "  \"email\": \"carlos.ramirez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"cargo\": \"Director Académico\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> registrarAdministradorCompleto(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // Aseguramos que el rol sea ADMIN
            registerRequestDTO.setRol("ADMIN");

            // Si no hay datosEspecificos, los inicializamos
            if (registerRequestDTO.getDatosEspecificos() == null) {
                registerRequestDTO.setDatosEspecificos(new HashMap<>());
            }

            // Verificamos que el cargo exista
            if (!registerRequestDTO.getDatosEspecificos().containsKey("cargo")) {
                throw new RuntimeException("El cargo es requerido");
            }

            // Usamos el servicio de autenticación para registrar
            return ResponseEntity.ok(authService.register(registerRequestDTO));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la solicitud: " + e.getMessage(), e);
        }
    }
}