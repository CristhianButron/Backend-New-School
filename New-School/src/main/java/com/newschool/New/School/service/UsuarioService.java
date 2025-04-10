package com.newschool.New.School.service;

import java.util.List;
import java.util.Optional;

import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.dto.UsuarioRequestDTO;
import com.newschool.New.School.dto.UsuarioResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioDTO> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toDTOList(usuarios);
    }

    public UsuarioDTO findById(Integer id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        return usuarioOptional.map(usuarioMapper::toDTO).orElse(null);
    }

    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO updateUsuario(Integer id, UsuarioRequestDTO usuarioRequestDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario = usuarioMapper.updateEntity(usuario, usuarioRequestDTO);
            usuario = usuarioRepository.save(usuario);
            return usuarioMapper.toResponseDTO(usuario);
        }

        return null;
    }

    public boolean deleteUsuario(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UsuarioDTO findByEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        return usuarioOptional.map(usuarioMapper::toDTO).orElse(null);
    }
}