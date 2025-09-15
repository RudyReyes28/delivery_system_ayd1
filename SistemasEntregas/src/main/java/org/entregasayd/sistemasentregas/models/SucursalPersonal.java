package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sucursal")
public class SucursalPersonal {
    @Id
    @Column(name = "id_sucursal_personal")
    private Long idSucursalPersonal;
}
