package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "empresa_contacto")
public class EmpresaContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa_contacto")
    private Long idEmpresaContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "cargo", length = 100, nullable = false)
    private String cargo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

}
