package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "asignacion_repartidor")
public class AsignacionRepartidor {
    @Id
    @Column(name = "id_asignacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;
}
