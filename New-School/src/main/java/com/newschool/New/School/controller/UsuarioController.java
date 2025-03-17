package com.newschool.New.School.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.service.UsuarioService;



@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/getUsuarios")
    @ResponseBody
    public List<Usuario> listaUsuarios() {
        return usuarioService.findAll();
        }

    @GetMapping("/getUsuarios/{id}")
    @ResponseBody
    public Usuario getUsuarioById(@PathVariable Integer id) {
        return usuarioService.findById(id);
    }

 

}


