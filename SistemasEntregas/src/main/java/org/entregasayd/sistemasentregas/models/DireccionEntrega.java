package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "direccion_entrega")
/*DIRECCION_ENTREGA
    id_direccion_entrega (PK)
    id_cliente (FK)
    id_direccion (FK) -- Referencia a dirección base
    alias_direccion VARCHAR(50) -- "Casa", "Oficina", "Casa de mamá"
    instrucciones_especificas TEXT
    punto_referencia TEXT
    persona_recibe VARCHAR(200) -- Quien puede recibir en esa dirección
    activa BOOLEAN DEFAULT TRUE
*/
public class DireccionEntrega {
    @Id
    @Column(name = "id_direccion_entrega", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccionEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", nullable = false)
    private Direccion direccion;

    @Column(name = "alias_direccion", length = 50)
    private String aliasDireccion;

    @Column(name = "instrucciones_especificas", columnDefinition = "TEXT")
    private String instruccionesEspecificas;

    @Column(name = "punto_referencia", columnDefinition = "TEXT")
    private String puntoReferencia;

    @Column(name = "persona_recibe", length = 200)
    private String personaRecibe;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
