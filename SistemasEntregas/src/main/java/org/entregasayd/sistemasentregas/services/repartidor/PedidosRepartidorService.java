package org.entregasayd.sistemasentregas.services.repartidor;
import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;
import org.entregasayd.sistemasentregas.dto.guias.GuiaDetalleClienteDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.CambiarEstadoDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.DetallesAsignacionRepartidorDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.RechazoAsignacionDTO;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.coordinador.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class PedidosRepartidorService {

    @Autowired
    private GuiaRespository guiaRepository;
    @Autowired
    private TipoServicioRepository tipoServicioRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private GuiaEstadoRepository guiaEstadoRepository;
    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ContratoRepository contratoRepository;
    @Autowired
    private AsignacionRepartidorRepository asignacionRepartidorRepository;

    //Obtener todas las asignaciones del repartidor
    public ArrayList<DetallesAsignacionRepartidorDTO> getAsignacionesRepartidor(Long idUsuario){
        Repartidor repartidor = repartidorRepository.findByEmpleado_Usuario_IdUsuario(idUsuario);
        if (repartidor == null) {
            throw new RuntimeException("El usuario no es un repartidor");
        }


        ArrayList<DetallesAsignacionRepartidorDTO> asignacionesDTO = new ArrayList<>();

        List<AsignacionRepartidor> asignaciones = asignacionRepartidorRepository.findByRepartidor_IdRepartidor(repartidor.getIdRepartidor());

        for (AsignacionRepartidor asignacion : asignaciones) {
            DetallesAsignacionRepartidorDTO.AsignacionRepartidorDTO asignacionDTO = DetallesAsignacionRepartidorDTO.AsignacionRepartidorDTO.builder()
                    .idAsignacion(asignacion.getIdAsignacion())
                    .fechaAsignacion(asignacion.getFechaAsignacion().toString())
                    .estadoAsignacion(asignacion.getEstadoAsignacion().name())
                    .guia(DetallesAsignacionRepartidorDTO.GuiaAsignacionDTO.builder()
                            .idGuia(asignacion.getGuia().getIdGuia())
                            .numeroGuia(asignacion.getGuia().getNumeroGuia())
                            .codigoInterno(asignacion.getGuia().getCodigoInterno())
                            .descripcionContenido(asignacion.getGuia().getDescripcionContenido())
                            .valorDeclarado(asignacion.getGuia().getValorDeclarado())
                            .pesoKg(asignacion.getGuia().getPesoKg())
                            .dimensiones(asignacion.getGuia().getDimensiones())
                            .estadoGuia(asignacion.getGuia().getEstadoActual().name())
                            .cliente(new GetDetalleGuiaDTO.ClienteDTO(
                                    asignacion.getGuia().getCliente().getIdCliente(),
                                    asignacion.getGuia().getCliente().getNombreCompleto(),
                                    asignacion.getGuia().getCliente().getEmail(),
                                    asignacion.getGuia().getCliente().getTelefono()
                            ))
                            .direccionEntrega(new GetDetalleGuiaDTO.DireccionEntregaDTO(
                                    asignacion.getGuia().getDireccionEntrega().getIdDireccion(),
                                    asignacion.getGuia().getDireccionEntrega().getMunicipio(),
                                    asignacion.getGuia().getDireccionEntrega().getDepartamento(),
                                    asignacion.getGuia().getDireccionEntrega().getPais(),
                                    asignacion.getGuia().getDireccionEntrega().getCodigoPostal(),
                                    asignacion.getGuia().getDireccionEntrega().getReferencias()
                            ))
                            .build())
                    .build();
            DetallesAsignacionRepartidorDTO detalleDTO = DetallesAsignacionRepartidorDTO.builder()
                    .asignacion(asignacionDTO)
                    .build();
            asignacionesDTO.add(detalleDTO);
        }
        return asignacionesDTO;
    }

    //Aceptar una asignacion
    @Transactional
    public String aceptarAsignacion(Long idAsignacion){
        AsignacionRepartidor asignacion = asignacionRepartidorRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignacion no encontrada"));

        if(asignacion.getEstadoAsignacion() != AsignacionRepartidor.EstadoAsignacion.PENDIENTE){
            throw new RuntimeException("La asignacion ya ha sido procesada");
        }

        asignacion.setEstadoAsignacion(AsignacionRepartidor.EstadoAsignacion.ACEPTADA);
        asignacion.setFechaAceptacion(LocalDateTime.now());
        asignacionRepartidorRepository.save(asignacion);

        Guia guia = asignacion.getGuia();
        guia.setEstadoActual(Guia.EstadoActual.ASIGNADA);
        guiaRepository.save(guia);

        //Obtenemos el repartidor
        Repartidor repartidor = asignacion.getRepartidor();

        GuiaEstado nuevoEstado = new GuiaEstado();
        nuevoEstado.setGuia(guia);
        nuevoEstado.setEstadoAnterior(GuiaEstado.Estado.CREADA);
        nuevoEstado.setEstadoNuevo(GuiaEstado.Estado.ASIGNADA);
        nuevoEstado.setUsuarioCambio(repartidor.getEmpleado().getUsuario());
        nuevoEstado.setFechaCambio(LocalDateTime.now());
        nuevoEstado.setMotivoCambio("El repartidor ha aceptado la asignacion y la guia esta en reparto");
        guiaEstadoRepository.save(nuevoEstado);

        //Enviar correo al cliente notificando que su guia esta asignada
        String asunto = "Notificación de asignación de guía";
        String mensaje = "Estimado/a " + guia.getCliente().getNombreCompleto() + ",\n\n" +
                "Nos complace informarle que su guía con número "+ guia.getNumeroGuia() + " ha sido asignada a un repartidor.\n" +
                "Pronto recibirá su entrega.\n\n" +
                "Gracias por confiar en nosotros.\n" +
                "Atentamente,\n" +
                "El equipo de Entregas SIE";
        emailService.enviarEmailGenerico(guia.getCliente().getEmail(), asunto, mensaje);

        return "Asignacion aceptada correctamente";
    }

    //Rechazar una asignacion
    @Transactional
    public String rechazarAsignacion(Long idAsignacion, RechazoAsignacionDTO rechazoDTO){
        AsignacionRepartidor asignacion = asignacionRepartidorRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignacion no encontrada"));

        if(asignacion.getEstadoAsignacion() != AsignacionRepartidor.EstadoAsignacion.PENDIENTE){
            throw new RuntimeException("La asignacion ya ha sido procesada");
        }

        asignacion.setEstadoAsignacion(AsignacionRepartidor.EstadoAsignacion.RECHAZADA);
        asignacion.setFechaRechazo(LocalDateTime.now());
        asignacion.setMotivoRechazo(rechazoDTO.getMotivoRechazo());
        asignacionRepartidorRepository.save(asignacion);

        Guia guia = asignacion.getGuia();
        guia.setEstadoActual(Guia.EstadoActual.CREADA);
        guia.setRepartidor(null);
        guiaRepository.save(guia);

        /*GuiaEstado nuevoEstado = new GuiaEstado();
        nuevoEstado.setGuia(guia);
        nuevoEstado.setUsuarioCambio(asignacion.getRepartidor().getEmpleado().getUsuario());
        nuevoEstado.setEstadoAnterior(GuiaEstado.Estado.CREADA);
        nuevoEstado.setEstadoNuevo(GuiaEstado.Estado.CREADA);
        nuevoEstado.setFechaCambio(LocalDateTime.now());
        nuevoEstado.setMotivoCambio("El repartidor ha rechazado la asignacion. Motivo: " + rechazoDTO.getMotivoRechazo());
        guiaEstadoRepository.save(nuevoEstado);*/

        return "Asignacion rechazada correctamente";
    }

    //Obtenemos todos los detalles de las guias asignadas al repartidor, exceptuando las que estan en estado CREADA
    public ArrayList<GetDetalleGuiaDTO> getDetallesGuiasAsignadas(Long idUsuario) {
        ArrayList<GetDetalleGuiaDTO> guiasDTO = new ArrayList<>();

        //Obtenemos el repartidor asociado al usuario
        Repartidor repartidor = repartidorRepository.findByEmpleado_Usuario_IdUsuario(idUsuario);
        if (repartidor == null) {
            throw new RuntimeException("El usuario no es un repartidor");
        }

        List<AsignacionRepartidor> asignaciones = asignacionRepartidorRepository.findByRepartidor_IdRepartidor(repartidor.getIdRepartidor());

        for (AsignacionRepartidor asignacion : asignaciones) {
            Guia guia = asignacion.getGuia();
            //Verificar que la guia no este en estado CREADA y que la asignacion haya sido aceptada
            if (guia.getEstadoActual() == Guia.EstadoActual.CREADA || asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.RECHAZADA || asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.PENDIENTE) {
                continue; // Saltamos las guias en estado CREADA
            }

            //Obtenemos los estados de la guia
            List<GuiaEstado> estados = guiaEstadoRepository.findByGuiaIdGuiaOrderByFechaCambioDesc(guia.getIdGuia());
            ArrayList<GetDetalleGuiaDTO.GuiaEstadoDTO> estadosDTO = new ArrayList<>();
            for (GuiaEstado estado : estados) {
                GetDetalleGuiaDTO.GuiaEstadoDTO estadoDTO = GetDetalleGuiaDTO.GuiaEstadoDTO.builder()
                        .idGuiaEstado(estado.getIdGuiaEstado())
                        .estadoAnterior(estado.getEstadoAnterior().name())
                        .estadoNuevo(estado.getEstadoNuevo().name())
                        .comentarios(estado.getComentarios())
                        .motivoCambio(estado.getMotivoCambio())
                        .fechaCambio(estado.getFechaCambio().toString())
                        .build();
                estadosDTO.add(estadoDTO);
            }

            GetDetalleGuiaDTO.GuiaDTO guiaDTO = GetDetalleGuiaDTO.GuiaDTO.builder()
                    .idGuia(guia.getIdGuia())
                    .numeroGuia(guia.getNumeroGuia())
                    .codigoInterno(guia.getCodigoInterno())
                    .descripcionContenido(guia.getDescripcionContenido())
                    .valorDeclarado(guia.getValorDeclarado())
                    .pesoKg(guia.getPesoKg())
                    .dimensiones(guia.getDimensiones())
                    .observaciones(guia.getObservaciones())
                    .fechaCreacion(guia.getFechaCreacion().toString())
                    .fechaRecoleccionReal(guia.getFechaRecoleccionReal() != null ? guia.getFechaRecoleccionReal().toString() : null)
                    .prioridad(guia.getPrioridad().name())
                    .intentosEntrega(guia.getIntentosEntrega())
                    .total(guia.getTotal())
                    .estadoActual(guia.getEstadoActual().name())
                    .cliente(new GetDetalleGuiaDTO.ClienteDTO(
                            guia.getCliente().getIdCliente(),
                            guia.getCliente().getNombreCompleto(),
                            guia.getCliente().getEmail(),
                            guia.getCliente().getTelefono()
                    ))
                    .direccionEntrega(new GetDetalleGuiaDTO.DireccionEntregaDTO(
                            guia.getDireccionEntrega().getIdDireccion(),
                            guia.getDireccionEntrega().getMunicipio(),
                            guia.getDireccionEntrega().getDepartamento(),
                            guia.getDireccionEntrega().getPais(),
                            guia.getDireccionEntrega().getCodigoPostal(),
                            guia.getDireccionEntrega().getReferencias()
                    ))
                    .estadosEntregas(estadosDTO)
                    .build();
            GetDetalleGuiaDTO guiaDetalleDTO = GetDetalleGuiaDTO.builder()
                    .guia(guiaDTO)
                    .build();
            guiasDTO.add(guiaDetalleDTO);
        }
        return guiasDTO;
    }

    //Cambiar el estado de una guia asignada
    @Transactional
    public String cambiarEstadoGuiaAsignada(CambiarEstadoDTO cambiarEstadoDTO) {
        Guia guia = guiaRepository.findById(cambiarEstadoDTO.getIdGuia())
                .orElseThrow(() -> new RuntimeException("Guia no encontrada"));

        //Obtenemos la asignacion del repartidor
        List<AsignacionRepartidor> asignacion = asignacionRepartidorRepository.findByGuia_IdGuia(guia.getIdGuia());
        for(AsignacionRepartidor a : asignacion){
            if(a.getEstadoAsignacion() != AsignacionRepartidor.EstadoAsignacion.ACEPTADA){
                throw new RuntimeException("La guia no tiene una asignacion aceptada por un repartidor");
            }
        }

        //Validamos que el nuevo estado sea diferente al actual
        Guia.EstadoActual nuevoEstado;
        try {
            nuevoEstado = Guia.EstadoActual.valueOf(cambiarEstadoDTO.getNuevoEstado());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El estado proporcionado no es valido");
        }

        if (guia.getEstadoActual() == nuevoEstado) {
            throw new RuntimeException("El nuevo estado debe ser diferente al estado actual");
        }

        //Validamos la transicion de estados
        if (!esTransicionValida(guia.getEstadoActual(), nuevoEstado)) {
            throw new RuntimeException("La transicion de estados no es valida");
        }

        //Obtenemos el repartidor
        Repartidor repartidor = repartidorRepository.findByEmpleado_Usuario_IdUsuario(cambiarEstadoDTO.getIdUsuario());


        //Actualizamos el estado de la guia
        Guia.EstadoActual estadoAnterior = guia.getEstadoActual();
        guia.setEstadoActual(nuevoEstado);
        if (nuevoEstado == Guia.EstadoActual.RECOLECTADA) {
            guia.setFechaRecoleccionReal(LocalDateTime.now());
        }else if (nuevoEstado == Guia.EstadoActual.ENTREGADA) {
            guia.setFechaEntregaReal(LocalDateTime.now());
        }else if (nuevoEstado == Guia.EstadoActual.CANCELADA || nuevoEstado == Guia.EstadoActual.DEVUELTA) {
            guia.setFechaEntregaReal(LocalDateTime.now());
        }
        guiaRepository.save(guia);

        //Registramos el cambio de estado en GuiaEstado
        GuiaEstado nuevoRegistroEstado = new GuiaEstado();
        nuevoRegistroEstado.setGuia(guia);
        nuevoRegistroEstado.setEstadoAnterior(convertirAEstado(estadoAnterior));
        nuevoRegistroEstado.setEstadoNuevo(convertirAEstado(nuevoEstado));
        nuevoRegistroEstado.setUsuarioCambio(repartidor.getEmpleado().getUsuario());
        nuevoRegistroEstado.setComentarios(cambiarEstadoDTO.getComentarios());
        nuevoRegistroEstado.setMotivoCambio(cambiarEstadoDTO.getMotivoCambio());
        nuevoRegistroEstado.setFechaCambio(LocalDateTime.now());
        guiaEstadoRepository.save(nuevoRegistroEstado);

        //Enviar correo al cliente notificando el cambio de estado
        String asunto = "Actualización del estado de su guía: " + guia.getNumeroGuia();
        String mensaje = "Estimado/a " + guia.getCliente().getNombreCompleto() + ",\n\n" +
                "Le informamos que el estado de su guía con número " + guia.getNumeroGuia() + " ha sido actualizado a: " + nuevoEstado.name() + ".\n\n" +
                "Gracias por confiar en nuestros servicios.\n\n" +
                "Atentamente,\n" +
                "El equipo de Entregas SIE";
        emailService.enviarEmailGenerico(guia.getCliente().getEmail(), asunto, mensaje);


        return "Estado de la guia actualizado correctamente";
    }

    private boolean esTransicionValida(Guia.EstadoActual estadoActual, Guia.EstadoActual nuevoEstado) {
        switch (estadoActual) {
            case ASIGNADA:
                return nuevoEstado == Guia.EstadoActual.RECOLECTADA;
            case RECOLECTADA:
                return nuevoEstado == Guia.EstadoActual.EN_TRANSITO || nuevoEstado == Guia.EstadoActual.DEVUELTA;
            case EN_TRANSITO:
                return nuevoEstado == Guia.EstadoActual.EN_REPARTO || nuevoEstado == Guia.EstadoActual.DEVUELTA;
            case EN_REPARTO:
                return nuevoEstado == Guia.EstadoActual.ENTREGADA || nuevoEstado == Guia.EstadoActual.DEVUELTA || nuevoEstado == Guia.EstadoActual.CANCELADA;
            case DEVUELTA:
                return nuevoEstado == Guia.EstadoActual.CANCELADA || nuevoEstado == Guia.EstadoActual.EN_TRANSITO;

            default:
                return false;
        }
    }

    private GuiaEstado.Estado convertirAEstado(Guia.EstadoActual estadoActual) {
        switch (estadoActual) {
            case CREADA:
                return GuiaEstado.Estado.CREADA;
            case ASIGNADA:
                return GuiaEstado.Estado.ASIGNADA;
            case RECOLECTADA:
                return GuiaEstado.Estado.RECOLECTADA;
            case EN_TRANSITO:
                return GuiaEstado.Estado.EN_TRANSITO;
            case EN_REPARTO:
                return GuiaEstado.Estado.EN_REPARTO;
            case ENTREGADA:
                return GuiaEstado.Estado.ENTREGADA;
            case DEVUELTA:
                return GuiaEstado.Estado.DEVUELTA;
            case CANCELADA:
                return GuiaEstado.Estado.CANCELADA;
            default:
                throw new IllegalArgumentException("Estado no valido");
        }
    }

}
