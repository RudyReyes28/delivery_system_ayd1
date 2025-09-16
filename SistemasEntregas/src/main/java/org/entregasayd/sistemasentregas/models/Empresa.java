package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_empresa", nullable = false)
    private TipoEmpresa tipoEmpresa;

    @Column(name = "nombre_comercial", nullable = false, length = 200)
    private String nombreComercial;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String nit;

    @Column(name = "registro_mercantil", length = 50)
    private String registroMercantil;

    @Column(name = "fecha_constitucion")
    private LocalDate fechaConstitucion;

    @Column(name = "fecha_afiliacion", nullable = false)
    private LocalDate fechaAfiliacion;

    @Column(name = "fecha_vencimiento_afiliacion")
    private LocalDate fechaVencimientoAfiliacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.ACTIVA;

    public enum TipoEmpresa {
        COMERCIO_AFILIADO,
        EMPRESA_LOGISTICA,
        PROVEEDOR
    }

    public enum Estado {
        ACTIVA,
        INACTIVA,
        SUSPENDIDA,
        MOROSA
    }
}
