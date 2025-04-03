package com.newschool.New.School.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "incripcion_grados")
@NamedQueries({
        @NamedQuery(name = "Inscripcion_grados.findAll", query = "SELECT i FROM Inscripcion_grados i"),
        @NamedQuery(name = "Inscripcion_grados.findById", query = "SELECT i FROM Inscripcion_grados i WHERE i.id = :id"),
        @NamedQuery(name = "Inscripcion_grados.findByEstudianteId", query = "SELECT i FROM Inscripcion_grados i WHERE i.estudiante.idEstudiante = :estudianteId"),
        @NamedQuery(name = "Inscripcion_grados.findByGradoId", query = "SELECT i FROM Inscripcion_grados i WHERE i.grado.id = :gradoId"),
        @NamedQuery(name = "Inscripcion_grados.findByGestion", query = "SELECT i FROM Inscripcion_grados i WHERE i.gestion = :gestion")
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
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;

    @Basic(optional = false)
    @Column(name = "gestion")
    private Integer gestion;

    @JoinColumn(name = "Estudiante_id", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiantes estudiante;

    @JoinColumn(name = "Grados_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grados grado;
}