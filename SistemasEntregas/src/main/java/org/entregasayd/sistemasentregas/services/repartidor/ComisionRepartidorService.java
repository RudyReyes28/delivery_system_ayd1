package org.entregasayd.sistemasentregas.services.repartidor;

import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;
import org.entregasayd.sistemasentregas.dto.guias.GuiaDetalleClienteDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.CambiarEstadoDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.ComisionRepartidorDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.DetallesAsignacionRepartidorDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.RechazoAsignacionDTO;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.coordinador.*;

import java.util.ArrayList;
import java.util.List;


@Service
public class ComisionRepartidorService {
    @Autowired
    private ComisionEntregaRepository comisionEntregaRepository;

    @Autowired
    private LiquidacionRepartidorRepository liquidacionRepartidorRepository;

    @Autowired
    private PeriodoLiquidacionRepository periodoLiquidacionRepository;
    @Autowired
    private RepartidorRepository repartidorRepository;

    @Transactional(readOnly = true)
    public List<ComisionRepartidorDTO> obtenerComisionRepartidor(Long idUsuario) {
        //Obtenemos el repartidor asociado al usuario
        Repartidor repartidor = repartidorRepository.findByEmpleado_Usuario_IdUsuario(idUsuario);
        List<LiquidacionRepartidor> liquidaciones = liquidacionRepartidorRepository.findByRepartidor_IdRepartidor(repartidor.getIdRepartidor());

        List<ComisionRepartidorDTO> comisionRepartidorDTOs = new ArrayList<>();
        for (LiquidacionRepartidor liquidacion : liquidaciones) {
            ComisionRepartidorDTO.ComisionLiquidacionDTO comisionLiquidacionDTO = ComisionRepartidorDTO.ComisionLiquidacionDTO.builder()
                    .idLiquidacion(liquidacion.getIdLiquidacion())
                    .totalComisiones(liquidacion.getTotalComisiones())
                    .totalBonificaciones(liquidacion.getTotalBonificaciones())
                    .totalDeducciones(liquidacion.getTotalDeducciones())
                    .subtotal(liquidacion.getSubtotal())
                    .descuentoIgss(liquidacion.getDescuentoIgss())
                    .descuentoIsr(liquidacion.getDescuentoIsr())
                    .otrosDescuentos(liquidacion.getOtrosDescuentos())
                    .totalDescuentos(liquidacion.getTotalDescuentos())
                    .totalNeto(liquidacion.getTotalNeto())
                    .periodoLiquidacion(ComisionRepartidorDTO.PeriodoLiquidacionComisionDTO.builder()
                            .idPeriodo(liquidacion.getPeriodoLiquidacion().getIdPeriodo())
                            .descripcion(liquidacion.getPeriodoLiquidacion().getDescripcion())
                            .fechaInicio(liquidacion.getPeriodoLiquidacion().getFechaInicio().toString())
                            .fechaFin(liquidacion.getPeriodoLiquidacion().getFechaFin().toString())
                            .estado(liquidacion.getPeriodoLiquidacion().getEstado().toString())
                            .build())
                    .detallesComision(new ArrayList<>())
                    .build();

            List<ComisionEntrega> detallesComision = comisionEntregaRepository.findByRepartidor_IdRepartidorAndPeriodoLiquidacion_IdPeriodo(repartidor.getIdRepartidor(), liquidacion.getPeriodoLiquidacion().getIdPeriodo());
            for (ComisionEntrega detalle : detallesComision) {
                ComisionRepartidorDTO.DetalleComisionDTO detalleComisionDTO = ComisionRepartidorDTO.DetalleComisionDTO.builder()
                        .idDetalle(detalle.getIdComision())
                        .fechaEntrega(detalle.getGuia().getFechaEntregaReal().toString())
                        .numeroGuia(detalle.getGuia().getNumeroGuia())
                        .tipoComision(detalle.getTipoComision().toString())
                        .montoBaseCalculo(detalle.getMontoBaseCalculo())
                        .valorComision(detalle.getValorComision())
                        .montoComision(detalle.getMontoComision())
                        .bonificacion(detalle.getBonificacion())
                        .deduccion(detalle.getDeduccion())
                        .montoNeto(detalle.getMontoNeto())
                        .estado(detalle.getEstado().toString())
                        .build();
                comisionLiquidacionDTO.getDetallesComision().add(detalleComisionDTO);
            }
            ComisionRepartidorDTO comisionRepartidorDTO = ComisionRepartidorDTO.builder()
                    .comision(comisionLiquidacionDTO)
                    .build();
            comisionRepartidorDTOs.add(comisionRepartidorDTO);


        }
        return comisionRepartidorDTOs;
    }

}
