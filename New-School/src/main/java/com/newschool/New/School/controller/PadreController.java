package com.newschool.New.School.controller;

import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.padre.PadreRequestDTO;
import com.newschool.New.School.dto.padre.PadreResponseDTO;
import com.newschool.New.School.service.PadreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/padres")
@CrossOrigin(origins = "*")
public class PadreController {

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
}