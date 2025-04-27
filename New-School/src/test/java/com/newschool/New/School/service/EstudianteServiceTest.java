package com.newschool.New.School.service;

import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.estudiante.EstudianteRequestDTO;
import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.EstudianteMapper;
import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.EstudiantesRepository;
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
class EstudianteServiceTest {

    @Mock
    private EstudiantesRepository estudiantesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    // Crear instancias reales de los mappers
    private UsuarioMapper usuarioMapper = new UsuarioMapper();
    private EstudianteMapper estudianteMapper = new EstudianteMapper();

    private EstudianteService estudianteService;

    private Usuario usuario;
    private Estudiantes estudiante;
    private EstudianteRequestDTO estudianteRequestDTO;
    private byte[] certificadoBytes;

    @BeforeEach
    void setUp() {
        // Configurar el UsuarioMapper en el EstudianteMapper
        ReflectionTestUtils.setField(estudianteMapper, "usuarioMapper", usuarioMapper);

        // Configurar el servicio manualmente
        estudianteService = new EstudianteService();
        ReflectionTestUtils.setField(estudianteService, "estudianteRepository", estudiantesRepository);
        ReflectionTestUtils.setField(estudianteService, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(estudianteService, "estudianteMapper", estudianteMapper);

        // Configurar datos de prueba
        certificadoBytes = "Certificado de Nacimiento".getBytes();

        // Configurar objetos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCi("1234567890");
        usuario.setNombre("Ana");
        usuario.setApellido("García");
        usuario.setEmail("ana.garcia@example.com");
        usuario.setPassword("password123");
        usuario.setRol("ESTUDIANTE");

        estudiante = new Estudiantes();
        estudiante.setIdEstudiante(1);
        estudiante.setFechaNacimiento("2000-01-15");
        estudiante.setCertificadoNacimiento(certificadoBytes);
        estudiante.setUsuarioIdUsuario(usuario);

        estudianteRequestDTO = EstudianteRequestDTO.builder()
                .fechaNacimiento("2000-01-15")
                .usuarioId(1)
                .certificadoNacimiento(certificadoBytes)
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        List<Estudiantes> estudiantesList = Arrays.asList(estudiante);
        when(estudiantesRepository.findAll()).thenReturn(estudiantesList);

        // Act
        List<EstudianteDTO> result = estudianteService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("2000-01-15", result.get(0).getFechaNacimiento());
        assertEquals(1, result.get(0).getUsuarioId());
        assertNotNull(result.get(0).getUsuario());
        assertEquals("Ana", result.get(0).getUsuario().getNombre());
        assertArrayEquals(certificadoBytes, result.get(0).getCertificadoNacimiento());
        verify(estudiantesRepository).findAll();
    }

    @Test
    void findById() {
        // Arrange - Caso cuando existe
        when(estudiantesRepository.findById(1)).thenReturn(Optional.of(estudiante));

        // Act
        EstudianteDTO result = estudianteService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("2000-01-15", result.getFechaNacimiento());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Ana", result.getUsuario().getNombre());
        assertArrayEquals(certificadoBytes, result.getCertificadoNacimiento());
        verify(estudiantesRepository).findById(1);

        // Arrange - Caso cuando no existe
        when(estudiantesRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        EstudianteDTO notFoundResult = estudianteService.findById(99);

        // Assert
        assertNull(notFoundResult);
        verify(estudiantesRepository).findById(99);
    }

    @Test
    void findByUsuarioId() {
        // Arrange - Caso cuando existe
        when(estudiantesRepository.findByUsuarioId(1)).thenReturn(Optional.of(estudiante));

        // Act
        EstudianteDTO result = estudianteService.findByUsuarioId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("2000-01-15", result.getFechaNacimiento());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Ana", result.getUsuario().getNombre());
        assertArrayEquals(certificadoBytes, result.getCertificadoNacimiento());
        verify(estudiantesRepository).findByUsuarioId(1);

        // Arrange - Caso cuando no existe
        when(estudiantesRepository.findByUsuarioId(99)).thenReturn(Optional.empty());

        // Act
        EstudianteDTO notFoundResult = estudianteService.findByUsuarioId(99);

        // Assert
        assertNull(notFoundResult);
        verify(estudiantesRepository).findByUsuarioId(99);
    }

    @Test
    void createEstudiante_Success() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(estudiantesRepository.findByUsuarioId(1)).thenReturn(Optional.empty());

        when(estudiantesRepository.save(any(Estudiantes.class))).thenAnswer(invocation -> {
            Estudiantes savedEstudiante = invocation.getArgument(0);
            savedEstudiante.setIdEstudiante(1);
            return savedEstudiante;
        });

        // Act
        EstudianteResponseDTO result = estudianteService.createEstudiante(estudianteRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("2000-01-15", result.getFechaNacimiento());
        assertEquals(1, result.getUsuarioId());
        assertTrue(result.isTieneCertificado());
        assertNotNull(result.getUsuario());
        assertEquals("Ana", result.getUsuario().getNombre());
        verify(usuarioRepository).findById(1);
        verify(estudiantesRepository).findByUsuarioId(1);
        verify(estudiantesRepository).save(any(Estudiantes.class));
    }

    @Test
    void createEstudiante_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudianteService.createEstudiante(
                    EstudianteRequestDTO.builder()
                            .fechaNacimiento("2000-01-15")
                            .usuarioId(99)
                            .certificadoNacimiento(certificadoBytes)
                            .build()
            );
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(99);
        verify(estudiantesRepository, never()).save(any(Estudiantes.class));
    }

    @Test
    void createEstudiante_WrongRole() {
        // Arrange
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setIdUsuario(2);
        usuarioAdmin.setCi("9876543210");
        usuarioAdmin.setNombre("Pedro");
        usuarioAdmin.setApellido("Gómez");
        usuarioAdmin.setEmail("pedro.gomez@example.com");
        usuarioAdmin.setPassword("password456");
        usuarioAdmin.setRol("ADMIN");

        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuarioAdmin));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudianteService.createEstudiante(
                    EstudianteRequestDTO.builder()
                            .fechaNacimiento("2000-01-15")
                            .usuarioId(2)
                            .certificadoNacimiento(certificadoBytes)
                            .build()
            );
        });

        assertEquals("El usuario no tiene el rol de ESTUDIANTE", exception.getMessage());
        verify(usuarioRepository).findById(2);
        verify(estudiantesRepository, never()).save(any(Estudiantes.class));
    }

    @Test
    void createEstudiante_AlreadyExists() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(estudiantesRepository.findByUsuarioId(1)).thenReturn(Optional.of(estudiante));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudianteService.createEstudiante(estudianteRequestDTO);
        });

        assertEquals("Ya existe un estudiante con ese ID de usuario", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(estudiantesRepository).findByUsuarioId(1);
        verify(estudiantesRepository, never()).save(any(Estudiantes.class));
    }

    @Test
    void updateEstudiante_Success() {
        // Arrange
        when(estudiantesRepository.findById(1)).thenReturn(Optional.of(estudiante));

        byte[] newCertificado = "Nuevo Certificado".getBytes();
        EstudianteRequestDTO updateRequest = EstudianteRequestDTO.builder()
                .fechaNacimiento("2001-02-20")
                .certificadoNacimiento(newCertificado)
                .build();

        when(estudiantesRepository.save(any(Estudiantes.class))).thenAnswer(invocation -> {
            Estudiantes savedEstudiante = invocation.getArgument(0);
            savedEstudiante.setFechaNacimiento("2001-02-20");
            savedEstudiante.setCertificadoNacimiento(newCertificado);
            return savedEstudiante;
        });

        // Act
        EstudianteResponseDTO result = estudianteService.updateEstudiante(1, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("2001-02-20", result.getFechaNacimiento());
        assertEquals(1, result.getUsuarioId());
        assertTrue(result.isTieneCertificado());
        assertNotNull(result.getUsuario());
        verify(estudiantesRepository).findById(1);
        verify(estudiantesRepository).save(any(Estudiantes.class));
    }

    @Test
    void updateEstudiante_WithoutNewCertificate() {
        // Arrange
        when(estudiantesRepository.findById(1)).thenReturn(Optional.of(estudiante));

        EstudianteRequestDTO updateRequest = EstudianteRequestDTO.builder()
                .fechaNacimiento("2001-02-20")
                .certificadoNacimiento(new byte[0])  // Certificado vacío
                .build();

        when(estudiantesRepository.save(any(Estudiantes.class))).thenAnswer(invocation -> {
            Estudiantes savedEstudiante = invocation.getArgument(0);
            savedEstudiante.setFechaNacimiento("2001-02-20");
            // Debe mantener el certificado original
            return savedEstudiante;
        });

        // Act
        EstudianteResponseDTO result = estudianteService.updateEstudiante(1, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("2001-02-20", result.getFechaNacimiento());
        assertEquals(1, result.getUsuarioId());
        assertTrue(result.isTieneCertificado());  // Todavía tiene certificado
        verify(estudiantesRepository).findById(1);
        verify(estudiantesRepository).save(any(Estudiantes.class));
    }

    @Test
    void updateEstudiante_NotFound() {
        // Arrange
        when(estudiantesRepository.findById(99)).thenReturn(Optional.empty());

        EstudianteRequestDTO updateRequest = EstudianteRequestDTO.builder()
                .fechaNacimiento("2001-02-20")
                .certificadoNacimiento(certificadoBytes)
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudianteService.updateEstudiante(99, updateRequest);
        });

        assertEquals("Estudiante no encontrado", exception.getMessage());
        verify(estudiantesRepository).findById(99);
        verify(estudiantesRepository, never()).save(any(Estudiantes.class));
    }

    @Test
    void deleteEstudiante_Success() {
        // Arrange
        when(estudiantesRepository.existsById(1)).thenReturn(true);
        doNothing().when(estudiantesRepository).deleteById(1);

        // Act
        boolean result = estudianteService.deleteEstudiante(1);

        // Assert
        assertTrue(result);
        verify(estudiantesRepository).existsById(1);
        verify(estudiantesRepository).deleteById(1);
    }

    @Test
    void deleteEstudiante_NotFound() {
        // Arrange
        when(estudiantesRepository.existsById(99)).thenReturn(false);

        // Act
        boolean result = estudianteService.deleteEstudiante(99);

        // Assert
        assertFalse(result);
        verify(estudiantesRepository).existsById(99);
        verify(estudiantesRepository, never()).deleteById(anyInt());
    }
}