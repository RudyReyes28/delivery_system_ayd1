package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "empresa_fidelizacion")
public class EmpresaFidelizacion {

    @Id
    @Column(name = "id_empresa_fidelizacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresaFidelizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_fidelizacion", nullable = false)
    private NivelFindelizacion nivelFidelizacion;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "total_entregas", nullable = false)
    private Integer totalEntregas;

    @Column(name = "monto_total_entregas", nullable = false)
    private Double montoTotalEntregas;

    @Column(name = "total_cancelaciones", nullable = false)
    private Integer totalCancelaciones = 0;

    @Column(name = "descuento_aplicado", nullable = false)
    private Double descuentoAplicado = 0.00;

    @Column(name = "penalizaciones_aplicadas", nullable = false)
    private Double penalizacionesAplicadas = 0.00;


}
