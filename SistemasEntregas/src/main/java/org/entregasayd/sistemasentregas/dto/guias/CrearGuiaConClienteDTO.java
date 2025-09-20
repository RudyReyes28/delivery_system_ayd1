package org.entregasayd.sistemasentregas.dto.guias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearGuiaConClienteDTO {
    //Informacion basica del cliente
    private String nombreCompleto;
    private String telefono;
    private String email;
    //Preferencias de entrega
    private String horarioPreferidoInicio;
    private String horarioPreferidoFin;
    private String instruccionesEntrega;
    private Boolean aceptaEntregasVecinos = false;
    private Boolean requiereIdentificacion = false;

    //Direccion de entrega
    private String municipio;
    private String departamento;
    private String pais = "Guatemala";
    private String codigoPostal;
    private String referencias;
    private String aliasDireccion;
    private String instruccionesEspecificas;
    private String puntoReferencia;
    private String personaRecibe;

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
Ejemplo de JSON para crear una guia con cliente y direccion de entrega
{
    "nombreCompleto": "Juan Perez",
    "telefono": "5551234567",
    "email": "rudyreyes202031213@cunoc.edu.gt",
    "horarioPreferidoInicio": "09:00",
    "horarioPreferidoFin": "18:00",
    "instruccionesEntrega": "Dejar en la recepción si no estoy",
    "aceptaEntregasVecinos": true,
    "requiereIdentificacion": false,
    "municipio": "Guatemala",
    "departamento": "Guatemala",
    "pais": "Guatemala",
    "codigoPostal": "01001",
    "referencias": "Cerca del parque central",
    "aliasDireccion": "Oficina",
    "instruccionesEspecificas": "Usar el timbre 123",
    "puntoReferencia": "Frente a la iglesia",
    "personaRecibe": "Maria Lopez",
    "idUsuario": 2,
    "idTipoServicio": 1,
    "descripcionContenido": "Documentos importantes",
    "valorDeclarado": 500.00,
    "pesoKG": 0.5,
    "dimensiones": "30x20x5 cm",
    "esFragil": false,
    "observaciones": "Manejar con cuidado",
    "fechaProgramadaRecoleccion": "2025-10-01",
    "prioridad": "NORMAL",
    "subtotal": 50.00,
    "descuentos": 0.00,
    "recargos": 0.00,
    "formaPago": "CONTADO"
}
 */