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

    @Transactional(readOnly = true)
    public List<RespuestaDTO> findAll() {
        return respuestaMapper.toDTOList(respuestaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public RespuestaDTO findById(Integer id) {
        return respuestaRepository.findById(id)
                .map(respuestaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<RespuestaDTO> findByTareaId(Integer tareaId) {
        return respuestaMapper.toDTOList(respuestaRepository.findByTareaId(tareaId));
    }

    @Transactional(readOnly = true)
    public List<RespuestaDTO> findByEstudianteId(Integer estudianteId) {
        return respuestaMapper.toDTOList(respuestaRepository.findByEstudianteIdEstudiante(estudianteId));
    }

    @Transactional
    public RespuestaResponseDTO crear(RespuestaRequestDTO requestDTO) {
        Tareas tarea = tareaRepository.findById(requestDTO.getTareaId())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        Estudiantes estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Verificar si ya existe una respuesta para esta tarea y estudiante
        if (!respuestaRepository.findByTareaIdAndEstudianteIdEstudiante(
                requestDTO.getTareaId(), requestDTO.getEstudianteId()).isEmpty()) {
            throw new RuntimeException("Ya existe una respuesta para esta tarea del estudiante");
        }

        Respuestas respuesta = new Respuestas();
        respuesta.setContenido(requestDTO.getContenido());
        respuesta.setTarea(tarea);
        respuesta.setEstudiante(estudiante);
        respuesta.setArchivo(requestDTO.getArchivo());

        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toResponseDTO(respuesta);
    }

    @Transactional
    public RespuestaResponseDTO actualizar(Integer id, RespuestaRequestDTO requestDTO) {
        Respuestas respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        if (requestDTO.getContenido() != null) {
            respuesta.setContenido(requestDTO.getContenido());
        }

        if (requestDTO.getArchivo() != null) {
            respuesta.setArchivo(requestDTO.getArchivo());
        }

        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toResponseDTO(respuesta);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!respuestaRepository.existsById(id)) {
            throw new RuntimeException("Respuesta no encontrada");
        }
        respuestaRepository.deleteById(id);
    }
}