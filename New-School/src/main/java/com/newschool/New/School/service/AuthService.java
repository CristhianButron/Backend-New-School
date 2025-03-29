package com.newschool.New.School.service;

import com.newschool.New.School.dto.AuthRequestDTO;
import com.newschool.New.School.dto.AuthResponseDTO;
import com.newschool.New.School.dto.RegisterRequestDTO;
import com.newschool.New.School.entity.*;

import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.*;

import com.newschool.New.School.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdministrativosRepository administrativosRepository;

    @Autowired
    private DocentesRepository docenteRepository;

    @Autowired
    private EstudiantesRepository estudianteRepository;

    @Autowired
    private PadresRepository padreRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario según su rol
     * Maneja los roles: ADMIN, DOCENTE, ESTUDIANTE, PADRE
     */
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setCi(request.getCi());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());

        // Guardar usuario
        usuario = usuarioRepository.save(usuario);

        // Procesar los datos específicos según el rol
        if (request.getDatosEspecificos() != null) {
            switch (request.getRol().toUpperCase()) {
                case "ADMIN":
                    registrarAdministrador(usuario, request);
                    break;
                case "DOCENTE":
                    registrarDocente(usuario, request);
                    break;
                case "ESTUDIANTE":
                    registrarEstudiante(usuario, request);
                    break;
                case "PADRE":
                    registrarPadre(usuario, request);
                    break;
                default:
                    throw new RuntimeException("Rol no soportado: " + request.getRol());
            }
        }

        // Generar token
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_" + usuario.getRol().toUpperCase())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        // Crear respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(usuarioMapper.toDTO(usuario))
                .build();
    }

    /**
     * Registra los datos específicos de un administrador
     */
    private void registrarAdministrador(Usuario usuario, RegisterRequestDTO request) {
        if (request.getDatosEspecificos().containsKey("cargo")) {
            Administrativos administrativo = new Administrativos();
            administrativo.setUsuarioIdUsuario(usuario);
            administrativo.setCargo(request.getDatosEspecificos().get("cargo"));
            administrativosRepository.save(administrativo);
        }
    }

    /**
     * Registra los datos específicos de un docente
     */
    private void registrarDocente(Usuario usuario, RegisterRequestDTO request) {
        if (request.getDatosEspecificos().containsKey("licenciatura")) {
            Docentes docente = new Docentes();
            docente.setUsuarioIdUsuario(usuario);
            docente.setLicenciatura(request.getDatosEspecificos().get("licenciatura"));

            // Procesar el título si existe en base64
            if (request.getDatosEspecificos().containsKey("titulo")) {
                try {
                    String tituloBase64 = request.getDatosEspecificos().get("titulo");
                    docente.setTitulo(Base64.getDecoder().decode(tituloBase64));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Formato inválido para el título del docente");
                }
            }

            docenteRepository.save(docente);
        }
    }

    /**
     * Registra los datos específicos de un estudiante
     */
    private void registrarEstudiante(Usuario usuario, RegisterRequestDTO request) {
        if (request.getDatosEspecificos().containsKey("fechaNacimiento")) {
            Estudiantes estudiante = new Estudiantes();
            estudiante.setUsuarioIdUsuario(usuario);
            estudiante.setFechaNacimiento(request.getDatosEspecificos().get("fechaNacimiento"));

            // Procesar el certificado de nacimiento si existe en base64
            if (request.getDatosEspecificos().containsKey("certificadoNacimiento")) {
                try {
                    String certificadoBase64 = request.getDatosEspecificos().get("certificadoNacimiento");
                    estudiante.setCertificadoNacimiento(Base64.getDecoder().decode(certificadoBase64));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Formato inválido para el certificado de nacimiento");
                }
            }

            estudianteRepository.save(estudiante);
        }
    }

    /**
     * Registra los datos específicos de un padre
     */
    private void registrarPadre(Usuario usuario, RegisterRequestDTO request) {
        if (request.getDatosEspecificos().containsKey("parentesco")) {
            Padres padre = new Padres();
            padre.setUsuarioIdUsuario(usuario);
            padre.setParentesco(request.getDatosEspecificos().get("parentesco"));
            padreRepository.save(padre);

            // Si se proporciona un ID de estudiante, vincular
            if (request.getDatosEspecificos().containsKey("estudianteId")) {
                try {
                    Integer estudianteId = Integer.parseInt(request.getDatosEspecificos().get("estudianteId"));
                    vincularPadreEstudiante(padre.getIdPadre(), estudianteId);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ID de estudiante inválido");
                }
            }
        }
    }

    /**
     * Vincula un padre con un estudiante en la tabla PadresAlumnos
     */
    public void vincularPadreEstudiante(Integer padreId, Integer estudianteId) {
        // Implementar la lógica para crear un registro en la tabla PadresAlumnos
        // Esto dependerá de cómo tengas modelada esa tabla en tu sistema
    }

    /**
     * Autentica un usuario y devuelve un token JWT
     */
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        // Autenticar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Si llegamos aquí, la autenticación fue exitosa
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        // Generar token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        // Crear respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(usuarioMapper.toDTO(usuario))
                .build();
    }
}