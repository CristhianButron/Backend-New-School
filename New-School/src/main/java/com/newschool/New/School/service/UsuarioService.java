package com.newschool.New.School.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.repository.UsuarioRepository;



@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    public Usuario findById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario;
    }

}
