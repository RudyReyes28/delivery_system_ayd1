package org.entregasayd.sistemasentregas.services.guias;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.guias.*;
import org.entregasayd.sistemasentregas.services.email.EmailService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class GuiaClienteService {
    @Autowired
    private GuiaRespository guiaRepository;
    @Autowired
    private GuiaEstadoRepository guiaEstadoRepository;

    
    //Metodo para devolver el detalle de una guia para el cliente
    public GuiaDetalleClienteDTO getDetalleGuiaCliente(String numeroGuia) throws Exception {
        GuiaDetalleClienteDTO guiaDetalleClienteDTO = new GuiaDetalleClienteDTO();
        GuiaDetalleClienteDTO.GuiaDetalleDto guiaDetalleDto = new GuiaDetalleClienteDTO.GuiaDetalleDto();

        Guia guia = guiaRepository.findByNumeroGuia(numeroGuia);
        if(guia == null){
            throw new Exception("La guia con numero " + numeroGuia + " no existe");
        }

        guiaDetalleDto.setIdGuia(guia.getIdGuia());
        guiaDetalleDto.setNumeroGuia(guia.getNumeroGuia());
        guiaDetalleDto.setDescripcionContenido(guia.getDescripcionContenido());
        guiaDetalleDto.setValorDeclarado(guia.getValorDeclarado());
        guiaDetalleDto.setPesoKg(guia.getPesoKg());
        guiaDetalleDto.setDimensiones(guia.getDimensiones());
        guiaDetalleDto.setObservaciones(guia.getObservaciones());
        guiaDetalleDto.setFechaCreacion(guia.getFechaCreacion().toString());
        if(guia.getFechaRecoleccionReal() != null){
            guiaDetalleDto.setFechaRecoleccionReal(guia.getFechaRecoleccionReal().toString());
        }else{
            guiaDetalleDto.setFechaRecoleccionReal("No ha sido recolectada");
        }
        guiaDetalleDto.setPrioridad(guia.getPrioridad().name());
        guiaDetalleDto.setIntentosEntrega(guia.getIntentosEntrega());
        guiaDetalleDto.setTotal(guia.getTotal());
        guiaDetalleDto.setEstadoActual(guia.getEstadoActual().name());

        //Listado de estados de la guia
        ArrayList<GetDetalleGuiaDTO.GuiaEstadoDTO> estadosDTO = new ArrayList<>();
        ArrayList<GuiaEstado> estados = guiaEstadoRepository.findByGuiaIdGuiaOrderByFechaCambioDesc(guia.getIdGuia());
        for (GuiaEstado ge : estados) {
            GetDetalleGuiaDTO.GuiaEstadoDTO estadoDTO = new GetDetalleGuiaDTO.GuiaEstadoDTO();
            estadoDTO.setIdGuiaEstado(ge.getIdGuiaEstado());
            estadoDTO.setEstadoAnterior(ge.getEstadoAnterior().name());
            estadoDTO.setEstadoNuevo(ge.getEstadoNuevo().name());
            estadoDTO.setFechaCambio(ge.getFechaCambio().toString());
            estadoDTO.setComentarios(ge.getComentarios());
            estadoDTO.setMotivoCambio(ge.getMotivoCambio());
            estadosDTO.add(estadoDTO);
        }
        guiaDetalleDto.setEstadosEntregas(estadosDTO);
        
        //Detalle del repartidor
        if(guia.getRepartidor() != null) {
            GuiaDetalleClienteDTO.RepartidorDTO repartidorDTO = new GuiaDetalleClienteDTO.RepartidorDTO();
            Repartidor repartidor = guia.getRepartidor();
            Persona usuarioRepartidor = repartidor.getEmpleado().getUsuario().getPersona();
            repartidorDTO.setIdRepartidor(repartidor.getIdRepartidor());
            repartidorDTO.setNombreCompleto(usuarioRepartidor.getNombre() + " " + usuarioRepartidor.getApellido());
            repartidorDTO.setEmail(usuarioRepartidor.getCorreo());
            repartidorDTO.setTelefono(usuarioRepartidor.getTelefono());
            guiaDetalleDto.setRepartidor(repartidorDTO);
        }else{
            guiaDetalleDto.setRepartidor(null);
        }
        guiaDetalleClienteDTO.setGuia(guiaDetalleDto);
        return guiaDetalleClienteDTO;
     
    }

}
