package com.newschool.New.School.service;

import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.dto.UsuarioRequestDTO;
import com.newschool.New.School.dto.UsuarioResponseDTO;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    // Usar una instancia real del mapper en lugar de un mock o spy
    private UsuarioMapper usuarioMapper = new UsuarioMapper();

    // Inyectar manualmente el service en setUp()
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        // Inicializar el servicio manualmente con el repositorio mock y el mapper real
        usuarioService = new UsuarioService(usuarioRepository, usuarioMapper);

        // Configurar objetos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCi("1234567890");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setPassword("password123");
        usuario.setRol("ESTUDIANTE");

        usuarioRequestDTO = UsuarioRequestDTO.builder()
                .ci("1234567890")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .password("password123")
                .rol("ESTUDIANTE")
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> result = usuarioService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Pérez", result.get(0).getApellido());
        verify(usuarioRepository).findAll();
    }

    @Test
    void findById_WhenUserExists() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getNombre());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void findById_WhenUserDoesNotExist() {
        // Arrange
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        UsuarioDTO result = usuarioService.findById(99);

        // Assert
        assertNull(result);
        verify(usuarioRepository).findById(99);
    }

    @Test
    void createUsuario() {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario savedUsuario = invocation.getArgument(0);
            savedUsuario.setIdUsuario(1); // Simular generación de ID
            return savedUsuario;
        });

        // Act
        UsuarioResponseDTO result = usuarioService.createUsuario(usuarioRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("1234567890", result.getCi());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void updateUsuario_WhenUserExists() {
        // Arrange
        UsuarioRequestDTO updateRequest = UsuarioRequestDTO.builder()
                .ci("9876543210")
                .nombre("Juan Actualizado")
                .apellido("Pérez Actualizado")
                .email("juan.actualizado@example.com")
                .password("newPassword123")
                .rol("DOCENTE")
                .build();

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UsuarioResponseDTO result = usuarioService.updateUsuario(1, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan Actualizado", result.getNombre());
        assertEquals("9876543210", result.getCi());
        assertEquals("DOCENTE", result.getRol());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void updateUsuario_WhenUserDoesNotExist() {
        // Arrange
        UsuarioRequestDTO updateRequest = UsuarioRequestDTO.builder()
                .ci("9876543210")
                .nombre("Juan Actualizado")
                .apellido("Pérez Actualizado")
                .email("juan.actualizado@example.com")
                .password("newPassword123")
                .rol("DOCENTE")
                .build();

        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        UsuarioResponseDTO result = usuarioService.updateUsuario(99, updateRequest);

        // Assert
        assertNull(result);
        verify(usuarioRepository).findById(99);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deleteUsuario_WhenUserExists() {
        // Arrange
        when(usuarioRepository.existsById(1)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1);

        // Act
        boolean result = usuarioService.deleteUsuario(1);

        // Assert
        assertTrue(result);
        verify(usuarioRepository).existsById(1);
        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void deleteUsuario_WhenUserDoesNotExist() {
        // Arrange
        when(usuarioRepository.existsById(99)).thenReturn(false);

        // Act
        boolean result = usuarioService.deleteUsuario(99);

        // Assert
        assertFalse(result);
        verify(usuarioRepository).existsById(99);
        verify(usuarioRepository, never()).deleteById(anyInt());
    }

    @Test
    void findByEmail_WhenUserExists() {
        // Arrange
        when(usuarioRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.findByEmail("juan.perez@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("juan.perez@example.com", result.getEmail());
        verify(usuarioRepository).findByEmail("juan.perez@example.com");
    }

    @Test
    void findByEmail_WhenUserDoesNotExist() {
        // Arrange
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // Act
        UsuarioDTO result = usuarioService.findByEmail("noexiste@example.com");

        // Assert
        assertNull(result);
        verify(usuarioRepository).findByEmail("noexiste@example.com");
    }
}