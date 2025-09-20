package org.entregasayd.sistemasentregas.dto.guias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearGuiaSinClienteDTO
{
    //Guia con cliente existente
    private long idCliente;
    //Datos de la guia
    private long idUsuario; //Usuario que crea la guia de aqui sale el idEmpresa y idSucursal
    private long idTipoServicio;
    private String descripcionContenido;
    private Double valorDeclarado;
    private Double pesoKG;
    private String dimensiones;
    private Boolean esFragil = false;
    private String observaciones;
    private String fechaProgramadaRecoleccion; //Formato AAAA-MM-DD
    private String prioridad; //NORMAL, ALTA, URGENTE
    //Calculos financieros
    private Double subtotal;
    private Double descuentos = 0.00;
    private Double recargos = 0.00;
    //Informacion del pago
    private String formaPago; //CREDITO, CONTADO, TARJETA
}

/*
Ejemplo de JSON para crear una guia con cliente existente
{
    "idCliente": 1,
    "idUsuario": 1,
    "idTipoServicio": 1,
    "descripcionContenido": "Documentos importantes",
    "valorDeclarado": 500.00,
    "pesoKG": 0.5,
    "dimensiones": "30x20x5 cm",
    "esFragil": false,
    "observaciones": "Entregar en mano",
    "fechaProgramadaRecoleccion": "2024-10-01",
    "prioridad": "NORMAL",
    "subtotal": 50.00,
    "descuentos": 0.00,
    "recargos": 0.00,
    "formaPago": "CONTADO"
 }
 */