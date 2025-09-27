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
public class CancelacionSucursalService {

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

    //Obtener las guias con estados CREADA o ASIGNADA para cancelaciones
    public List<DetalleGuiaOperacionesDTO> obtenerGuiasParaCancelacion(Long idUsuario) {
        //Obtener la sucursal del usuario
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new RuntimeException("El usuario no existe.");
        }

        SucursalPersonal sucursalPersonal = SucursalPersonalRepository.findByUsuarioIdUsuario(usuario.getIdUsuario());

        ArrayList<DetalleGuiaOperacionesDTO> detalleGuias = new ArrayList<>();
        List<Guia> guias = guiaRepository.findBySucursalOrigen_IdSucursalAndEstadoActualInOrderByFechaCreacionDesc(
                sucursalPersonal.getSucursal().getIdSucursal(),
                List.of(Guia.EstadoActual.CREADA, Guia.EstadoActual.ASIGNADA)
        );

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

    //CANCELAR GUIA
    @Transactional
    public String cancelarGuia(CancelacionGuiaDTO cancelacionGuiaDTO) {
        //Buscar la guia
        Guia guia = guiaRepository.findById(cancelacionGuiaDTO.getIdGuia()).orElse(null);
        if (guia == null) {
            return "La guía no existe.";
        }
        //Verificar que la guia este en estado CREADA o ASIGNADA
        if (!(guia.getEstadoActual() == Guia.EstadoActual.CREADA || guia.getEstadoActual() == Guia.EstadoActual.ASIGNADA)) {
            return "La guía no puede ser cancelada en su estado actual.";
        }
        //Buscar el usuario que realiza la cancelacion
        Usuario empleado = usuarioRepository.findById(cancelacionGuiaDTO.getIdUsuario()).orElse(null);
        if (empleado == null) {
            return "El usuario que realiza la cancelación no existe.";
        }

        //Verificar que la guia no tenga una cancelacion previa
        List<CancelacionGuia> cancelacionExistente = cancelacionGuiaRepository.findByGuia_IdGuiaAndEstadoCancelacion(guia.getIdGuia(), CancelacionGuia.EstadoCancelacion.SOLICITADA);

        if (!cancelacionExistente.isEmpty()) {
            return "La guía ya tiene una solicitud de cancelación pendiente.";
        }

        boolean aplicaPenalizacion = false;
        //verificar si la guia esta como CREADA


        //Si aplica penalizacion hay que calcular el monto penalizacion
        double montoPenalizacion = 0.00;
        boolean pagarComisionRepartidor = false;

        //Verificar si la guia esta ASIGNADA
        if (guia.getEstadoActual() == Guia.EstadoActual.ASIGNADA) {
            //Obtenemos el tipo de servicio para saber el precio base
            TipoServicio tipoServicio = tipoServicioRepository.findById(guia.getTipoServicio().getIdTipoServicio()).orElse(null);
            //Obtener la comision del repartidor en COMISION_REPARTIDOR
            Repartidor repartidor = repartidorRepository.findById(guia.getRepartidor().getIdRepartidor()).orElse(null);
            ContratoComision contratoComision = null;
            double precioBase = 0.00;
            double comisionRepartidor = 0.00;
            if (repartidor != null) {
                //Obtener el contrato activo del repartidor
                Contrato contrato = contratoRepository.findByEmpleado_IdEmpleadoAndEstadoContrato((repartidor.getEmpleado().getIdEmpleado()), Contrato.EstadoContrato.ACTIVO);
                if (contrato != null) {
                    contratoComision = contratoComisionRepository.findByContrato_IdContrato(contrato.getIdContrato());
                    comisionRepartidor = contratoComision != null ? contratoComision.getPorcentaje() : 0.00;
                }
            }

            if (tipoServicio != null) {
                precioBase = tipoServicio.getPrecioBase();
            }

            //Calculos penalizacion
            montoPenalizacion = precioBase * (comisionRepartidor/100); //10% del precio base
            pagarComisionRepartidor = true;
            aplicaPenalizacion = true;
        }

        //Crear la cancelacion
        CancelacionGuia cancelacionGuia = new CancelacionGuia();
        cancelacionGuia.setGuia(guia);
        cancelacionGuia.setMotivoCategoria(CancelacionGuia.MotivoCategoria.valueOf(cancelacionGuiaDTO.getMotivoCategoria()));
        cancelacionGuia.setMotivoDetalle(cancelacionGuiaDTO.getMotivoDetalle());
        cancelacionGuia.setCanceladaPor(empleado);
        cancelacionGuia.setFechaCancelacion(LocalDateTime.now());
        cancelacionGuia.setAplicaPenalizacion(aplicaPenalizacion);
        cancelacionGuia.setMontoPenalizacion(montoPenalizacion);
        cancelacionGuia.setPagarComisionRepartidor(pagarComisionRepartidor);
        cancelacionGuia.setMontoComisionPagar(montoPenalizacion);
        cancelacionGuia.setEstadoCancelacion(CancelacionGuia.EstadoCancelacion.SOLICITADA);
        cancelacionGuiaRepository.save(cancelacionGuia);

        return "La cancelación de la guía ha sido registrada y está pendiente de autorización.";
    }

}
