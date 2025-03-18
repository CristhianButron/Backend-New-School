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
@Table(name = "grados")
@NamedQueries({
    @NamedQuery(name = "Grados.findAll", query = "SELECT g FROM Grados g"),
    @NamedQuery(name = "Grados.findByIdGrados", query = "SELECT g FROM Grados g WHERE g.id_grados = :id_grados"),
    @NamedQuery(name = "Grados.findByDescripcion", query = "SELECT g FROM Grados g WHERE g.descripcion = :descripcion"),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Grados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_grado")
    private Integer id_grados;

    @Basic (optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @Basic (optional = false)
    @Column(name = "primaria_secundaria")
    private String primaria_secundaria;

    // @OneToMany(mappedBy = "gradosIdgrados", fetch = FetchType.LAZY)
    // private List<Inscripcion_grados> inscripcion_gradosList;

    // @JoinColumn(name = "cursos_idcursos" , referencedColumnName = "id_cursos")
    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // private Cursos cursosIdCursos;

}
