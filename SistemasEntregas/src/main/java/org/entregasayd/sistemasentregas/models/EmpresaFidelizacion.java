package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "empresa_fidelizacion")
public class EmpresaFidelizacion {
    @Id
    @Column(name = "id_empresa_fidelizacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresaFidelizacion;
}
