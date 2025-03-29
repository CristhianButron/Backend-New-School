package com.newschool.New.School.controller;


import com.newschool.New.School.dto.AuthRequestDTO;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.repository.UsuarioRepository;
import com.newschool.New.School.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*")
public class TestAuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint para verificar si un usuario existe y ver su hash de contraseña
    @GetMapping("/usuario")
    public ResponseEntity<Map<String, String>> testUsuario(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                response.put("status", "success");
                response.put("message", "Usuario encontrado");
                response.put("email", usuario.getEmail());
                response.put("nombre", usuario.getNombre());
                response.put("rol", usuario.getRol());
                response.put("passwordHash", usuario.getPassword());
            } else {
                response.put("status", "error");
                response.put("message", "Usuario no encontrado con email: " + email);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al buscar usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Endpoint para probar la verificación de contraseña
    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> testPassword(@RequestBody AuthRequestDTO request) {
        Map<String, String> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

            if (!usuarioOpt.isPresent()) {
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(404).body(response);
            }

            Usuario usuario = usuarioOpt.get();
            boolean matches = passwordEncoder.matches(request.getPassword(), usuario.getPassword());

            response.put("status", matches ? "success" : "error");
            response.put("message", matches ? "Contraseña correcta" : "Contraseña incorrecta");
            response.put("inputPassword", request.getPassword());
            response.put("storedPasswordHash", usuario.getPassword());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al verificar contraseña: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Endpoint para probar el proceso de autenticación completo
    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> testAuth(@RequestBody AuthRequestDTO request) {
        Map<String, String> response = new HashMap<>();

        try {
            // Paso 1: Buscar el usuario
            response.put("step1", "Buscando usuario...");
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

            if (!usuarioOpt.isPresent()) {
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(404).body(response);
            }

            Usuario usuario = usuarioOpt.get();
            response.put("step1_result", "Usuario encontrado: " + usuario.getEmail());

            // Paso 2: Verificar la contraseña manualmente
            response.put("step2", "Verificando contraseña...");
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), usuario.getPassword());

            if (!passwordMatches) {
                response.put("status", "error");
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.status(401).body(response);
            }

            response.put("step2_result", "Contraseña correcta");

            // Paso 3: Intentar autenticar con AuthenticationManager
            try {
                response.put("step3", "Intentando autenticar con AuthenticationManager...");
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
                response.put("step3_result", "Autenticación exitosa con AuthenticationManager");
            } catch (Exception e) {
                response.put("step3_result", "Error con AuthenticationManager: " + e.getMessage());
                e.printStackTrace();
            }

            // Paso 4: Generar token JWT
            response.put("step4", "Generando token JWT...");
            UserDetails userDetails = new User(
                    usuario.getEmail(),
                    usuario.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()))
            );

            String token = jwtUtil.generateToken(userDetails);
            response.put("step4_result", "Token generado correctamente");
            response.put("token", token);

            // Respuesta final
            response.put("status", "success");
            response.put("message", "Proceso de autenticación completado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error en el proceso de autenticación: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(response);
        }
    }
}