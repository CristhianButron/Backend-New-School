package com.newschool.New.School.entity;

import java.io.Serializable;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "curso")
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c"),
    @NamedQuery(name = "Cursos.findByIdCurso", query = "SELECT c FROM Cursos c WHERE c.idCurso = :idCurso"),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cursos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Basic(optional = false)
    @Column(name = "nombre_curso")
    private String nombreCurso;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "cursoIdCurso", fetch = FetchType.LAZY)
    private List<Tareas> tareasList;

    @OneToMany(mappedBy = "cursoIdCurso", fetch = FetchType.LAZY)
    private List<Contenidos> contenidosList; 

    @OneToMany(mappedBy = "cursoIdCurso", fetch = FetchType.LAZY)
    private List<Inscripcion_grados> inscripcionGradosList;
    
    @JoinColumn(name = "docentes_id_docente", referencedColumnName = "id_docente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Docentes docentesIdDocente;

}
