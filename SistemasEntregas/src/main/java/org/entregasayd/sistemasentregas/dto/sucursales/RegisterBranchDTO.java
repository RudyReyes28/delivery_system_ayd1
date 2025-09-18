package org.entregasayd.sistemasentregas.dto.sucursales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBranchDTO {

    //SUCURSAL
    private Long idEmpresa;
    private String codigoSucursal;
    private String nombreSucursal;
    private String horarioApertura;
    private String horarioCierre;
    private String diasOperacion;
    private String estado; //ACTIVA, INACTIVA, TEMPORAL, MANTENIMIENTO

    //DIRECCION
    private String tipoDireccion;
    private String municipio;
    private String departamento;
    private String pais = "Guatemala";
    private String codigoPostal;
    private String referencias;
    private boolean activa = true;

    //PERSONAL SUCURSAL
    private long idUsuario;
    private String cargo;
    private boolean esEncargado = true;
    private String fechaFin; //Formato "yyyy-MM-dd"
    private boolean activo = true;

}

/*Ejemplo json
{
    "idEmpresa": 1,
    "codigoSucursal": "SUC001",
    "nombreSucursal": "Sucursal Central",
    "horarioApertura": "08:00",
    "horarioCierre": "18:00",
    "diasOperacion": "LMMJVSD",
    "estado": "ACTIVA",
    "tipoDireccion": "Comercial",
    "municipio": "Guatemala",
    "departamento": "Guatemala",
    "pais": "Guatemala",
    "codigoPostal": "01001",
    "referencias": "Cerca del parque central",
    "activa": true,
    "idUsuario": 2,
    "cargo": "Gerente de Sucursal",
    "esEncargado": true,
    "fechaFin": "2025-12-31",
    "activo": true
}

 */