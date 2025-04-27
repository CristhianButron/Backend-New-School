package com.newschool.New.School.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "estudiante")
@NamedQueries({
        @NamedQuery(name = "Estudiantes.findAll", query = "SELECT e FROM Estudiantes e"),
        @NamedQuery(name = "Estudiantes.findByIdEstudiante", query = "SELECT e FROM Estudiantes e WHERE e.idEstudiante = :idEstudiante"),
        @NamedQuery(name = "Estudiantes.findByFechaNacimiento", query = "SELECT e FROM Estudiantes e WHERE e.fechaNacimiento = :fechaNacimiento")
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"certificadoNacimiento", "padresEstudiantesList", "respuestasList", "inscripcionGradosList"})

public class Estudiantes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @Lob
    @Column(name = "certificado_nacimiento")
    private byte[] certificadoNacimiento;

    @JoinColumn(name = "usuarios_id_usuarios", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioIdUsuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiantesIdEstudiantes", fetch = FetchType.LAZY)
    private List<PadresEstudiantes> padresEstudiantesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiantesIdEstudiantes", fetch = FetchType.LAZY)
    private List<Respuestas> respuestasList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante", fetch = FetchType.LAZY)
    private List<Inscripcion_grados> inscripcionGradosList;

}