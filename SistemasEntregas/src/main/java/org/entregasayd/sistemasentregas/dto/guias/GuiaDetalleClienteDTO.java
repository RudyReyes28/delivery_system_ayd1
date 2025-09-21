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
public class GuiaDetalleClienteDTO {
    //Aqui vamos a colocar los detalles de la guia que vera el cliente
    private GuiaDetalleDto guia;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuiaDetalleDto {
        private Long idGuia;
        private String numeroGuia;
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

        //Listado de estados de la guia
        private ArrayList<GetDetalleGuiaDTO.GuiaEstadoDTO> estadosEntregas;
        //Detalle del repartidor
        private RepartidorDTO repartidor;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RepartidorDTO {
        private Long idRepartidor;
        private String nombreCompleto;
        private String email;
        private String telefono;
    }
}
