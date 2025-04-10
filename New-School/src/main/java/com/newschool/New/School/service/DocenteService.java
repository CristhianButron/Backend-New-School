package com.newschool.New.School.service;


import com.newschool.New.School.dto.docente.DocenteDTO;
import com.newschool.New.School.dto.docente.DocenteRequestDTO;
import com.newschool.New.School.dto.docente.DocenteResponseDTO;
import com.newschool.New.School.entity.Docentes;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.DocenteMapper;
import com.newschool.New.School.repository.DocentesRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteService {

    @Autowired
    private DocentesRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocenteMapper docenteMapper;

    /**
     * Obtiene todos los docentes
     */
    public List<DocenteDTO> findAll() {
        return docenteRepository.findAll().stream()
                .map(docenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un docente por su ID
     */
    public DocenteDTO findById(Integer id) {
        return docenteRepository.findById(id)
                .map(docenteMapper::toDTO)
                .orElse(null);
    }

    /**
     * Busca un docente por el ID de usuario
     */
    public DocenteDTO findByUsuarioId(Integer usuarioId) {
        return docenteRepository.findByUsuarioId(usuarioId)
                .map(docenteMapper::toDTO)
                .orElse(null);
    }

    /**
     * Crea un nuevo docente
     */
    @Transactional
    public DocenteResponseDTO createDocente(DocenteRequestDTO docenteRequestDTO) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(docenteRequestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario tenga el rol de DOCENTE
        if (!"DOCENTE".equalsIgnoreCase(usuario.getRol())) {
            throw new RuntimeException("El usuario no tiene el rol de DOCENTE");
        }

        // Verificar que no exista un docente con ese ID de usuario
        if (docenteRepository.findByUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Ya existe un docente con ese ID de usuario");
        }

        // Crear y guardar el docente
        Docentes docente = new Docentes();
        docente.setUsuarioIdUsuario(usuario);
        docente.setLicenciatura(docenteRequestDTO.getLicenciatura());
        docente.setTitulo(docenteRequestDTO.getTitulo());

        docente = docenteRepository.save(docente);

        // Crear la respuesta
        return DocenteResponseDTO.builder()
                .id(docente.getIdDocente())
                .licenciatura(docente.getLicenciatura())
                .usuarioId(usuario.getIdUsuario())
                .tieneTitulo(docente.getTitulo() != null && docente.getTitulo().length > 0)
                .usuario(docenteMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Actualiza un docente existente
     */
    @Transactional
    public DocenteResponseDTO updateDocente(Integer id, DocenteRequestDTO docenteRequestDTO) {
        // Buscar el docente
        Docentes docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        // Actualizar datos
        docente.setLicenciatura(docenteRequestDTO.getLicenciatura());

        // Actualizar título solo si se proporciona uno nuevo
        if (docenteRequestDTO.getTitulo() != null && docenteRequestDTO.getTitulo().length > 0) {
            docente.setTitulo(docenteRequestDTO.getTitulo());
        }

        docente = docenteRepository.save(docente);

        // Obtener el usuario asociado
        Usuario usuario = docente.getUsuarioIdUsuario();

        // Crear la respuesta
        return DocenteResponseDTO.builder()
                .id(docente.getIdDocente())
                .licenciatura(docente.getLicenciatura())
                .usuarioId(usuario.getIdUsuario())
                .tieneTitulo(docente.getTitulo() != null && docente.getTitulo().length > 0)
                .usuario(docenteMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Elimina un docente
     */
    @Transactional
    public boolean deleteDocente(Integer id) {
        if (docenteRepository.existsById(id)) {
            docenteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}