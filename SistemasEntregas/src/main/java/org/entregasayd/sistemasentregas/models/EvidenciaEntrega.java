package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "evidencia_entrega")
public class EvidenciaEntrega {

    @Id
    @Column(name = "id_evidencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvidenciaEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    private Guia guia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evidencia", nullable = false)
    private TipoEvidencia tipoEvidencia;

    @Column(name = "nombre_archivo", length = 200)
    private String nombreArchivo;

    @Column(name = "url_archivo", nullable = false, length = 500)
    private String urlArchivo;

    @Column(name = "nombre_receptor", length = 200)
    private String nombreReceptor;

    @Column(name = "documento_receptor", length = 50)
    private String documentoReceptor;

    @Column(name = "parentesco_receptor", length = 50)
    private String parentescoReceptor;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    public enum TipoEvidencia {
        FOTO_PAQUETE,
        FOTO_ENTREGA,
        FIRMA_DIGITAL,
        DOCUMENTO_IDENTIDAD
    }
}
