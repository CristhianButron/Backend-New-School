package com.newschool.New.School.controller;


import com.newschool.New.School.dto.AuthRequestDTO;
import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "API para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema con su rol y datos específicos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para el registro del usuario",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Registro de Docente",
                                    summary = "Datos para registrar un usuario con rol DOCENTE",
                                    value = "{\n" +
                                            "  \"ci\": \"12345678\",\n" +
                                            "  \"nombre\": \"Juan\",\n" +
                                            "  \"apellido\": \"Pérez\",\n" +
                                            "  \"email\": \"juan.perez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"rol\": \"DOCENTE\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"licenciatura\": \"Ingeniería en Sistemas\"\n" +
                                            "  }\n" +
                                            "}"
                            ),
                            @ExampleObject(
                                    name = "Registro de Estudiante",
                                    summary = "Datos para registrar un usuario con rol ESTUDIANTE",
                                    value = "{\n" +
                                            "  \"ci\": \"87654321\",\n" +
                                            "  \"nombre\": \"Maria\",\n" +
                                            "  \"apellido\": \"González\",\n" +
                                            "  \"email\": \"maria.gonzalez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"rol\": \"ESTUDIANTE\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"fechaNacimiento\": \"2000-01-15\"\n" +
                                            "  }\n" +
                                            "}"
                            ),
                            @ExampleObject(
                                    name = "Registro de Administrador",
                                    summary = "Datos para registrar un usuario con rol ADMIN",
                                    value = "{\n" +
                                            "  \"ci\": \"98765432\",\n" +
                                            "  \"nombre\": \"Carlos\",\n" +
                                            "  \"apellido\": \"Ramírez\",\n" +
                                            "  \"email\": \"carlos.ramirez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"rol\": \"ADMIN\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"cargo\": \"Director Académico\"\n" +
                                            "  }\n" +
                                            "}"
                            ),
                            @ExampleObject(
                                    name = "Registro de Padre",
                                    summary = "Datos para registrar un usuario con rol PADRE",
                                    value = "{\n" +
                                            "  \"ci\": \"45678912\",\n" +
                                            "  \"nombre\": \"Roberto\",\n" +
                                            "  \"apellido\": \"Gómez\",\n" +
                                            "  \"email\": \"roberto.gomez@example.com\",\n" +
                                            "  \"password\": \"contraseña123\",\n" +
                                            "  \"rol\": \"PADRE\",\n" +
                                            "  \"datosEspecificos\": {\n" +
                                            "    \"parentesco\": \"Padre\",\n" +
                                            "    \"estudianteId\": \"1\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales de autenticación",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Credenciales de autenticación",
                                    value = "{\n" +
                                            "  \"email\": \"usuario@example.com\",\n" +
                                            "  \"password\": \"contraseña123\"\n" +
                                            "}"
                            )
                    }
            )
    )
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}