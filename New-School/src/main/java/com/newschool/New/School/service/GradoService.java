package com.newschool.New.School.service;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.mapper.GradoMapper;
import com.newschool.New.School.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;
    private final GradoMapper gradoMapper;

    @Autowired
    public GradoService(GradoRepository gradoRepository, GradoMapper gradoMapper) {
        this.gradoRepository = gradoRepository;
        this.gradoMapper = gradoMapper;
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> findAll() {
        return gradoMapper.toDTOList(gradoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public GradoDTO findById(Integer id) {
        return gradoRepository.findById(id)
                .map(gradoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    @Transactional(readOnly = true)
    public GradoDTO findByDescripcion(String descripcion) {
        return gradoRepository.findByDescripcion(descripcion)
                .map(gradoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Grado con descripción '" + descripcion + "' no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> findByPrimariaSencundaria(Boolean esPrimaria) {
        return gradoMapper.toDTOList(gradoRepository.findByNivel(esPrimaria));
    }

    @Transactional
    public GradoDTO save(GradoDTO gradoDTO) {
        validateGradoDTO(gradoDTO);

        // Verificar si ya existe un grado con la misma descripción
        if (gradoDTO.getDescripcion() != null) {
            if (gradoRepository.findByDescripcion(gradoDTO.getDescripcion()).isPresent()) {
                throw new RuntimeException("Ya existe un grado con la descripción: " + gradoDTO.getDescripcion());
            }
        }

        Grados grados = gradoMapper.toEntity(gradoDTO);
        Grados savedGrados = gradoRepository.save(grados);
        return gradoMapper.toDTO(savedGrados);
    }

    @Transactional
    public GradoDTO update(Integer id, GradoDTO gradoDTO) {
        validateGradoDTO(gradoDTO);

        // Verificar si el grado existe
        Grados existingGrado = gradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        // Verificar si ya existe otro grado con la misma descripción (que no sea el actual)
        if (gradoDTO.getDescripcion() != null) {
            var existingWithSameDesc = gradoRepository.findByDescripcion(gradoDTO.getDescripcion());
            if (existingWithSameDesc.isPresent() && !existingWithSameDesc.get().getId().equals(id)) {
                throw new RuntimeException("Ya existe otro grado con la descripción: " + gradoDTO.getDescripcion());
            }
        }

        Grados updatedGrados = gradoMapper.updateEntity(existingGrado, gradoDTO);
        return gradoMapper.toDTO(gradoRepository.save(updatedGrados));
    }

    @Transactional
    public void deleteById(Integer id) {
        // Verificar si el grado existe
        if (!gradoRepository.existsById(id)) {
            throw new RuntimeException("Grado no encontrado");
        }

        // Aquí podrías verificar si el grado tiene alumnos o inscripciones asociadas antes de eliminarlo
        // Por ejemplo:
        // if (inscripcionGradoRepository.existsByGradoId(id)) {
        //     throw new RuntimeException("No se puede eliminar el grado porque tiene inscripciones asociadas");
        // }

        gradoRepository.deleteById(id);
    }

    private void validateGradoDTO(GradoDTO gradoDTO) {
        if (gradoDTO.getDescripcion() == null || gradoDTO.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción del grado no puede estar vacía");
        }

        if (gradoDTO.getDescripcion().length() < 3) {
            throw new RuntimeException("La descripción del grado debe tener al menos 3 caracteres");
        }

        if (gradoDTO.getDescripcion().length() > 100) {
            throw new RuntimeException("La descripción del grado no puede exceder los 100 caracteres");
        }

        if (gradoDTO.getPrimariaSencundaria() == null) {
            throw new RuntimeException("Debe especificar si el grado es de primaria o secundaria");
        }
    }
}