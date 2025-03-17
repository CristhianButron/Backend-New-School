package com.newschool.New.School.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newschool.New.School.entity.Estudiantes;

@Repository
public interface EstudiantesRepository extends JpaRepository<Estudiantes, Integer> {

}
