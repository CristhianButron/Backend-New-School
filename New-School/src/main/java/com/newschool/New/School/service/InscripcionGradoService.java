package com.newschool.New.School.service;

import com.newschool.New.School.dto.inscripcion.InscripcionGradoDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoRequestDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.entity.Inscripcion_grados;
import com.newschool.New.School.mapper.InscripcionGradoMapper;
import com.newschool.New.School.repository.EstudiantesRepository;
import com.newschool.New.School.repository.GradoRepository;
import com.newschool.New.School.repository.InscripcionGradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InscripcionGradoService {

    private final InscripcionGradoRepository inscripcionRepository;
    private final EstudiantesRepository estudianteRepository;
    private final GradoRepository gradoRepository;
    private final InscripcionGradoMapper mapper;

    @Autowired
    public InscripcionGradoService(
            InscripcionGradoRepository inscripcionRepository,
            EstudiantesRepository estudianteRepository,
            GradoRepository gradoRepository,
            InscripcionGradoMapper mapper
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.gradoRepository = gradoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public InscripcionGradoResponseDTO crear(InscripcionGradoRequestDTO requestDTO) {
        // Verificar si el estudiante existe
        Estudiantes estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Verificar si el grado existe
        Grados grado = gradoRepository.findById(requestDTO.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        // Verificar si ya existe una inscripción para este estudiante en esta gestión
        if (inscripcionRepository.existsByEstudianteIdEstudianteAndGestion(
                requestDTO.getEstudianteId(), requestDTO.getGestion())) {
            throw new RuntimeException("El estudiante ya está inscrito en esta gestión");
        }

        // Crear la inscripción
        Inscripcion_grados inscripcion = mapper.toEntity(requestDTO, estudiante, grado);
        Inscripcion_grados guardada = inscripcionRepository.save(inscripcion);

        return mapper.toResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<InscripcionGradoDTO> obtenerPorEstudiante(Integer estudianteId) {
        List<Inscripcion_grados> inscripciones = inscripcionRepository.findByEstudianteIdEstudiante(estudianteId);
        return mapper.toDTOList(inscripciones);
    }

    @Transactional(readOnly = true)
    public List<InscripcionGradoDTO> obtenerPorGrado(Integer gradoId) {
        List<Inscripcion_grados> inscripciones = inscripcionRepository.findByGradoId(gradoId);
        return mapper.toDTOList(inscripciones);
    }

    @Transactional(readOnly = true)
    public List<InscripcionGradoDTO> obtenerPorGestion(Integer gestion) {
        List<Inscripcion_grados> inscripciones = inscripcionRepository.findByGestion(gestion);
        return mapper.toDTOList(inscripciones);
    }

    @Transactional
    public InscripcionGradoResponseDTO actualizar(Integer id, InscripcionGradoRequestDTO requestDTO) {
        // Buscar la inscripción existente
        Inscripcion_grados inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        // Buscar estudiante si se proporciona
        Estudiantes estudiante = requestDTO.getEstudianteId() != null ?
                estudianteRepository.findById(requestDTO.getEstudianteId()).orElse(null) : null;

        // Buscar grado si se proporciona
        Grados grado = requestDTO.getGradoId() != null ?
                gradoRepository.findById(requestDTO.getGradoId()).orElse(null) : null;

        // Actualizar la inscripción
        mapper.updateEntity(inscripcion, requestDTO, estudiante, grado);
        Inscripcion_grados guardada = inscripcionRepository.save(inscripcion);

        return mapper.toResponseDTO(guardada);
    }

    @Transactional
    public void eliminar(Integer id) {
        // Verificar si la inscripción existe
        if (!inscripcionRepository.existsById(id)) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        inscripcionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<InscripcionGradoDTO> obtenerPorId(Integer id) {
        return inscripcionRepository.findById(id)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<InscripcionGradoDTO> obtenerTodos() {
        List<Inscripcion_grados> inscripciones = inscripcionRepository.findAll();
        return mapper.toDTOList(inscripciones);
    }
}