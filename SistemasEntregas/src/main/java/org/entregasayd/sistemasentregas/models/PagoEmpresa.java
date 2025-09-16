package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pago_empresa")
public class PagoEmpresa {
    /*PAGO_EMPRESA
    id_pago (PK)
    id_factura (FK) NULL -- Puede ser pago a cuenta
    id_empresa (FK)

    -- Información del pago
    numero_recibo VARCHAR(50) UNIQUE NOT NULL
    monto_pago DECIMAL(12,2) NOT NULL
    moneda VARCHAR(3) DEFAULT 'GTQ'
    tipo_cambio DECIMAL(10,4) DEFAULT 1.0000
    fecha_pago DATE NOT NULL

    -- Forma de pago
    forma_pago ENUM('efectivo','transferencia','cheque','tarjeta','deposito') NOT NULL
    referencia_pago VARCHAR(100) -- Número de cheque, transferencia, etc.
    banco_origen VARCHAR(100)
    cuenta_destino VARCHAR(50)

    -- Estado del pago
    estado_pago ENUM('registrado','confirmado','conciliado','rechazado') DEFAULT 'registrado'
    fecha_confirmacion TIMESTAMP NULL
	*/
    @Id
    @Column(name = "id_pago", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura")
    private FacturaEmpresa facturaEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "numero_recibo", nullable = false, unique = true, length = 50)
    private String numeroRecibo;

    @Column(name = "monto_pago", nullable = false)
    private Double montoPago;

    @Column(name = "moneda", length = 3)
    private String moneda = "GTQ";

    @Column(name = "tipo_cambio")
    private Double tipoCambio = 1.0000;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false)
    private FormaPago formaPago;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    @Column(name = "banco_origen", length = 100)
    private String bancoOrigen;

    @Column(name = "cuenta_destino", length = 50)
    private String cuentaDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago = EstadoPago.REGISTRADO;

    @Column(name = "fecha_confirmacion")
    private LocalDate fechaConfirmacion;

    public enum FormaPago {
        EFECTIVO,
        TRANSFERENCIA,
        CHEQUE,
        TARJETA,
        DEPOSITO
    }

    public enum EstadoPago {
        REGISTRADO,
        CONFIRMADO,
        CONCILIADO,
        RECHAZADO
    }


}
