package org.entregasayd.sistemasentregas.dto.guias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDetalleGuiaDTO {
    //Detalles de la guia
    private GuiaDTO guia;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuiaDTO {
        private Long idGuia;
        private String numeroGuia;
        private String codigoInterno;
        private String descripcionContenido;
        private Double valorDeclarado;
        private Double pesoKg;
        private String dimensiones;
        private String observaciones;
        private String fechaCreacion;
        private String fechaRecoleccionReal;
        private String prioridad;
        private Integer intentosEntrega;
        private Double total;
        private String estadoActual;

        //Detalles del cliente
        private ClienteDTO cliente;
        //Detalles de la direccion de entrega
        private DireccionEntregaDTO direccionEntrega;

        //Listado de estados de la guia
        private ArrayList<GuiaEstadoDTO> estadosEntregas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClienteDTO {
        private Long idCliente;
        private String nombreCompleto;
        private String email;
        private String telefono;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DireccionEntregaDTO {
        private Long idDireccion;
        private String municipio;
        private String departamento;
        private String pais;
        private String codigoPostal;
        private String referencias;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuiaEstadoDTO {
        private Long idGuiaEstado;
        private String estadoAnterior;
        private String estadoNuevo;
        private String comentarios;
        private String motivoCambio;
        private String fechaCambio;
    }

}