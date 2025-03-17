package com.newschool.New.School.repository;

import org.springframework.stereotype.Repository;

import com.newschool.New.School.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


}
