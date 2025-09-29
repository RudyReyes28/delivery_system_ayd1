package org.entregasayd.sistemasentregas.services.reportes;

import org.entregasayd.sistemasentregas.dto.repartidor.ComisionRepartidorDTO;
import org.entregasayd.sistemasentregas.dto.reportes.CancelacionesEmpresaDTO;
import org.entregasayd.sistemasentregas.dto.reportes.ComisionRepartidorReporteDTO;
import org.entregasayd.sistemasentregas.dto.reportes.DescuentosAplicadosDTO;
import org.entregasayd.sistemasentregas.dto.reportes.EntregasDTO;
import org.springframework.stereotype.Service;

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
import java.util.Objects;

@Service
public class ReportesService {
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
    private ContratoComisionRepository contratoComisionRepository;
    @Autowired
    private LiquidacionRepartidorRepository liquidacionRepartidorRepository;
    @Autowired
    private PeriodoLiquidacionRepository periodoLiquidacionRepository;
    @Autowired
    private EmpresaFidelizacionRepository EmpresaFidelizacionRepository;
    @Autowired
    private ComisionEntregaRepository comisionEntregaRepository;
    @Autowired
    private FacturaEmpresaRepository facturaEmpresaRepository;
    @Autowired
    private FacturaDetalleRepository facturaDetalleRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private IncidenciaRepository IncidenciaRepository;
    @Autowired
    private NivelFidelizacionRepository NivelFidelizacionRepository;
    @Autowired
    private EmpresaRepository EmpresaRepository;
    @Autowired
    private CancelacionGuiaRepository CancelacionGuiaRepository;

    //Reporte entregas completadas / canceladas / rechazadas
    public EntregasDTO getEntregasDTO() {
        List<EntregasDTO.EntregasCanceladasDTO> entregasCanceladasList = new ArrayList<>();
        List<EntregasDTO.EntregasCompletadasDTO> entregasCompletadasList =new ArrayList<>();
        List<EntregasDTO.EntregasRechazadasDTO> entregasRechazadasList = new ArrayList<>();
        ArrayList<Guia> guias =  (ArrayList<Guia>) guiaRepository.findAll();
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
            //Revisamos el estado de la guia para agregarla al reporte correspondiente
            if (guia.getEstadoActual() == Guia.EstadoActual.CANCELADA) {
                //AQUI HAY QUE VER SI LA GUIA TIENE UNA INCIDENCIA DE RECHAZO Y ESTA CERRADA
                List<Incidencia> incidenciasGuia = IncidenciaRepository.findByGuia_IdGuiaAndTipoIncidencia(guia.getIdGuia(), Incidencia.TipoIncidencia.CLIENTE_RECHAZA);
                if(incidenciasGuia.isEmpty()){
                    EntregasDTO.EntregasCanceladasDTO entregasCanceladasDTO = new EntregasDTO.EntregasCanceladasDTO();
                    entregasCanceladasDTO.setEntregasCanceladas(detalleGuia);
                    entregasCanceladasDTO.setTotalEntregasCanceladas(1);
                    entregasCanceladasList.add(entregasCanceladasDTO);
                }else{
                    for(Incidencia incidencia : incidenciasGuia){
                        if(incidencia.getEstado() == Incidencia.Estado.CERRADA) {
                            EntregasDTO.EntregasRechazadasDTO entregasRechazadasDTO = new EntregasDTO.EntregasRechazadasDTO();
                            entregasRechazadasDTO.setEntregasRechazadas(detalleGuia);
                            entregasRechazadasDTO.setMotivoRechazo(incidencia.getDescripcion());
                            entregasRechazadasDTO.setFechaRechazo(incidencia.getFechaReporte().toString());
                            entregasRechazadasDTO.setTotalEntregasRechazadas(1);
                            entregasRechazadasList.add(entregasRechazadasDTO);
                        }
                    }

                }

            }else if(guia.getEstadoActual() == Guia.EstadoActual.ENTREGADA) {
                EntregasDTO.EntregasCompletadasDTO entregasCompletadasDTO = new EntregasDTO.EntregasCompletadasDTO();
                entregasCompletadasDTO.setEntregasCompletadas(detalleGuia);
                entregasCompletadasDTO.setTotalEntregasCompletadas(1);
                entregasCompletadasList.add(entregasCompletadasDTO);
            }
        }

