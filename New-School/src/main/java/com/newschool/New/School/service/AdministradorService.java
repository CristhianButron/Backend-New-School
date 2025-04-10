package com.newschool.New.School.service;

import com.newschool.New.School.dto.administrador.AdministradorDTO;
import com.newschool.New.School.dto.administrador.AdministradorRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorResponseDTO;
import com.newschool.New.School.entity.Administrativos;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.AdministradorMapper;
import com.newschool.New.School.repository.AdministrativosRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    @Autowired
    private AdministrativosRepository administrativosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    /**
     * Obtiene todos los administradores
     */
    public List<AdministradorDTO> findAll() {
        return administrativosRepository.findAll().stream()
                .map(administradorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un administrador por su ID
     */
    public AdministradorDTO findById(Integer id) {
        return administrativosRepository.findById(id)
                .map(administradorMapper::toDTO)
                .orElse(null);
    }

    /**
     * Busca un administrador por el ID de usuario
     */
    public AdministradorDTO findByUsuarioId(Integer usuarioId) {
        return administrativosRepository.findByUsuarioId(usuarioId)
                .map(administradorMapper::toDTO)
                .orElse(null);
    }

    /**
     * Crea un nuevo administrador
     */
    @Transactional
    public AdministradorResponseDTO createAdministrador(AdministradorRequestDTO administradorRequestDTO) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(administradorRequestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario tenga el rol de ADMIN
        if (!"ADMIN".equalsIgnoreCase(usuario.getRol())) {
            throw new RuntimeException("El usuario no tiene el rol de ADMIN");
        }

        // Verificar que no exista un administrador con ese ID de usuario
        if (administrativosRepository.findByUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Ya existe un administrador con ese ID de usuario");
        }

        // Crear y guardar el administrador
        Administrativos administrativo = new Administrativos();
        administrativo.setUsuarioIdUsuario(usuario);
        administrativo.setCargo(administradorRequestDTO.getCargo());

        administrativo = administrativosRepository.save(administrativo);

        // Crear la respuesta
        return AdministradorResponseDTO.builder()
                .id(administrativo.getIdAdministrativo())
                .cargo(administrativo.getCargo())
                .usuarioId(usuario.getIdUsuario())
                .usuario(administradorMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Actualiza un administrador existente
     */
    @Transactional
    public AdministradorResponseDTO updateAdministrador(Integer id, AdministradorRequestDTO administradorRequestDTO) {
        // Buscar el administrador
        Administrativos administrativo = administrativosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        // Actualizar datos
        administrativo.setCargo(administradorRequestDTO.getCargo());

        administrativo = administrativosRepository.save(administrativo);

        // Obtener el usuario asociado
        Usuario usuario = administrativo.getUsuarioIdUsuario();

        // Crear la respuesta
        return AdministradorResponseDTO.builder()
                .id(administrativo.getIdAdministrativo())
                .cargo(administrativo.getCargo())
                .usuarioId(usuario.getIdUsuario())
                .usuario(administradorMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Elimina un administrador
     */
    @Transactional
    public boolean deleteAdministrador(Integer id) {
        if (administrativosRepository.existsById(id)) {
            administrativosRepository.deleteById(id);
            return true;
        }
        return false;
    }
}