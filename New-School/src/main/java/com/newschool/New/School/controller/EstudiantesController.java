package com.newschool.New.School.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.service.EstudiantesService;

@RestController
@RequestMapping("/api/v1/estudiantes")
@CrossOrigin(origins = "*")
public class EstudiantesController {

    @Autowired
    EstudiantesService estudiantesService;



    @GetMapping("/getEstudiantes/{id}")
    @ResponseBody
    public Estudiantes getEstudianteById(@PathVariable Integer id) {
        return estudiantesService.findById(id);
    }
    
    @PostMapping("/postEstudiantes")
    @ResponseBody
    public ResponseEntity<Estudiantes> saveEstudiante(@RequestBody Estudiantes estudiantesDto) {
        Estudiantes savedEstudianteDto = estudiantesService.save(estudiantesDto);
        return ResponseEntity.ok(savedEstudianteDto);
    }

    @GetMapping("/getEstudiantes")
    @ResponseBody
    public List<Estudiantes> getEstudiantes() {
        return estudiantesService.findAll();
    }

    @DeleteMapping("/deleteEstudiantes/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteEstudiante(@PathVariable Integer id) {
        estudiantesService.deleteById(id);
        return ResponseEntity.ok("Estudiante eliminado");
    }



}

