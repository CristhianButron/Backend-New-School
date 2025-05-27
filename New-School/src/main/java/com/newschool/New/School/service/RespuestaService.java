package com.newschool.New.School.service;

import com.newschool.New.School.dto.respuesta.RespuestaDTO;
import com.newschool.New.School.dto.respuesta.RespuestaRequestDTO;
import com.newschool.New.School.dto.respuesta.RespuestaResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Respuestas;
import com.newschool.New.School.entity.Tareas;
import com.newschool.New.School.mapper.RespuestaMapper;
import com.newschool.New.School.repository.EstudiantesRepository;
import com.newschool.New.School.repository.RespuestaRepository;
import com.newschool.New.School.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EstudiantesRepository estudianteRepository;

    @Autowired
    private RespuestaMapper respuestaMapper;

    /**
     * Obtiene todas las respuestas
     */
    @Transactional(readOnly = true)
    public List<RespuestaDTO> findAll() {
        return respuestaRepository.findAll().stream()
                .map(respuestaMapper::toDTO)
                .toList();
    }

    /**
     * Busca una respuesta por su ID
     */
    @Transactional(readOnly = true)
    public RespuestaDTO findById(Integer id) {
        return respuestaRepository.findById(id)
                .map(respuestaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
    }

    /**
     * Busca respuestas por ID de tarea
     */
    @Transactional(readOnly = true)
    public List<RespuestaDTO> findByTareaId(Integer tareaId) {
        return respuestaRepository.findByTareaId(tareaId).stream()
                .map(respuestaMapper::toDTO)
                .toList();
    }

    /**
     * Busca respuestas por ID de estudiante
     */
    @Transactional(readOnly = true)
    public List<RespuestaDTO> findByEstudianteId(Integer estudianteId) {
        return respuestaRepository.findByEstudianteIdEstudiante(estudianteId).stream()
                .map(respuestaMapper::toDTO)
                .toList();
    }

    /**
     * Crea una nueva respuesta
     */
    @Transactional
    public RespuestaResponseDTO crear(RespuestaRequestDTO requestDTO) {
        // Validar datos de entrada
        validateRespuestaRequestDTO(requestDTO);

        // Verificar si la tarea existe
        Tareas tarea = tareaRepository.findById(requestDTO.getTareaId())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Verificar si el estudiante existe
        Estudiantes estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Verificar si ya existe una respuesta para esta tarea y estudiante
        if (!respuestaRepository.findByTareaIdAndEstudianteIdEstudiante(
                requestDTO.getTareaId(), requestDTO.getEstudianteId()).isEmpty()) {
            throw new RuntimeException("Ya existe una respuesta para esta tarea del estudiante");
        }

        // Crear y guardar la respuesta
        Respuestas respuesta = new Respuestas();
        respuesta.setRespuesta(requestDTO.getRespuesta());
        respuesta.setArchivo(requestDTO.getArchivo());
        respuesta.setFechaEntrega(LocalDate.now());
        respuesta.setTareasIdTareas(tarea);
        respuesta.setEstudiantesIdEstudiantes(estudiante);

        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toResponseDTO(respuesta);
    }

    /**
     * Actualiza una respuesta existente
     */
    @Transactional
    public RespuestaResponseDTO actualizar(Integer id, RespuestaRequestDTO requestDTO) {
        // Validar datos de entrada
        validateRespuestaRequestDTO(requestDTO);

        // Buscar la respuesta existente
        Respuestas respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        // Actualizar datos
        respuesta.setRespuesta(requestDTO.getRespuesta());
        if (requestDTO.getArchivo() != null) {
            respuesta.setArchivo(requestDTO.getArchivo());
        }
        respuesta.setFechaEntrega(LocalDate.now());

        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toResponseDTO(respuesta);
    }

    /**
     * Califica una respuesta
     */
    @Transactional
    public RespuestaResponseDTO calificar(Integer id, Double calificacion) {
        if (calificacion < 0 || calificacion > 100) {
            throw new RuntimeException("La calificación debe estar entre 0 y 100");
        }

        Respuestas respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        respuesta.setPuntaje(calificacion.intValue());
        respuesta = respuestaRepository.save(respuesta);

        return respuestaMapper.toResponseDTO(respuesta);
    }

    /**
     * Elimina una respuesta
     */
    @Transactional
    public void eliminar(Integer id) {
        if (!respuestaRepository.existsById(id)) {
            throw new RuntimeException("Respuesta no encontrada");
        }
        respuestaRepository.deleteById(id);
    }

    /**
     * Valida los datos de entrada de una respuesta
     */
    private void validateRespuestaRequestDTO(RespuestaRequestDTO requestDTO) {
        if (requestDTO.getRespuesta() == null || requestDTO.getRespuesta().trim().isEmpty()) {
            throw new RuntimeException("La respuesta no puede estar vacía");
        }

        if (requestDTO.getTareaId() == null) {
            throw new RuntimeException("El ID de la tarea es requerido");
        }

        if (requestDTO.getEstudianteId() == null) {
            throw new RuntimeException("El ID del estudiante es requerido");
        }
    }
}