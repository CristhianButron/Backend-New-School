package com.newschool.New.School.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.repository.EstudiantesRepository;



@Service
public class EstudiantesService {
    @Autowired
    private final EstudiantesRepository estudiantesRepository;


    public EstudiantesService(EstudiantesRepository estudiantesRepository) {
        this.estudiantesRepository = estudiantesRepository;
       
    }


    public Estudiantes findById(Integer id) {
        Estudiantes estudiante = estudiantesRepository.findById(id).orElse(null);
        return estudiante;
    }

    public Estudiantes save(Estudiantes estudiante) {
       
        estudiante = estudiantesRepository.save(estudiante);

        return estudiante;
    }


    public List<Estudiantes> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    


}
