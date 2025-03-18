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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "padres_estudiantes")
@NamedQueries({
    @jakarta.persistence.NamedQuery(name = "PadresEstudiantes.findAll", query = "SELECT p FROM PadresEstudiantes p"),
    @jakarta.persistence.NamedQuery(name = "PadresEstudiantes.findByIdPadresEstudiantes", query = "SELECT p FROM PadresEstudiantes p WHERE p.id_padres_estudiantes = :id_padres_estudiantes")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class PadresEstudiantes implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_padres_estudiantes")
    private Integer id_padres_estudiantes;

    @JoinColumn(name = "padres_idpadres", referencedColumnName = "id_padre")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Padres padresIdpadres;

    @JoinColumn(name = "estudiantes_idestudiantes", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiantes estudiantesIdestudiantes;
}
