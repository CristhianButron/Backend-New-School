package com.newschool.New.School.service;

import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.dto.administrador.AdministradorDTO;
import com.newschool.New.School.dto.administrador.AdministradorRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorResponseDTO;
import com.newschool.New.School.entity.Administrativos;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.AdministradorMapper;
import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.AdministrativosRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    @Mock
    private AdministrativosRepository administrativosRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    // Crear instancias reales de los mappers
    private UsuarioMapper usuarioMapper = new UsuarioMapper();
    private AdministradorMapper administradorMapper = new AdministradorMapper();

    private AdministradorService administradorService;

    private Usuario usuario;
    private Administrativos administrativo;
    private AdministradorRequestDTO administradorRequestDTO;

    @BeforeEach
    void setUp() {
        // Configurar el UsuarioMapper en el AdministradorMapper
        ReflectionTestUtils.setField(administradorMapper, "usuarioMapper", usuarioMapper);

        // Configurar el servicio manualmente
        administradorService = new AdministradorService();
        ReflectionTestUtils.setField(administradorService, "administrativosRepository", administrativosRepository);
        ReflectionTestUtils.setField(administradorService, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(administradorService, "administradorMapper", administradorMapper);

        // Configurar objetos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCi("1234567890");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setPassword("password123");
        usuario.setRol("ADMIN");

        administrativo = new Administrativos();
        administrativo.setIdAdministrativo(1);
        administrativo.setCargo("Director");
        administrativo.setUsuarioIdUsuario(usuario);

        administradorRequestDTO = AdministradorRequestDTO.builder()
                .cargo("Director")
                .usuarioId(1)
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        List<Administrativos> administrativosList = Arrays.asList(administrativo);
        when(administrativosRepository.findAll()).thenReturn(administrativosList);

        // Act
        List<AdministradorDTO> result = administradorService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Director", result.get(0).getCargo());
        assertEquals(1, result.get(0).getUsuarioId());
        assertNotNull(result.get(0).getUsuario());
        assertEquals("Juan", result.get(0).getUsuario().getNombre());
        verify(administrativosRepository).findAll();
    }

    @Test
    void findById() {
        // Arrange - Caso cuando existe
        when(administrativosRepository.findById(1)).thenReturn(Optional.of(administrativo));

        // Act
        AdministradorDTO result = administradorService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Director", result.getCargo());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Juan", result.getUsuario().getNombre());
        verify(administrativosRepository).findById(1);

        // Arrange - Caso cuando no existe
        when(administrativosRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        AdministradorDTO notFoundResult = administradorService.findById(99);

        // Assert
        assertNull(notFoundResult);
        verify(administrativosRepository).findById(99);
    }

    @Test
    void findByUsuarioId() {
        // Arrange - Caso cuando existe
        when(administrativosRepository.findByUsuarioId(1)).thenReturn(Optional.of(administrativo));

        // Act
        AdministradorDTO result = administradorService.findByUsuarioId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Director", result.getCargo());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Juan", result.getUsuario().getNombre());
        verify(administrativosRepository).findByUsuarioId(1);

        // Arrange - Caso cuando no existe
        when(administrativosRepository.findByUsuarioId(99)).thenReturn(Optional.empty());

        // Act
        AdministradorDTO notFoundResult = administradorService.findByUsuarioId(99);

        // Assert
        assertNull(notFoundResult);
        verify(administrativosRepository).findByUsuarioId(99);
    }

    @Test
    void createAdministrador_Success() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(administrativosRepository.findByUsuarioId(1)).thenReturn(Optional.empty());

        when(administrativosRepository.save(any(Administrativos.class))).thenAnswer(invocation -> {
            Administrativos savedAdmin = invocation.getArgument(0);
            savedAdmin.setIdAdministrativo(1);
            return savedAdmin;
        });

        // Act
        AdministradorResponseDTO result = administradorService.createAdministrador(administradorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Director", result.getCargo());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Juan", result.getUsuario().getNombre());
        verify(usuarioRepository).findById(1);
        verify(administrativosRepository).findByUsuarioId(1);
        verify(administrativosRepository).save(any(Administrativos.class));
    }

    @Test
    void createAdministrador_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            administradorService.createAdministrador(
                    AdministradorRequestDTO.builder()
                            .cargo("Director")
                            .usuarioId(99)
                            .build()
            );
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(99);
        verify(administrativosRepository, never()).save(any(Administrativos.class));
    }

    @Test
    void createAdministrador_WrongRole() {
        // Arrange
        Usuario usuarioEstudiante = new Usuario();
        usuarioEstudiante.setIdUsuario(2);
        usuarioEstudiante.setCi("9876543210");
        usuarioEstudiante.setNombre("Pedro");
        usuarioEstudiante.setApellido("Gómez");
        usuarioEstudiante.setEmail("pedro.gomez@example.com");
        usuarioEstudiante.setPassword("password456");
        usuarioEstudiante.setRol("ESTUDIANTE");

        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuarioEstudiante));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            administradorService.createAdministrador(
                    AdministradorRequestDTO.builder()
                            .cargo("Director")
                            .usuarioId(2)
                            .build()
            );
        });

        assertEquals("El usuario no tiene el rol de ADMIN", exception.getMessage());
        verify(usuarioRepository).findById(2);
        verify(administrativosRepository, never()).save(any(Administrativos.class));
    }

    @Test
    void createAdministrador_AlreadyExists() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(administrativosRepository.findByUsuarioId(1)).thenReturn(Optional.of(administrativo));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            administradorService.createAdministrador(administradorRequestDTO);
        });

        assertEquals("Ya existe un administrador con ese ID de usuario", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(administrativosRepository).findByUsuarioId(1);
        verify(administrativosRepository, never()).save(any(Administrativos.class));
    }

    @Test
    void updateAdministrador_Success() {
        // Arrange
        when(administrativosRepository.findById(1)).thenReturn(Optional.of(administrativo));

        when(administrativosRepository.save(any(Administrativos.class))).thenAnswer(invocation -> {
            Administrativos savedAdmin = invocation.getArgument(0);
            savedAdmin.setCargo("Subdirector");
            return savedAdmin;
        });

        AdministradorRequestDTO updateRequest = AdministradorRequestDTO.builder()
                .cargo("Subdirector")
                .usuarioId(1)
                .build();

        // Act
        AdministradorResponseDTO result = administradorService.updateAdministrador(1, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Subdirector", result.getCargo());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Juan", result.getUsuario().getNombre());
        verify(administrativosRepository).findById(1);
        verify(administrativosRepository).save(any(Administrativos.class));
    }

    @Test
    void updateAdministrador_NotFound() {
        // Arrange
        when(administrativosRepository.findById(99)).thenReturn(Optional.empty());

        AdministradorRequestDTO updateRequest = AdministradorRequestDTO.builder()
                .cargo("Subdirector")
                .usuarioId(1)
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            administradorService.updateAdministrador(99, updateRequest);
        });

        assertEquals("Administrador no encontrado", exception.getMessage());
        verify(administrativosRepository).findById(99);
        verify(administrativosRepository, never()).save(any(Administrativos.class));
    }

    @Test
    void deleteAdministrador_Success() {
        // Arrange
        when(administrativosRepository.existsById(1)).thenReturn(true);
        doNothing().when(administrativosRepository).deleteById(1);

        // Act
        boolean result = administradorService.deleteAdministrador(1);

        // Assert
        assertTrue(result);
        verify(administrativosRepository).existsById(1);
        verify(administrativosRepository).deleteById(1);
    }

    @Test
    void deleteAdministrador_NotFound() {
        // Arrange
        when(administrativosRepository.existsById(99)).thenReturn(false);

        // Act
        boolean result = administradorService.deleteAdministrador(99);

        // Assert
        assertFalse(result);
        verify(administrativosRepository).existsById(99);
        verify(administrativosRepository, never()).deleteById(anyInt());
    }
}