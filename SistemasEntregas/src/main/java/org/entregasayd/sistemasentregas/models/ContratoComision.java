package org.entregasayd.sistemasentregas.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contrato_comision")
public class ContratoComision {

    @Id
    @Column(name = "id_contrato_comision", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContratoComision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comision", nullable = false)
    private TipoComision tipoComision;

    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje = 0.00;

    @Column(name = "monto_fijo", nullable = false)
    private Double montoFijo = 0.00;

    @Column(name = "aplica_desde", nullable = false)
    private LocalDate aplicaDesde = LocalDate.now();

    @Column(name = "aplica_hasta")
    private LocalDate aplicaHasta;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "minimo_entregas_mes", nullable = false)
    private Integer minimoEntregasMes = 0;

    @Column(name = "maximo_entregas_mes")
    private Integer maximoEntregasMes;

    @Column(name = "factor_multiplicador", nullable = false)
    private Double factorMultiplicador = 1.00;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    public enum TipoComision {
        PORCENTAJE,
        FIJO_POR_ENTREGA,
        ESCALONADO
    }
}
