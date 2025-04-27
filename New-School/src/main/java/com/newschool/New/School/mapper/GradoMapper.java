package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.entity.Grados;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GradoMapper {

    public GradoDTO toDTO(Grados grados) {
        return GradoDTO.builder()
                .id(grados.getId())
                .descripcion(grados.getDescripcion())
                .primariaSencundaria(grados.getPrimaria_secundaria())
                .build();
    }

    public List<GradoDTO> toDTOList(List<Grados> gradosList) {
        return gradosList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Grados toEntity(GradoDTO gradoDTO) {
        Grados grados = new Grados();
        grados.setId(gradoDTO.getId());
        grados.setDescripcion(gradoDTO.getDescripcion());
        grados.setPrimaria_secundaria(gradoDTO.getPrimariaSencundaria());
        return grados;
    }

    public Grados updateEntity(Grados grados, GradoDTO gradoDTO) {
        grados.setDescripcion(gradoDTO.getDescripcion());
        grados.setPrimaria_secundaria(gradoDTO.getPrimariaSencundaria());
        return grados;
    }
}