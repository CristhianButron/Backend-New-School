package com.newschool.New.School.controller;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;

import com.newschool.New.School.dto.estudiante.EstudianteRequestDTO;
import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiante")
@CrossOrigin(origins = "*")
public class EstudiantesController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listaEstudiantes() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> getEstudianteById(@PathVariable Integer id) {
        EstudianteDTO estudianteDTO = estudianteService.findById(id);
        if (estudianteDTO != null) {
            return ResponseEntity.ok(estudianteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EstudianteDTO> getEstudianteByUsuarioId(@PathVariable Integer usuarioId) {
        EstudianteDTO estudianteDTO = estudianteService.findByUsuarioId(usuarioId);
        if (estudianteDTO != null) {
            return ResponseEntity.ok(estudianteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> createEstudiante(
            @RequestPart("estudiante") EstudianteRequestDTO estudianteRequestDTO,
            @RequestPart(value = "certificado", required = false) MultipartFile certificado) {
        try {
            if (certificado != null) {
                estudianteRequestDTO.setCertificadoNacimiento(certificado.getBytes());
            }
            EstudianteResponseDTO createdEstudiante = estudianteService.createEstudiante(estudianteRequestDTO);
            return new ResponseEntity<>(createdEstudiante, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(
            @PathVariable Integer id,
            @RequestPart("estudiante") EstudianteRequestDTO estudianteRequestDTO,
            @RequestPart(value = "certificado", required = false) MultipartFile certificado) {
        try {
            if (certificado != null) {
                estudianteRequestDTO.setCertificadoNacimiento(certificado.getBytes());
            }
            EstudianteResponseDTO updatedEstudiante = estudianteService.updateEstudiante(id, estudianteRequestDTO);
            if (updatedEstudiante != null) {
                return ResponseEntity.ok(updatedEstudiante);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Integer id) {
        boolean deleted = estudianteService.deleteEstudiante(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}