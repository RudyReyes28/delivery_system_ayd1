package org.entregasayd.sistemasentregas.services.coordinador;

import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;
import org.entregasayd.sistemasentregas.dto.guias.GuiaDetalleClienteDTO;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.coordinador.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GuiasCoordinadorService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private SucursalPersonalRepository sucursalPersonalRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private DireccionEntregaRepository direccionEntregaRepository;
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

    //Obtener todas las guias
    public ArrayList<DetalleGuiaOperacionesDTO> getTodasLasGuias() {
        ArrayList<Guia> guias = (ArrayList<Guia>) guiaRepository.findAll();
        return detalleGuias(guias);
    }

    //Obtener guias con repartidor null
    public ArrayList<DetalleGuiaOperacionesDTO> getGuiasSinRepartidor() {
        ArrayList<Guia> guias = (ArrayList<Guia>) guiaRepository.findByRepartidorIsNull();
        return detalleGuias(guias);
    }

    //Detalles del repartidor
    public ArrayList<DetalleRepartidorDTO> getDetallesRepartidores() {
        ArrayList<Repartidor> repartidores = (ArrayList<Repartidor>) repartidorRepository.findAll();
        ArrayList<DetalleRepartidorDTO> detallesRepartidores = new ArrayList<>();

        for (Repartidor repartidor : repartidores) {
            DetalleRepartidorDTO detalleRepartidorDTO = new DetalleRepartidorDTO();
            DetalleRepartidorDTO.repartidorDetalleDTO repartidorDetalleDTO = new DetalleRepartidorDTO.repartidorDetalleDTO();

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
            //Obtener todas las guias asiganas al repartidor
            ArrayList<Guia> guiasAsignadas = (ArrayList<Guia>) guiaRepository.findByRepartidor_IdRepartidor(repartidor.getIdRepartidor());
            ArrayList<DetalleRepartidorDTO.GuiasAsignadasDTO> guiasAsignadasDTO = new ArrayList<>();
            for (Guia guia : guiasAsignadas) {
                DetalleRepartidorDTO.GuiasAsignadasDTO guiaDTO = new DetalleRepartidorDTO.GuiasAsignadasDTO();
                guiaDTO.setIdGuia(guia.getIdGuia());
                guiaDTO.setNumeroGuia(guia.getNumeroGuia());
                guiaDTO.setDescripcionContenido(guia.getDescripcionContenido());
                guiaDTO.setFechaCreacion(guia.getFechaCreacion().toString());
                guiaDTO.setEstadoActual(guia.getEstadoActual().name());
                guiasAsignadasDTO.add(guiaDTO);
            }

            repartidorDetalleDTO.setContratoDetalle(contratoDTO);
            repartidorDetalleDTO.setGuiasAsignadas(guiasAsignadasDTO);
            detalleRepartidorDTO.setRepartidor(repartidorDetalleDTO);
            detallesRepartidores.add(detalleRepartidorDTO);


        }
        return detallesRepartidores;
    }

    //Obtener repartidores con contratos activos
    public ArrayList<DetalleRepartidorDTO.repartidorDetalleDTO> getRepartidoresConContratoActivo() {
        ArrayList<Repartidor> repartidoresConContratoActivo = new ArrayList<>();
        ArrayList<Repartidor> todosLosRepartidores = (ArrayList<Repartidor>) repartidorRepository.findAll();
        for (Repartidor repartidor : todosLosRepartidores) {
            List<Contrato> contratos = contratoRepository.findByEmpleado_IdEmpleado(repartidor.getEmpleado().getIdEmpleado());
            for (Contrato contrato : contratos) {
                if (contrato.getEstadoContrato().name().equals("ACTIVO")) {
                    repartidoresConContratoActivo.add(repartidor);
                    break; // Salir del bucle interno si se encuentra un contrato activo
                }
            }
        }
        ArrayList<DetalleRepartidorDTO.repartidorDetalleDTO> detallesRepartidores = new ArrayList<>();
        for (Repartidor repartidor : repartidoresConContratoActivo) {
            DetalleRepartidorDTO.repartidorDetalleDTO repartidorDetalleDTO = new DetalleRepartidorDTO.repartidorDetalleDTO();
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
            detallesRepartidores.add(repartidorDetalleDTO);
        }
        return detallesRepartidores;
    }

    //Asignar repartidor a guia
    @Transactional
    public String asignarRepartidorAGuia(AsignacionGuiaDTO asignacionGuiaDTO) {
        Guia guia = guiaRepository.findById(asignacionGuiaDTO.getIdGuia()).orElse(null);
        if (guia == null) {
            return "La guia con id " + asignacionGuiaDTO.getIdGuia() + " no existe";
        }
        Repartidor repartidor = repartidorRepository.findById(asignacionGuiaDTO.getIdRepartidor()).orElse(null);
        if (repartidor == null) {
            return "El repartidor con id " + asignacionGuiaDTO.getIdRepartidor() + " no existe";
        }
        //Verificar que el repartidor tenga un contrato activo
        List<Contrato> contratos = contratoRepository.findByEmpleado_IdEmpleado(repartidor.getEmpleado().getIdEmpleado());
        boolean tieneContratoActivo = false;
        for (Contrato contrato : contratos) {
            if (contrato.getEstadoContrato().name().equals("ACTIVO")) {
                tieneContratoActivo = true;
                break;
            }
        }
        if (!tieneContratoActivo) {
            return "El repartidor con id " + asignacionGuiaDTO.getIdRepartidor() + " no tiene un contrato activo";
        }
        //Verificar que la guia no tenga repartidor asignado
        if (guia.getRepartidor() != null) {
            return "La guia con id " + asignacionGuiaDTO.getIdGuia() + " ya tiene un repartidor asignado";
        }

        //Verificar que no haya un asignacion pendiente para la guia
        List<AsignacionRepartidor> asignacionesPendientes = asignacionRepartidorRepository.findByGuia_IdGuiaAndEstadoAsignacion(asignacionGuiaDTO.getIdGuia(), AsignacionRepartidor.EstadoAsignacion.PENDIENTE);
        if (!asignacionesPendientes.isEmpty()) {
            return "La guia con id " + asignacionGuiaDTO.getIdGuia() + " ya tiene una asignación pendiente";
        }

        //Asignar el repartidor a la guia
        guia.setRepartidor(repartidor);
        String fechaEntrega = asignacionGuiaDTO.getFechaEntrega();
        LocalDate fecha = LocalDate.parse(fechaEntrega);
        guia.setFechaEstimadaEntrega(fecha.atStartOfDay());
        Guia guiaSalvada = guiaRepository.save(guia);

        //Crear la asignacion de repartidor
        AsignacionRepartidor asignacionRepartidor = new AsignacionRepartidor();
        asignacionRepartidor.setGuia(guiaSalvada);
        asignacionRepartidor.setRepartidor(repartidor);
        asignacionRepartidor.setFechaAsignacion(LocalDateTime.now());
        asignacionRepartidor.setTipoAsignacion(AsignacionRepartidor.TipoAsignacion.MANUAL);
        asignacionRepartidor.setEstadoAsignacion(AsignacionRepartidor.EstadoAsignacion.PENDIENTE);
        asignacionRepartidorRepository.save(asignacionRepartidor);


        //Enviar email al repartidor
        Persona personaRepartidor = repartidor.getEmpleado().getUsuario().getPersona();
        String emailRepartidor = personaRepartidor.getCorreo();
        String nombreRepartidor = personaRepartidor.getNombre() + " " + personaRepartidor.getApellido();
        String asunto = "Nueva asignación de guía - " + guia.getNumeroGuia();
        String mensaje = "Hola " + nombreRepartidor + ",\n\n" +
                "Se te ha asignado una nueva guía para entrega.\n\n" +
                "Detalles de la guía:\n" +
                "Número de Guía: " + guia.getNumeroGuia() + "\n" +
                "Descripción del Contenido: " + guia.getDescripcionContenido() + "\n" +
                "Valor Declarado: $" + guia.getValorDeclarado() + "\n" +
                "Peso (kg): " + guia.getPesoKg() + "\n" +
                "Dimensiones: " + guia.getDimensiones() + "\n" +
                "Observaciones: " + guia.getObservaciones() + "\n" +
                "Fecha de Creación: " + guia.getFechaCreacion().toString() + "\n" +
                "Prioridad: " + guia.getPrioridad().name() + "\n" +
                "Total: $" + guia.getTotal() + "\n" +
                "Estado Actual: " + guia.getEstadoActual().name() + "\n\n" +
                "Por favor, asegúrate de revisar los detalles y aceptar la asignacion.\n\n" +
                "Gracias por tu compromiso y dedicación.\n\n" +
                "Atentamente,\n" +
                "El equipo de Gestión de Entregas";
        emailService.enviarEmailAsignacionRepartidor(emailRepartidor, asunto, mensaje);
        return "Repartidor asignado correctamente a la guía y notificado por email.";
    }

    //Metodo auxiliar para el detalle guia
    private ArrayList<DetalleGuiaOperacionesDTO> detalleGuias(ArrayList<Guia> guias) {
        ArrayList<DetalleGuiaOperacionesDTO> detalleGuias = new ArrayList<>();

        for (Guia guia : guias) {
            DetalleGuiaOperacionesDTO detalleGuia = new DetalleGuiaOperacionesDTO();
            DetalleGuiaOperacionesDTO.GuiaCoordinadorDTO guiaCoordinadorDTO = new DetalleGuiaOperacionesDTO.GuiaCoordinadorDTO();

            guiaCoordinadorDTO.setIdGuia(guia.getIdGuia());
            guiaCoordinadorDTO.setNumeroGuia(guia.getNumeroGuia());
            guiaCoordinadorDTO.setCodigoInterno(guia.getCodigoInterno());
            guiaCoordinadorDTO.setDescripcionContenido(guia.getDescripcionContenido());
            guiaCoordinadorDTO.setValorDeclarado(guia.getValorDeclarado());
            guiaCoordinadorDTO.setPesoKg(guia.getPesoKg());
            guiaCoordinadorDTO.setDimensiones(guia.getDimensiones());
            guiaCoordinadorDTO.setObservaciones(guia.getObservaciones());
            guiaCoordinadorDTO.setFechaCreacion(guia.getFechaCreacion().toString());
            guiaCoordinadorDTO.setFechaRecoleccionReal(guia.getFechaRecoleccionReal() != null ? guia.getFechaRecoleccionReal().toString() : null);
            guiaCoordinadorDTO.setPrioridad(guia.getPrioridad().name());
            guiaCoordinadorDTO.setIntentosEntrega(guia.getIntentosEntrega());
            guiaCoordinadorDTO.setTotal(guia.getTotal());
            guiaCoordinadorDTO.setEstadoActual(guia.getEstadoActual().name());

            //Detalles del cliente
            Cliente cliente = clienteRepository.findById(guia.getCliente().getIdCliente()).orElse(null);
            if (cliente != null) {
                GetDetalleGuiaDTO.ClienteDTO clienteDTO = new GetDetalleGuiaDTO.ClienteDTO();
                Cliente clienteGuia = guia.getCliente();
                clienteDTO.setIdCliente(clienteGuia.getIdCliente());
                clienteDTO.setNombreCompleto(clienteGuia.getNombreCompleto());
                clienteDTO.setEmail(clienteGuia.getEmail());
                clienteDTO.setTelefono(clienteGuia.getTelefono());
                guiaCoordinadorDTO.setCliente(clienteDTO);
            }
            //Detalles de la direccion de entrega
            Direccion direccionEntrega = guia.getDireccionEntrega();
            if (direccionEntrega != null) {
                GetDetalleGuiaDTO.DireccionEntregaDTO direccionEntregaDTO = new GetDetalleGuiaDTO.DireccionEntregaDTO();
                direccionEntregaDTO.setIdDireccion(direccionEntrega.getIdDireccion());
                direccionEntregaDTO.setMunicipio(direccionEntrega.getMunicipio());
                direccionEntregaDTO.setDepartamento(direccionEntrega.getDepartamento());
                direccionEntregaDTO.setReferencias(direccionEntrega.getReferencias());
                direccionEntregaDTO.setCodigoPostal(direccionEntrega.getCodigoPostal());
                direccionEntregaDTO.setPais(direccionEntrega.getPais());
                guiaCoordinadorDTO.setDireccionEntrega(direccionEntregaDTO);
            }
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

            guiaCoordinadorDTO.setEstadosEntregas(estadosDTO);

            //Detalles del repartidor asignado
            if (guia.getRepartidor() != null) {
                GuiaDetalleClienteDTO.RepartidorDTO repartidorDTO = new GuiaDetalleClienteDTO.RepartidorDTO();
                Repartidor repartidor = guia.getRepartidor();
                Persona usuarioRepartidor = repartidor.getEmpleado().getUsuario().getPersona();
                repartidorDTO.setIdRepartidor(repartidor.getIdRepartidor());
                repartidorDTO.setNombreCompleto(usuarioRepartidor.getNombre() + " " + usuarioRepartidor.getApellido());
                repartidorDTO.setEmail(usuarioRepartidor.getCorreo());
                repartidorDTO.setTelefono(usuarioRepartidor.getTelefono());
                guiaCoordinadorDTO.setRepartidor(repartidorDTO);
            } else {
                guiaCoordinadorDTO.setRepartidor(null);
            }

            detalleGuia.setGuia(guiaCoordinadorDTO);
            detalleGuias.add(detalleGuia);
        }
        return detalleGuias;
    }

}
