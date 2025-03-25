package com.newschool.New.School.entity;

import java.io.Serializable;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "inscripciongrados")
@NamedQueries({
    @NamedQuery(name = "Inscripcion_grados.findAll", query = "SELECT i FROM Inscripcion_grados i"),
    @NamedQuery(name = "Inscripcion_grados.findByIdInscripcionGrados", query = "SELECT i FROM Inscripcion_grados i WHERE i.id_inscripcion_grados = :id_inscripcion_grados"),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Inscripcion_grados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_inscripcion_grados")
    private Integer id_inscripcion_grados;

    @Basic (optional = false)
    @Column(name = "fecha_inscripcion")
    private String fecha_inscripcion;

    @Basic (optional = false)
    @Column(name = "gestion")
    private String gestion;

    @JoinColumn(name = "grados_id_grados", referencedColumnName = "id_grado")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grados gradosIdGrados;


    @JoinColumn(name = "estudiantes_id_estudiantes", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiantes estudiantesIdEstudiantes;

    @JoinColumn(name = "cursos_id_cursos", referencedColumnName = "id_curso")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cursos cursoIdCurso;
}