        EntregasDTO entregasDTO = new EntregasDTO();
        entregasDTO.setEntregasCanceladas(entregasCanceladasList);
        entregasDTO.setEntregasCompletadas(entregasCompletadasList);
        entregasDTO.setEntregasRechazadas(entregasRechazadasList);
        return entregasDTO;
    }

    //Comisiones de los repartidores
    public List<ComisionRepartidorReporteDTO> comisionesRepartidores(){
        //Obtenemos el repartidor asociado al usuario
        List<Repartidor> repartidor = repartidorRepository.findAll();
        List<ComisionRepartidorReporteDTO> comisionRepartidorReporteDTOs = new ArrayList<>();
        List<PeriodoLiquidacion> periodoLiquidaciones= periodoLiquidacionRepository.findAll();

        for(PeriodoLiquidacion periodo: periodoLiquidaciones){
            ComisionRepartidorReporteDTO.PeriodoLiquidacionReporteDTO periodoDTO = ComisionRepartidorReporteDTO.PeriodoLiquidacionReporteDTO.builder()
                    .idPeriodo(periodo.getIdPeriodo())
                    .descripcion(periodo.getDescripcion())
                    .fechaInicio(periodo.getFechaInicio().toString())
                    .fechaFin(periodo.getFechaFin().toString())
                    .estado(periodo.getEstado().toString())
                    .repartidores(new ArrayList<>())
                    .build();

            List<ComisionRepartidorReporteDTO.RepartidoresLiquidacionDTO> repartidoresLiquidacionDTO = new ArrayList<>();
            //Obtenemos los repartidores que tienen liquidaciones en el periodo
            List<LiquidacionRepartidor> liquidacionesPeriodo = liquidacionRepartidorRepository.findByPeriodoLiquidacion_IdPeriodo(periodo.getIdPeriodo());
            for(LiquidacionRepartidor liquidacion : liquidacionesPeriodo){
                for(Repartidor rep : repartidor){
                    if(liquidacion.getRepartidor().getIdRepartidor().equals(rep.getIdRepartidor())) {
                        ComisionRepartidorReporteDTO.RepartidoresLiquidacionDTO repartidorDTO = ComisionRepartidorReporteDTO.RepartidoresLiquidacionDTO.builder()
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
                                .nombreRepartidor(rep.getEmpleado().getUsuario().getPersona().getNombre() + " " + rep.getEmpleado().getUsuario().getPersona().getApellido())
                                .idRepartidor(rep.getIdRepartidor())
                                .detallesComision(new ArrayList<>())
                                .build();

                        List<ComisionEntrega> detallesComision = comisionEntregaRepository.findByRepartidor_IdRepartidorAndPeriodoLiquidacion_IdPeriodo(rep.getIdRepartidor(), liquidacion.getPeriodoLiquidacion().getIdPeriodo());
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
                            repartidorDTO.getDetallesComision().add(detalleComisionDTO);
                        }
                        repartidoresLiquidacionDTO.add(repartidorDTO);
                    }



                }
            }
            periodoDTO.setRepartidores(repartidoresLiquidacionDTO);
            ComisionRepartidorReporteDTO comisionRepartidorReporteDTO = ComisionRepartidorReporteDTO.builder()
                    .periodo(periodoDTO)
                    .build();
            comisionRepartidorReporteDTOs.add(comisionRepartidorReporteDTO);



        }
        return comisionRepartidorReporteDTOs;

    }

    //Descuentos aplicados por nivel de fidelizacion
    public List<DescuentosAplicadosDTO> descuentosAplicados() {

        //Obtener el nivel de fidelizacion
        List<NivelFindelizacion> niveles = NivelFidelizacionRepository.findAll();
        List<DescuentosAplicadosDTO> descuentosAplicadosDTOs = new ArrayList<>();
        for (NivelFindelizacion nivel : niveles) {
            DescuentosAplicadosDTO descuentosAplicadosDTO = new DescuentosAplicadosDTO();
            descuentosAplicadosDTO.setIdNivelFidelizacion(nivel.getIdNivel());
            descuentosAplicadosDTO.setNombreNivel(nivel.getNombreNivel());
            descuentosAplicadosDTO.setCodigoNivel(nivel.getCodigoNivel());
            descuentosAplicadosDTO.setPorcentajeDescuento(nivel.getDescuentoPorcentaje());
            //Obtener las empresas con ese nivel de fidelizacion
            List<EmpresaFidelizacion> empresas = EmpresaFidelizacionRepository.findByNivelFidelizacion_IdNivel(nivel.getIdNivel());
            double totalDescuentosNivel = 0.0;
            int totalEmpresasNivel = empresas.size();
            for (EmpresaFidelizacion empresa : empresas) {
                totalDescuentosNivel += empresa.getDescuentoAplicado();

            }
            descuentosAplicadosDTO.setTotalDescuentosAplicados(totalDescuentosNivel);
            descuentosAplicadosDTO.setTotalEmpresasBeneficiadas(totalEmpresasNivel);

            descuentosAplicadosDTOs.add(descuentosAplicadosDTO);
        }

        return descuentosAplicadosDTOs;
    }


    //Empreas con cancelaciones
    public List<CancelacionesEmpresaDTO>  cancelacionesEmpresa() {
        List<CancelacionesEmpresaDTO> cancelacionesEmpresaDTOs = new ArrayList<>();
        //Obtenemos las empresas que tienen cancelaciones
        List<Empresa> empresa = EmpresaRepository.findAll();

        for(Empresa emp : empresa){
            CancelacionesEmpresaDTO cancelacionesEmpresaDTO = new CancelacionesEmpresaDTO();
            cancelacionesEmpresaDTO.setIdEmpresa(emp.getIdEmpresa());
            cancelacionesEmpresaDTO.setNombreEmpresa(emp.getNombreComercial());
            //Obtenemos las facturas de la empresa
            //Otenemos las cancelaciones de las guias
            List<CancelacionGuia> cancelacionGuias = CancelacionGuiaRepository.findAll();
            List<CancelacionesEmpresaDTO.GuiaCancelacionesDTO> guiaCancelacionesDTOList = new ArrayList<>();
            int totalCancelaciones = 0;
            for(CancelacionGuia cancelacion : cancelacionGuias){
                if(Objects.equals(cancelacion.getGuia().getEmpresa().getIdEmpresa(), emp.getIdEmpresa()) && cancelacion.getEstadoCancelacion().equals(CancelacionGuia.EstadoCancelacion.PROCESADA)){
                    CancelacionesEmpresaDTO.GuiaCancelacionesDTO guiaCancelacionesDTO = new CancelacionesEmpresaDTO.GuiaCancelacionesDTO();
                    guiaCancelacionesDTO.setIdGuia(cancelacion.getGuia().getIdGuia());
                    guiaCancelacionesDTO.setNumeroGuia(cancelacion.getGuia().getNumeroGuia());
                    guiaCancelacionesDTO.setMotivoCancelacion(cancelacion.getMotivoDetalle());
                    guiaCancelacionesDTO.setFechaCancelacion(cancelacion.getFechaCancelacion().toString());
                    guiaCancelacionesDTO.setCategoriaCancelacion(cancelacion.getMotivoCategoria().name());
                    guiaCancelacionesDTOList.add(guiaCancelacionesDTO);
                    totalCancelaciones++;
                }
            }

            cancelacionesEmpresaDTO.setGuiasCanceladas(guiaCancelacionesDTOList);
            cancelacionesEmpresaDTO.setTotalCancelaciones(totalCancelaciones);
            if(totalCancelaciones > 0) {
                cancelacionesEmpresaDTOs.add(cancelacionesEmpresaDTO);
            }


        }

        return cancelacionesEmpresaDTOs;

    }



}
