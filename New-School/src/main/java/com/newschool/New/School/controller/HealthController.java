package com.newschool.New.School.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para verificar el estado de la aplicación.
 * Proporciona endpoints para herramientas de monitoreo como UptimeRobot.
 */
@RestController
public class HealthController {

    /**
     * Endpoint para verificar el estado de la aplicación.
     * Simplemente responde con un 200 OK para confirmar que la aplicación está funcionando.
     *
     * @return ResponseEntity con estado 200 OK
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    /**
     * Endpoint alternativo para verificar el estado de la aplicación.
     * Proporciona la misma funcionalidad que /health pero con una ruta alternativa.
     *
     * @return ResponseEntity con estado 200 OK
     */
    @GetMapping("/status")
    public ResponseEntity<String> statusCheck() {
        return ResponseEntity.ok("OK");
    }
}