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
@Table(name = "cursos")
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c"),
    @NamedQuery(name = "Cursos.findByIdCurso", query = "SELECT c FROM Cursos c WHERE c.idCursos = :idCursos"),
    @NamedQuery(name = "Cursos.findByNombreCurso", query = "SELECT c FROM Cursos c WHERE c.nombreCurso = :nombreCurso"),
    @NamedQuery(name = "Cursos.findByDescripcion", query = "SELECT c FROM Cursos c WHERE c.descripcion = :descripcion")})
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
    @Column(name = "id_cursos")
    private Integer idCursos;

    @Basic(optional = false)
    @Column(name = "nombre_curso")
    private String nombreCurso;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "cursosIdCurso", fetch = FetchType.LAZY)
    private List<Tareas> tareasList;

    @OneToMany(mappedBy = "cursosIdCurso", fetch = FetchType.LAZY)
    private List<Contenidos> contenidosList; 
    
    @JoinColumn(name = "docentes_id_docente", referencedColumnName = "id_docente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Docentes docentesIdDocente;

    @JoinColumn(name = "grados_id_grado", referencedColumnName = "id_grado")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grados gradosIdGrado;

}
