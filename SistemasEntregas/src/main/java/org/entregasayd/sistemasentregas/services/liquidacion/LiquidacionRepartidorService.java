package org.entregasayd.sistemasentregas.services.liquidacion;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LiquidacionRepartidorService {
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

    //Obtener el repartidor con su periodo de liquidacion activo o si no tiene ninguno, obtener el repartidor sin periodo de liquidacion
    public List<RepartidorLiquidacionDTO> obtenerRepartidoresConPeriodoLiquidacionActivo() {
        List<RepartidorLiquidacionDTO> repartidorLiquidacionDTOList = new ArrayList<>();
        //Obtenemos los datos de los repartidores
        for (Repartidor repartidor : repartidorRepository.findAll()) {
           //Obtenemos el periodo de liquidacion activo del repartidor
            LiquidacionRepartidor liquidacionRepartidor = liquidacionRepartidorRepository.findByRepartidor_IdRepartidorAndPeriodoLiquidacion_Estado(repartidor.getIdRepartidor(), PeriodoLiquidacion.EstadoPeriodo.ABIERTO);
            RepartidorLiquidacionDTO.RepartidorDetalleDTO repartidorDetalleDTO = new RepartidorLiquidacionDTO.RepartidorDetalleDTO();
            //Construimos el DTO
            repartidorDetalleDTO.setIdRepartidor(repartidor.getIdRepartidor());
            Persona persona = repartidor.getEmpleado().getUsuario().getPersona();
            repartidorDetalleDTO.setNombreCompleto(persona.getNombre() + " " + persona.getApellido());
            repartidorDetalleDTO.setEmail(persona.getCorreo());
            repartidorDetalleDTO.setTelefono(persona.getTelefono());
            repartidorDetalleDTO.setZonaAsignada(repartidor.getZonaAsignada());
            repartidorDetalleDTO.setRadioCobertura(repartidor.getRadioCoberturaKm());
            repartidorDetalleDTO.setDisponibilidad(repartidor.getDisponibilidad().name());
            repartidorDetalleDTO.setCalificacionPromedio(repartidor.getCalificacionPromedio());
            repartidorDetalleDTO.setTotalEntregasCompletadas(repartidor.getTotalEntregasCompletadas());
            repartidorDetalleDTO.setTotalEntregasFallidas(repartidor.getTotalEntregasFallidas());

            //Detalles del empleado asociado
            DetalleRepartidorDTO.EmpleadoDTO empleadoDTO = new DetalleRepartidorDTO.EmpleadoDTO();
            Empleado empleado = repartidor.getEmpleado();
            empleadoDTO.setIdEmpleado(empleado.getIdEmpleado());
            empleadoDTO.setCodigoEmpleado(empleado.getCodigoEmpleado());
            empleadoDTO.setEstadoEmpleado(empleado.getEstadoEmpleado().name());
            repartidorDetalleDTO.setEmpleadoDetalle(empleadoDTO);

            //Detalles del contrato asociado
            DetalleRepartidorDTO.ContratoDTO contratoDTO = new DetalleRepartidorDTO.ContratoDTO();
            List<Contrato> contrato = contratoRepository.findByEmpleado_IdEmpleado(empleado.getIdEmpleado());
            //Solo deberia haber un contrato activo por empleado
            if (!contrato.isEmpty()) {
                Contrato contratoActivo = null;
                for (Contrato c : contrato) {
                    if (c.getEstadoContrato().name().equals("ACTIVO")) {
                        contratoActivo = c;
                        break;
                    }
                }
                if (contratoActivo != null) {
                    contratoDTO.setIdContrato(contratoActivo.getIdContrato());
                    contratoDTO.setTipoContrato(contratoActivo.getTipoContrato().name());
                    contratoDTO.setFechaInicio(contratoActivo.getFechaInicio().toString());
                    contratoDTO.setFechaFin(contratoActivo.getFechaFin() != null ? contratoActivo.getFechaFin().toString() : "Indefinido");
                    contratoDTO.setEstadoContrato(contratoActivo.getEstadoContrato().name());
                } else {
                    contratoDTO.setIdContrato(null);
                    contratoDTO.setTipoContrato("No tiene contrato activo");
                    contratoDTO.setFechaInicio(null);
                    contratoDTO.setFechaFin(null);
                    contratoDTO.setEstadoContrato("No tiene contrato activo");
                }
            } else {
                contratoDTO.setIdContrato(null);
                contratoDTO.setTipoContrato("No tiene contrato");
                contratoDTO.setFechaInicio(null);
                contratoDTO.setFechaFin(null);
                contratoDTO.setEstadoContrato("No tiene contrato");
            }
            repartidorDetalleDTO.setContratoDetalle(contratoDTO);

            //Periodo de liquidacion
            RepartidorLiquidacionDTO.LiquidacionRepartidorDTO liquidacionRepartidorDTO = new RepartidorLiquidacionDTO.LiquidacionRepartidorDTO();
            if (liquidacionRepartidor != null) {
                liquidacionRepartidorDTO.setIdLiquidacion(liquidacionRepartidor.getIdLiquidacion());
                liquidacionRepartidorDTO.setTotalEntregas(liquidacionRepartidor.getTotalEntregas());
                liquidacionRepartidorDTO.setTotalComisiones(liquidacionRepartidor.getTotalComisiones());
                liquidacionRepartidorDTO.setTotalBonificaciones(liquidacionRepartidor.getTotalBonificaciones());
                liquidacionRepartidorDTO.setTotalDeducciones(liquidacionRepartidor.getTotalDeducciones());
                liquidacionRepartidorDTO.setSubtotal(liquidacionRepartidor.getSubtotal());
                liquidacionRepartidorDTO.setDescuentoIgss(liquidacionRepartidor.getDescuentoIgss());
                liquidacionRepartidorDTO.setDescuentoIsr(liquidacionRepartidor.getDescuentoIsr());
                liquidacionRepartidorDTO.setOtrosDescuentos(liquidacionRepartidor.getOtrosDescuentos());
                liquidacionRepartidorDTO.setTotalDescuentos(liquidacionRepartidor.getTotalDescuentos());
                liquidacionRepartidorDTO.setTotalNeto(liquidacionRepartidor.getTotalNeto());
                liquidacionRepartidorDTO.setMetodoPago(liquidacionRepartidor.getMetodoPago() != null ? liquidacionRepartidor.getMetodoPago().name() : null);
                liquidacionRepartidorDTO.setEstado(liquidacionRepartidor.getEstadoPago().name());
                //Periodo de liquidacion
                RepartidorLiquidacionDTO.PeriodoLiquidacionDTO periodoLiquidacionDTO = new RepartidorLiquidacionDTO.PeriodoLiquidacionDTO();
                PeriodoLiquidacion periodoLiquidacion = liquidacionRepartidor.getPeriodoLiquidacion();
                periodoLiquidacionDTO.setIdPeriodo(periodoLiquidacion.getIdPeriodo());
                periodoLiquidacionDTO.setDescripcion(periodoLiquidacion.getDescripcion());
                periodoLiquidacionDTO.setFechaInicio(periodoLiquidacion.getFechaInicio().toString());
                periodoLiquidacionDTO.setFechaFin(periodoLiquidacion.getFechaFin().toString());
                periodoLiquidacionDTO.setEstado(periodoLiquidacion.getEstado().name());
                periodoLiquidacionDTO.setTotalComisiones(periodoLiquidacion.getTotalComisiones());
                periodoLiquidacionDTO.setTotalBonificaciones(periodoLiquidacion.getTotalBonificaciones());
                periodoLiquidacionDTO.setTotalDeducciones(periodoLiquidacion.getTotalDeducciones());
                periodoLiquidacionDTO.setTotalNeto(periodoLiquidacion.getTotalNeto());
                liquidacionRepartidorDTO.setPeriodoLiquidacion(periodoLiquidacionDTO);
            }
            repartidorDetalleDTO.setLiquidacionRepartidor(liquidacionRepartidorDTO);
            RepartidorLiquidacionDTO repartidorLiquidacionDTO = new RepartidorLiquidacionDTO();
            repartidorLiquidacionDTO.setRepartidor(repartidorDetalleDTO);
            repartidorLiquidacionDTOList.add(repartidorLiquidacionDTO);
        }
        return repartidorLiquidacionDTOList;

    }

    //Crear un nuevo periodo de liquidacion para un repartidor
    @Transactional
    public String crearNuevoPeriodo(NuevoPeriodoLiquidacion nuevoPeriodoLiquidacion) {
        //Revisar si ya existe un periodo de liquidacion abierto
        List<PeriodoLiquidacion> periodoAbierto = periodoLiquidacionRepository.findByEstado(PeriodoLiquidacion.EstadoPeriodo.ABIERTO);
        if (!periodoAbierto.isEmpty()) {
            throw new RuntimeException("Ya existe un periodo de liquidacion abierto. No se puede crear un nuevo periodo hasta que el actual sea cerrado.");
        }

        //Creamos el nuevo periodo de liquidacion
        PeriodoLiquidacion periodoLiquidacion = new PeriodoLiquidacion();
        periodoLiquidacion.setDescripcion(nuevoPeriodoLiquidacion.getDescripcion());
        LocalDate fechaInicio = LocalDate.parse(nuevoPeriodoLiquidacion.getFechaInicio());
        LocalDate fechaFin = LocalDate.parse(nuevoPeriodoLiquidacion.getFechaFin());
        if (fechaFin.isBefore(fechaInicio)) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        periodoLiquidacion.setFechaInicio(fechaInicio);
        periodoLiquidacion.setFechaFin(fechaFin);
        periodoLiquidacion.setEstado(PeriodoLiquidacion.EstadoPeriodo.ABIERTO);
        periodoLiquidacion.setTotalComisiones(0.00);
        periodoLiquidacion.setTotalBonificaciones(0.00);
        periodoLiquidacion.setTotalDeducciones(0.00);
        periodoLiquidacion.setTotalNeto(0.00);
        periodoLiquidacionRepository.save(periodoLiquidacion);

        return "Nuevo periodo de liquidacion creado exitosamente.";
    }

    //Agregar repartidor a un periodo de liquidacion
    @Transactional
    public String agregarRepartidorAPeriodo(AgregarPeriodoRepartidorDTO agregarRepartidorPeriodoDTO) {
        //Validamos que el repartidor exista
        Repartidor repartidor = repartidorRepository.findById(agregarRepartidorPeriodoDTO.getIdRepartidor()).orElseThrow(() -> new RuntimeException("Repartidor no encontrado."));
        //Validamos que el periodo de liquidacion exista y este abierto
        PeriodoLiquidacion periodoLiquidacion = periodoLiquidacionRepository.findById(agregarRepartidorPeriodoDTO.getIdPeriodoLiquidacion()).orElseThrow(() -> new RuntimeException("Periodo de liquidacion no encontrado."));
        if (periodoLiquidacion.getEstado() != PeriodoLiquidacion.EstadoPeriodo.ABIERTO) {
            throw new RuntimeException("El periodo de liquidacion no esta abierto.");
        }
        //Validamos que el repartidor no tenga un periodo de liquidacion activo
        LiquidacionRepartidor liquidacionExistente = liquidacionRepartidorRepository.findByRepartidor_IdRepartidorAndPeriodoLiquidacion_Estado(repartidor.getIdRepartidor(), PeriodoLiquidacion.EstadoPeriodo.ABIERTO);
        if (liquidacionExistente != null) {
            throw new RuntimeException("El repartidor ya tiene un periodo de liquidacion activo.");
        }

        //Creamos la liquidacion del repartidor para el periodo de liquidacion
        LiquidacionRepartidor liquidacionRepartidor = new LiquidacionRepartidor();
        liquidacionRepartidor.setRepartidor(repartidor);
        liquidacionRepartidor.setPeriodoLiquidacion(periodoLiquidacion);
        liquidacionRepartidor.setTotalEntregas(0);
        liquidacionRepartidor.setTotalComisiones(0.00);
        liquidacionRepartidor.setTotalBonificaciones(0.00);
        liquidacionRepartidor.setTotalDeducciones(0.00);
        liquidacionRepartidor.setSubtotal(0.00);
        liquidacionRepartidor.setDescuentoIgss(0.00);
        liquidacionRepartidor.setDescuentoIsr(0.00);
        liquidacionRepartidor.setOtrosDescuentos(0.00);
        liquidacionRepartidor.setTotalDescuentos(0.00);
        liquidacionRepartidor.setTotalNeto(0.00);
        liquidacionRepartidor.setEstadoPago(LiquidacionRepartidor.EstadoPago.PENDIENTE);
        liquidacionRepartidorRepository.save(liquidacionRepartidor);

        return "Repartidor agregado al periodo de liquidacion exitosamente.";
    }

    //Obtener periodo de liquidacion activo
    public RepartidorLiquidacionDTO.PeriodoLiquidacionDTO obtenerPeriodosLiquidacion() {
        RepartidorLiquidacionDTO.PeriodoLiquidacionDTO periodoLiquidacionDTO = new RepartidorLiquidacionDTO.PeriodoLiquidacionDTO();
        List<PeriodoLiquidacion> periodos = periodoLiquidacionRepository.findAll();
        //Solo debe de haber un periodo abierto

        for (PeriodoLiquidacion periodo : periodos) {
            if (periodo.getEstado() == PeriodoLiquidacion.EstadoPeriodo.ABIERTO) {
                periodoLiquidacionDTO.setIdPeriodo(periodo.getIdPeriodo());
                periodoLiquidacionDTO.setDescripcion(periodo.getDescripcion());
                periodoLiquidacionDTO.setFechaInicio(periodo.getFechaInicio().toString());
                periodoLiquidacionDTO.setFechaFin(periodo.getFechaFin().toString());
                periodoLiquidacionDTO.setEstado(periodo.getEstado().name());
                periodoLiquidacionDTO.setTotalComisiones(periodo.getTotalComisiones());
                periodoLiquidacionDTO.setTotalBonificaciones(periodo.getTotalBonificaciones());
                periodoLiquidacionDTO.setTotalDeducciones(periodo.getTotalDeducciones());
                periodoLiquidacionDTO.setTotalNeto(periodo.getTotalNeto());
                return periodoLiquidacionDTO;
            }
        }
        throw new RuntimeException("No hay ningun periodo de liquidacion abierto.");
    }

    //Cerrar periodo de liquidacion
    @Transactional
    public String cerrarPeriodoLiquidacion(Long idPeriodo) {
        //Validamos que el periodo de liquidacion exista y este abierto
        PeriodoLiquidacion periodoLiquidacion = periodoLiquidacionRepository.findById(idPeriodo).orElseThrow(() -> new RuntimeException("Periodo de liquidacion no encontrado."));
        if (periodoLiquidacion.getEstado() != PeriodoLiquidacion.EstadoPeriodo.ABIERTO) {
            throw new RuntimeException("El periodo de liquidacion no esta abierto.");
        }
        //Obtenemos todas las liquidaciones de los repartidores en el periodo
        List<LiquidacionRepartidor> liquidaciones = liquidacionRepartidorRepository.findByPeriodoLiquidacion_IdPeriodo(idPeriodo);
        if (liquidaciones.isEmpty()) {
            throw new RuntimeException("No hay repartidores asociados a este periodo de liquidacion.");
        }

        //Cerramos las liquidaciones de los repartidores
        for (LiquidacionRepartidor liquidacion : liquidaciones) {
            //Si el repartidor no tiene entregas, no se puede cerrar la liquidacion
            /*if (liquidacion.getTotalEntregas() == 0) {
                throw new RuntimeException("El repartidor " + liquidacion.getRepartidor().getEmpleado().getUsuario().getPersona().getNombre() + " " + liquidacion.getRepartidor().getEmpleado().getUsuario().getPersona().getApellido() + " no tiene entregas en este periodo de liquidacion. No se puede cerrar el periodo.");
            }*/

            //Verificamos que los repartidores no tengan asignaciones pendientes en el periodo
            List<AsignacionRepartidor> asignacionesPendientes = asignacionRepartidorRepository.findByRepartidor_IdRepartidor(liquidacion.getRepartidor().getIdRepartidor());
            for (AsignacionRepartidor asignacion : asignacionesPendientes) {
                if (asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.PENDIENTE ||
                        asignacion.getEstadoAsignacion() == AsignacionRepartidor.EstadoAsignacion.ACEPTADA) {
                    throw new RuntimeException("El repartidor " + liquidacion.getRepartidor().getEmpleado().getUsuario().getPersona().getNombre() + " " + liquidacion.getRepartidor().getEmpleado().getUsuario().getPersona().getApellido() + " tiene asignaciones pendientes. No se puede cerrar el periodo.");
                }
            }

            //Calculamos el subtotal
            double subtotal = liquidacion.getTotalComisiones() + liquidacion.getTotalBonificaciones() - liquidacion.getTotalDeducciones();
            liquidacion.setSubtotal(subtotal);
            //Calculamos los descuentos
            //Descuento IGSS (4.83% del subtotal)
            double descuentoIgss = subtotal * 0.0483;
            liquidacion.setDescuentoIgss(descuentoIgss);
            //Descuento ISR (dependiendo del subtotal)
            double descuentoIsr = 0.00;
            if (subtotal > 5000 && subtotal <= 10000) {
                descuentoIsr = subtotal * 0.05;
            } else if (subtotal > 10000 && subtotal <= 20000) {
                descuentoIsr = subtotal * 0.10;
            } else if (subtotal > 20000) {
                descuentoIsr = subtotal * 0.15;
            }
            liquidacion.setDescuentoIsr(descuentoIsr);
            //Otros descuentos (si los hay)
            double otrosDescuentos = liquidacion.getOtrosDescuentos();
            liquidacion.setOtrosDescuentos(otrosDescuentos);
            //Total descuentos
            double totalDescuentos = descuentoIgss + descuentoIsr + otrosDescuentos;
            liquidacion.setTotalDescuentos(totalDescuentos);
            //Total neto
            double totalNeto = subtotal - totalDescuentos;
            liquidacion.setTotalNeto(totalNeto);
            //Cambiamos el estado de la liquidacion a LISTA
            liquidacion.setEstadoPago(LiquidacionRepartidor.EstadoPago.PENDIENTE);
            liquidacionRepartidorRepository.save(liquidacion);
        }

        //Cerramos el periodo de liquidacion
        periodoLiquidacion.setEstado(PeriodoLiquidacion.EstadoPeriodo.CERRADO);
        periodoLiquidacionRepository.save(periodoLiquidacion);
        return "Periodo de liquidacion cerrado exitosamente.";
    }

}
