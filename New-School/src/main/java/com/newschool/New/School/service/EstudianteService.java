package com.newschool.New.School.service;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.estudiante.EstudianteRequestDTO;
import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.EstudianteMapper;
import com.newschool.New.School.repository.EstudiantesRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    @Autowired
    private EstudiantesRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteMapper estudianteMapper;

    /**
     * Obtiene todos los estudiantes
     */
    public List<EstudianteDTO> findAll() {
        return estudianteRepository.findAll().stream()
                .map(estudianteMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un estudiante por su ID
     */
    public EstudianteDTO findById(Integer id) {
        return estudianteRepository.findById(id)
                .map(estudianteMapper::toDTO)
                .orElse(null);
    }

    /**
     * Busca un estudiante por el ID de usuario
     */
    public EstudianteDTO findByUsuarioId(Integer usuarioId) {
        return estudianteRepository.findByUsuarioId(usuarioId)
                .map(estudianteMapper::toDTO)
                .orElse(null);
    }

    /**
     * Crea un nuevo estudiante
     */
    @Transactional
    public EstudianteResponseDTO createEstudiante(EstudianteRequestDTO estudianteRequestDTO) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(estudianteRequestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario tenga el rol de ESTUDIANTE
        if (!"ESTUDIANTE".equalsIgnoreCase(usuario.getRol())) {
            throw new RuntimeException("El usuario no tiene el rol de ESTUDIANTE");
        }

        // Verificar que no exista un estudiante con ese ID de usuario
        if (estudianteRepository.findByUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Ya existe un estudiante con ese ID de usuario");
        }

        // Crear y guardar el estudiante
        Estudiantes estudiante = new Estudiantes();
        estudiante.setUsuarioIdUsuario(usuario);
        estudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());
        estudiante.setCertificadoNacimiento(estudianteRequestDTO.getCertificadoNacimiento());

        estudiante = estudianteRepository.save(estudiante);

        // Crear la respuesta
        return EstudianteResponseDTO.builder()
                .id(estudiante.getIdEstudiante())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .usuarioId(usuario.getIdUsuario())
                .tieneCertificado(estudiante.getCertificadoNacimiento() != null && estudiante.getCertificadoNacimiento().length > 0)
                .usuario(estudianteMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Actualiza un estudiante existente
     */
    @Transactional
    public EstudianteResponseDTO updateEstudiante(Integer id, EstudianteRequestDTO estudianteRequestDTO) {
        // Buscar el estudiante
        Estudiantes estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Actualizar datos
        estudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());

        // Actualizar certificado solo si se proporciona uno nuevo
        if (estudianteRequestDTO.getCertificadoNacimiento() != null && estudianteRequestDTO.getCertificadoNacimiento().length > 0) {
            estudiante.setCertificadoNacimiento(estudianteRequestDTO.getCertificadoNacimiento());
        }

        estudiante = estudianteRepository.save(estudiante);

        // Obtener el usuario asociado
        Usuario usuario = estudiante.getUsuarioIdUsuario();

        // Crear la respuesta
        return EstudianteResponseDTO.builder()
                .id(estudiante.getIdEstudiante())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .usuarioId(usuario.getIdUsuario())
                .tieneCertificado(estudiante.getCertificadoNacimiento() != null && estudiante.getCertificadoNacimiento().length > 0)
                .usuario(estudianteMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Elimina un estudiante
     */
    @Transactional
    public boolean deleteEstudiante(Integer id) {
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}