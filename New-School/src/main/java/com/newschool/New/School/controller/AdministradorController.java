package com.newschool.New.School.controller;

import com.newschool.New.School.dto.administrador.AdministradorDTO;
import com.newschool.New.School.dto.administrador.AdministradorRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorResponseDTO;
import com.newschool.New.School.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

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
}