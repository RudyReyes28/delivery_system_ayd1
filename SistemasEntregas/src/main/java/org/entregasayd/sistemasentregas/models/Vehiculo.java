package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @Column(name = "id_vehiculo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false)
    private TipoVehiculo tipoVehiculo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "placa", unique = true, length = 20)
    private String placa;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "capacidad_carga_kg", nullable = false)
    private Double capacidadCargaKg = 0.00;

    @Column(name = "capacidad_volumen_m3", nullable = false)
    private Double capacidadVolumenM3 = 0.00;

    // Documentación
    @Column(name = "numero_tarjeta_circulacion", length = 50)
    private String numeroTarjetaCirculacion;

    @Column(name = "numero_poliza_seguro", length = 50)
    private String numeroPolizaSeguro;

    @Column(name = "fecha_vencimiento_seguro")
    private java.time.LocalDate fechaVencimientoSeguro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_vehiculo", nullable = false)
    private EstadoVehiculo estadoVehiculo = EstadoVehiculo.ACTIVO;

    public enum TipoVehiculo {
        BICICLETA,
        MOTOCICLETA,
        AUTOMOVIL,
        PICKUP,
        VAN,
        A_PIE
    }

    public enum EstadoVehiculo {
        ACTIVO,
        MANTENIMIENTO,
        AVERIADO,
        INACTIVO
    }

}
