package org.entregasayd.sistemasentregas.services.cancelaciones;

import org.entregasayd.sistemasentregas.dto.coordinador.DetalleGuiaOperacionesDTO;
import org.entregasayd.sistemasentregas.dto.coordinador.DetalleRepartidorDTO;
import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;
import org.entregasayd.sistemasentregas.dto.guias.GuiaDetalleClienteDTO;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.liquidacion.*;
import org.entregasayd.sistemasentregas.dto.cancelaciones.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CancelacionesCoordinadorService {
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
    @Autowired
    private PeriodoLiquidacionRepository periodoLiquidacionRepository;
    @Autowired
    private LiquidacionRepartidorRepository liquidacionRepartidorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ContratoComisionRepository contratoComisionRepository;
    @Autowired
    private CancelacionGuiaRepository cancelacionGuiaRepository;
    @Autowired
    private SucursalPersonalRepository SucursalPersonalRepository;
    @Autowired
    private EmpresaFidelizacionRepository EmpresaFidelizacionRepository;


    //Obtener todas las solicitudes de cancelacion
    public List<RegistroCancelacionesDTO> obtenerSolicitudesCancelacion() {
        List<CancelacionGuia> cancelaciones = cancelacionGuiaRepository.findAll();
        List<RegistroCancelacionesDTO> resultado = new ArrayList<>();

        for (CancelacionGuia cancelacion : cancelaciones) {
            RegistroCancelacionesDTO dto = new RegistroCancelacionesDTO();
            dto.setIdCancelacion(cancelacion.getIdCancelacion());
            dto.setMotivoCategoria(cancelacion.getMotivoCategoria().name());
            dto.setMotivoDetalle(cancelacion.getMotivoDetalle());
            dto.setFechaCancelacion(cancelacion.getFechaCancelacion().toString());
            dto.setEstadoCancelacion(cancelacion.getEstadoCancelacion().name());

            Guia guia = cancelacion.getGuia();
            RegistroCancelacionesDTO.GuiaDetalleCancelacionDTO guiaDTO = new RegistroCancelacionesDTO.GuiaDetalleCancelacionDTO();
            guiaDTO.setIdGuia(guia.getIdGuia());
            guiaDTO.setNumeroGuia(guia.getNumeroGuia());
            guiaDTO.setCodigoInterno(guia.getCodigoInterno());
            guiaDTO.setDescripcionContenido(guia.getDescripcionContenido());
            guiaDTO.setValorDeclarado(guia.getValorDeclarado());
            guiaDTO.setPesoKg(guia.getPesoKg());
            guiaDTO.setDimensiones(guia.getDimensiones());
            guiaDTO.setObservaciones(guia.getObservaciones());
            guiaDTO.setFechaCreacion(guia.getFechaCreacion().toString());
            guiaDTO.setFechaRecoleccionReal(guia.getFechaRecoleccionReal() != null ? guia.getFechaRecoleccionReal().toString() : null);
            guiaDTO.setPrioridad(guia.getPrioridad().name());
            guiaDTO.setIntentosEntrega(guia.getIntentosEntrega());
            guiaDTO.setTotal(guia.getTotal());
            guiaDTO.setEstadoActual(guia.getEstadoActual().name());
            dto.setGuia(guiaDTO);
            resultado.add(dto);
        }

        return resultado;
    }

    //Autorizar solicitud de cancelacion
    @Transactional
    public String autorizarSolicitudCancelacion(AutorizacionCancelacionDTO autorizacionCancelacionDTO) throws Exception {
        CancelacionGuia cancelacion = cancelacionGuiaRepository.findById(autorizacionCancelacionDTO.getIdCancelacion()).orElseThrow(() -> new Exception("Cancelacion no encontrada"));
        if (cancelacion.getEstadoCancelacion() != CancelacionGuia.EstadoCancelacion.SOLICITADA) {
            throw new Exception("La cancelacion ya ha sido procesada");
        }

        //Obtenemos el calculo de la comision y penalizacion
        double montoPenalizacion = cancelacion.getMontoPenalizacion();
        double montoComisionPagar = cancelacion.getMontoComisionPagar();
        boolean aplicaPenalizacion = cancelacion.getAplicaPenalizacion();

        //Si aplica penalizacion es porque el repartidor ya estaba asignado a la guia
        if (aplicaPenalizacion) {
            //Hay que volver a recalcular la comision del repartidor por los niveles de fidelizacion
            Guia guia = cancelacion.getGuia();
            //Obtener nivel de fidelizacion de la empresa
            EmpresaFidelizacion empresaFidelizacion = EmpresaFidelizacionRepository.findByEmpresaIdEmpresa(guia.getEmpresa().getIdEmpresa());
            NivelFindelizacion nivelFidelizacion = empresaFidelizacion.getNivelFidelizacion();
            if (nivelFidelizacion.getCodigoNivel().equals("DIAMANTE")) {
                if (nivelFidelizacion.getCancelacionesGratuitasMes() < empresaFidelizacion.getTotalCancelaciones()) {
                    montoComisionPagar = montoComisionPagar * (nivelFidelizacion.getPorcentajePenalizacion() / 100);
                }
                //Aumentamos en 1 las cancelaciones del mes
                empresaFidelizacion.setTotalCancelaciones(empresaFidelizacion.getTotalCancelaciones() + 1);
                EmpresaFidelizacionRepository.save(empresaFidelizacion);
            } else if (nivelFidelizacion.getCodigoNivel().equals("ORO")) {
                montoComisionPagar = montoComisionPagar * (nivelFidelizacion.getPorcentajePenalizacion() / 100);
            } else if (nivelFidelizacion.getCodigoNivel().equals("PLATA")) {
                montoComisionPagar = montoComisionPagar * (nivelFidelizacion.getPorcentajePenalizacion() / 100);
            }

            Repartidor repartidor = guia.getRepartidor();
            // Buscamos el periodo de liquidacion activo
            LiquidacionRepartidor liquidacion = liquidacionRepartidorRepository.findByRepartidor_IdRepartidorAndPeriodoLiquidacion_Estado(repartidor.getIdRepartidor(), PeriodoLiquidacion.EstadoPeriodo.ABIERTO);
            //Sumale las comisiones y el total neto y subtotal
            liquidacion.setTotalComisiones(liquidacion.getTotalComisiones() + montoComisionPagar);
            liquidacion.setSubtotal(liquidacion.getSubtotal() + montoComisionPagar);
            liquidacion.setTotalNeto(liquidacion.getTotalNeto() + montoComisionPagar);
            liquidacionRepartidorRepository.save(liquidacion);

            //Sumar en empresa fidelizacion las penalizaciones aplicadas
            empresaFidelizacion.setPenalizacionesAplicadas(empresaFidelizacion.getPenalizacionesAplicadas() + montoPenalizacion);
            EmpresaFidelizacionRepository.save(empresaFidelizacion);

            //Sumar en PeriodoLiquidacion
            PeriodoLiquidacion periodo = liquidacion.getPeriodoLiquidacion();
            periodo.setTotalComisiones(periodo.getTotalComisiones() + montoComisionPagar);
            periodo.setTotalNeto(periodo.getTotalNeto() + montoComisionPagar);
            periodoLiquidacionRepository.save(periodo);

            //Actualizamos el total de comision en la solicitud de cancelacion
            cancelacion.setMontoComisionPagar(montoComisionPagar);


        }
        //ACTUALIZAMOS TODOS LOS ESTADOS
        cancelacion.setEstadoCancelacion(CancelacionGuia.EstadoCancelacion.PROCESADA);
        cancelacionGuiaRepository.save(cancelacion);

        //Actualizamos la guia
        Guia guia = cancelacion.getGuia();

        //Creamos un nuevo estado de guia
        GuiaEstado nuevoEstado = new GuiaEstado();
        nuevoEstado.setGuia(guia);
        nuevoEstado.setEstadoAnterior(GuiaEstado.Estado.valueOf(guia.getEstadoActual().toString()));
        nuevoEstado.setEstadoNuevo(GuiaEstado.Estado.CANCELADA);
        Usuario usuarioCambio = usuarioRepository.findById(autorizacionCancelacionDTO.getIdUsuario()).orElseThrow(() -> new Exception("Usuario no encontrado"));
        nuevoEstado.setUsuarioCambio(usuarioCambio);
        nuevoEstado.setComentarios("Cancelacion autorizada por coordinador");
        nuevoEstado.setMotivoCambio("Cancelacion de guia ID: " + guia.getIdGuia() + " por motivo: " + cancelacion.getMotivoCategoria().name() + " - " + cancelacion.getMotivoDetalle());
        nuevoEstado.setFechaCambio(LocalDateTime.now());
        guiaEstadoRepository.save(nuevoEstado);

        guia.setEstadoActual(Guia.EstadoActual.CANCELADA);
        guiaRepository.save(guia);

        List<AsignacionRepartidor> asignacionRepartidor = asignacionRepartidorRepository.findByRepartidor_IdRepartidorAndGuia_IdGuia(guia.getRepartidor().getIdRepartidor(), guia.getIdGuia());
        for (AsignacionRepartidor asignacion : asignacionRepartidor) {
            if (asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.PENDIENTE || asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.ACEPTADA) {
                asignacion.setEstadoAsignacion(AsignacionRepartidor.EstadoAsignacion.CANCELADA);
                asignacionRepartidorRepository.save(asignacion);
            }
        }

        //Enviar correo al cliente
        Cliente cliente = guia.getCliente();
        String asunto = "Notificación de Cancelación de Guía - " + guia.getNumeroGuia();
        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append("Estimado/a ").append(cliente.getNombreCompleto()).append(",\n\n");
        cuerpo.append("Le informamos que su guía con número ").append(guia.getNumeroGuia()).append(" ha sido cancelada.\n");
        cuerpo.append("Motivo de la cancelación: ").append(cancelacion.getMotivoCategoria().name()).append(" - ").append(cancelacion.getMotivoDetalle()).append("\n\n");
        cuerpo.append("Si tiene alguna pregunta o necesita más información, no dude en contactarnos.\n\n");
        cuerpo.append("Atentamente,\n");
        cuerpo.append("El equipo de Soporte\n");

        emailService.enviarEmailGenerico(cliente.getEmail(), asunto, cuerpo.toString());
        return "Cancelación autorizada y procesada correctamente.";
    }

    //Rechazar solicitud de cancelacion
    @Transactional
    public String rechazarSolicitudCancelacion(Long idCancelacion) throws Exception {
        CancelacionGuia cancelacion = cancelacionGuiaRepository.findById(idCancelacion).orElseThrow(() -> new Exception("Cancelacion no encontrada"));
        if (cancelacion.getEstadoCancelacion() != CancelacionGuia.EstadoCancelacion.SOLICITADA) {
            throw new Exception("La cancelacion ya ha sido procesada");
        }

        cancelacion.setEstadoCancelacion(CancelacionGuia.EstadoCancelacion.RECHAZADA);
        cancelacionGuiaRepository.save(cancelacion);

        return "Cancelación rechazada correctamente.";
    }

}
