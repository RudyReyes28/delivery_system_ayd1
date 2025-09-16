package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guia")
public class Guia {

    @Id
    @Column(name = "id_guia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuia;

    @Column(name = "numero_guia", nullable = false, unique = true, length = 50)
    private String numeroGuia;

    @Column(name = "codigo_interno", nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal_origen")
    private Sucursal sucursalOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion_entrega")
    private Direccion direccionEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor")
    private Repartidor repartidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_servicio")
    private TipoServicio tipoServicio;

    @Column(name = "descripcion_contenido", columnDefinition = "TEXT", nullable = false)
    private String descripcionContenido;

    @Column(name = "valor_declarado")
    private Double valorDeclarado = 0.00;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "dimensiones", length = 50)
    private String dimensiones;

    @Column(name = "es_fragil", nullable = false)
    private Boolean esFragil = false;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private java.time.LocalDateTime fechaCreacion;

    @Column(name = "fecha_programada_recoleccion")
    private LocalDateTime fechaProgramadaRecoleccion;

    @Column(name = "fecha_recoleccion_real")
    private LocalDateTime fechaRecoleccionReal;

    @Column(name = "fecha_estimada_entrega")
    private LocalDateTime fechaEstimadaEntrega;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false)
    private EstadoActual estadoActual = EstadoActual.CREADA;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    private Prioridad prioridad = Prioridad.NORMAL;

    @Column(name = "intentos_entrega", nullable = false)
    private Integer intentosEntrega = 0;

    @Column(name = "max_intentos", nullable = false)
    private Integer maxIntentos = 3;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "descuentos")
    private Double descuentos = 0.00;

    @Column(name = "recargos")
    private Double recargos = 0.00;

    @Column(name = "total", nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false)
    private FormaPago formaPago = FormaPago.CREDITO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    public enum EstadoActual {
        CREADA,
        ASIGNADA,
        RECOLECTADA,
        EN_TRANSITO,
        EN_REPARTO,
        ENTREGADA,
        DEVUELTA,
        CANCELADA
    }

    public enum Prioridad {
        NORMAL,
        ALTA,
        URGENTE
    }

    public enum FormaPago {
        CREDITO,
        CONTADO,
        CONTRA_ENTREGA
    }

    public enum EstadoPago {
        PENDIENTE,
        PAGADO,
        VENCIDO
    }

}
