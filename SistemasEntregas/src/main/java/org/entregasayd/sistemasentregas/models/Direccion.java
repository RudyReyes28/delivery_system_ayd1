package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @NotNull(message = "El tipo de direccion no puede ser nulo")
    @Column(name = "tipo_direccion", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDireccion tipoDireccion;

    @NotNull(message = "El municipio no puede ser nulo")
    @Column(name = "municipio", nullable = false, length = 100)
    private String municipio;

    @NotNull(message = "El departamento no puede ser nulo")
    @Column(name = "departamento", nullable = false, length = 100)
    private String departamento;

    @NotNull(message = "El pais no puede ser nulo")
    @Column(name = "pais", nullable = false)
    private String pais = "Guatemala";

    @NotNull(message = "El codigo postal no puede ser nulo")
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Column(name = "referencias", columnDefinition = "TEXT")
    private String referencias;

    @Column(name = "activa", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean activa = true;

    public enum TipoDireccion {
        RESIDENCIA,
        TRABAJO,
        ENTREGA,
        FISCAL
    }
}
