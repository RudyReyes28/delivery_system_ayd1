package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {
    @Id
    @Column(name = "id_tipo_servicio", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoServicio;

    @Column(name = "codigo_servicio", nullable = false, unique = true, length = 20)
    private String codigoServicio;

    @Column(name = "nombre_servicio", nullable = false, length = 100)
    private String nombreServicio;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tiempo_entrega_horas", nullable = false)
    private Integer tiempoEntregaHoras;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    @Column(name = "peso_maximo_kg")
    private Double pesoMaximoKg;

    @Column(name = "volumen_maximo_m3")
    private Double volumenMaximoM3;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;


}
