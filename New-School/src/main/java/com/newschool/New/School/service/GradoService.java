package com.newschool.New.School.service;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.exception.BadRequestException;
import com.newschool.New.School.exception.ResourceNotFoundException;
import com.newschool.New.School.exception.ValidationException;
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
                .orElseThrow(() -> ResourceNotFoundException.gradoNotFound(id));
    }

    @Transactional(readOnly = true)
    public GradoDTO findByDescripcion(String descripcion) {
        return gradoRepository.findByDescripcion(descripcion)
                .map(gradoMapper::toDTO)
                .orElseThrow(() -> ResourceNotFoundException.gradoNotFoundByDescripcion(descripcion));
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
            gradoRepository.findByDescripcion(gradoDTO.getDescripcion())
                    .ifPresent(g -> {
                        throw BadRequestException.gradoDuplicado(gradoDTO.getDescripcion());
                    });
        }

        Grados grados = gradoMapper.toEntity(gradoDTO);
        Grados savedGrados = gradoRepository.save(grados);
        return gradoMapper.toDTO(savedGrados);
    }

    @Transactional
    public GradoDTO update(Integer id, GradoDTO gradoDTO) {
        validateGradoDTO(gradoDTO);

        Grados existingGrado = gradoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.gradoNotFound(id));

        // Verificar si ya existe otro grado con la misma descripción (que no sea el actual)
        if (gradoDTO.getDescripcion() != null) {
            gradoRepository.findByDescripcion(gradoDTO.getDescripcion())
                    .ifPresent(g -> {
                        if (!g.getId().equals(id)) {
                            throw BadRequestException.gradoDuplicado(gradoDTO.getDescripcion());
                        }
                    });
        }

        Grados updatedGrados = gradoMapper.updateEntity(existingGrado, gradoDTO);
        return gradoMapper.toDTO(gradoRepository.save(updatedGrados));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!gradoRepository.existsById(id)) {
            throw ResourceNotFoundException.gradoNotFound(id);
        }

        // Aquí podrías verificar si el grado tiene alumnos asociados antes de eliminarlo
        // Por ejemplo:
        // if (alumnoRepository.existsByGradoId(id)) {
        //     throw BadRequestException.gradoConAlumnosActivos(id);
        // }

        gradoRepository.deleteById(id);
    }

    private void validateGradoDTO(GradoDTO gradoDTO) {
        ValidationException validationException = ValidationException.createForGrado();

        if (gradoDTO.getDescripcion() == null || gradoDTO.getDescripcion().trim().isEmpty()) {
            validationException.addError("descripcion", "La descripción no puede estar vacía");
        } else if (gradoDTO.getDescripcion().length() < 3) {
            validationException.addError("descripcion", "La descripción debe tener al menos 3 caracteres");
        } else if (gradoDTO.getDescripcion().length() > 100) {
            validationException.addError("descripcion", "La descripción no puede exceder los 100 caracteres");
        }

        if (gradoDTO.getPrimariaSencundaria() == null) {
            validationException.addError("primariaSencundaria", "Debe especificar si es primaria o secundaria");
        }

        if (!validationException.getErrors().isEmpty()) {
            throw validationException;
        }
    }
}