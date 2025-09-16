package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "nivel_fidelizacion")
public class NivelFindelizacion {

    @Id
    @Column(name = "id_nivel", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNivel;

    @Column(name = "codigo_nivel", nullable = false, unique = true, length = 20)
    private String codigoNivel;

    @Column(name = "nombre_nivel", nullable = false, length = 50)
    private String nombreNivel;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "entregas_minimas", nullable = false)
    private Integer entregasMinimas;

    @Column(name = "entregas_maximas")
    private Integer entregasMaximas;

    @Column(name = "descuento_porcentaje", nullable = false)
    private Double descuentoPorcentaje;

    @Column(name = "cancelaciones_gratuitas_mes", nullable = false)
    private Integer cancelacionesGratuitasMes = 0;

    @Column(name = "porcentaje_penalizacion", nullable = false)
    private Double porcentajePenalizacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fecha_fin_vigencia")
    private LocalDate fechaFinVigencia;


}
