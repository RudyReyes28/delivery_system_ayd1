package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "factura_empresa")
public class FacturaEmpresa {

    @Id
    @Column(name = "id_factura", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @Column(name = "serie_factura", length = 10, nullable = false)
    private String serieFactura = "A";

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "descuento_fidelizacion")
    private Double descuentoFidelizacion = 0.00;

    @Column(name = "recargos")
    private Double recargos = 0.00;

    @Column(name = "penalizaciones")
    private Double penalizaciones = 0.00;

    @Column(name = "base_imponible", nullable = false)
    private Double baseImponible;

    @Column(name = "iva")
    private Double iva = 0.00;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", nullable = false)
    private EstadoFactura estadoFactura = EstadoFactura.BORRADOR;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda = "GTQ";

    @Column(name = "tipo_cambio")
    private Double tipoCambio = 1.0000;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false)
    private FormaPago formaPago = FormaPago.CREDITO;

    @Column(name = "dias_credito")
    private Integer diasCredito = 30;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    public enum EstadoFactura {
        BORRADOR,
        EMITIDA,
        PAGADA,
        VENCIDA,
        ANULADA
    }

    public enum FormaPago {
        CREDITO,
        CONTADO,
        TRANSFERENCIA,
        CHEQUE
    }
}
