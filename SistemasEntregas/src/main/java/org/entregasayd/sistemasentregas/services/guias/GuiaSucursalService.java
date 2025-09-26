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
@Slf4j
@Service
public class GuiaSucursalService {
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

    //Obtener los tipos de servicio disponibles para la sucursal del usuario
    public ArrayList<TipoServicio> getTipoServicios(){
        return (ArrayList<TipoServicio>) tipoServicioRepository.findAll();
    }

    //Obtener clientes
    public ArrayList<GetClienteDTO> getClientes(){
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) clienteRepository.findAll();
        ArrayList<GetClienteDTO> clientesDTO = new ArrayList<>();
        for(Cliente c : clientes){
            GetClienteDTO dto = new GetClienteDTO();
            dto.setIdCliente(c.getIdCliente());
            dto.setCodigoCliente(c.getCodigoCliente());
            dto.setNombreCompleto(c.getNombreCompleto());
            dto.setTelefono(c.getTelefono());
            dto.setEmail(c.getEmail());
            clientesDTO.add(dto);
        }
        return clientesDTO;
    }

    //Crear guia con un nuevo cliente
    @Transactional
    public Guia crearGuiaConClienteNuevo(CrearGuiaConClienteDTO dto) throws Exception {
        //1. Crear el cliente
        Cliente cliente = new Cliente();
        //Generar un codigo unico para el cliente
        String codigoCliente = "CL" + System.currentTimeMillis();
        cliente.setCodigoCliente(codigoCliente);
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setHorarioPreferidoInicio(dto.getHorarioPreferidoInicio());
        cliente.setHorarioPreferidoFin(dto.getHorarioPreferidoFin());
        cliente.setInstruccionesEntrega(dto.getInstruccionesEntrega());
        cliente.setAceptaEntregasVecinos(dto.getAceptaEntregasVecinos());
        cliente.setRequiereIdentificacion(dto.getRequiereIdentificacion());
        Cliente clienteSaved = clienteRepository.save(cliente);

        //2. Crear la direccion de entrega
        Direccion direccion = new Direccion();
        direccion.setTipoDireccion(Direccion.TipoDireccion.ENTREGA);
        direccion.setMunicipio(dto.getMunicipio());
        direccion.setDepartamento(dto.getDepartamento());
        direccion.setPais(dto.getPais());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setReferencias(dto.getReferencias());
        Direccion direccionSaved = direccionRepository.save(direccion);

        //3. Crear la direccion de entrega del cliente
        DireccionEntrega direccionEntrega = new DireccionEntrega();
        direccionEntrega.setCliente(clienteSaved);
        direccionEntrega.setDireccion(direccionSaved);
        direccionEntrega.setAliasDireccion(dto.getAliasDireccion());
        direccionEntrega.setInstruccionesEspecificas(dto.getInstruccionesEspecificas());
        direccionEntrega.setPuntoReferencia(dto.getPuntoReferencia());
        direccionEntrega.setPersonaRecibe(dto.getPersonaRecibe());
        direccionEntrega = direccionEntregaRepository.save(direccionEntrega);

        //4. Validar y obtener la empresa y sucursal del usuario autenticado
        //Obtener la empresa y sucursal del usuario autenticado
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new Exception("Usuario no encontrado"));
        //Validamos que el usuario este activo
        if(!usuario.getEstado().equals(Usuario.Estado.ACTIVO)){
            throw new Exception("El usuario no está activo");
        }
        //Primero buscamos la sucursal en sucursal contacto
        Sucursal sucursal = sucursalPersonalRepository.findByUsuarioIdUsuario(dto.getIdUsuario()).getSucursal();
        if(sucursal == null){
            throw new Exception("El usuario no tiene una sucursal asignada");
        }
        //Validamos que la sucursal este activa
        if(!sucursal.getEstado().equals(Sucursal.Estado.ACTIVA)) {
            throw new Exception("La sucursal no está activa");
        }
        //Obtenemos la empresa
        Empresa empresa = sucursal.getEmpresa();
        if(empresa == null) {
            throw new Exception("La sucursal no tiene una empresa asignada");
        }
        //Validamos que la empresa este activa
        if(!empresa.getEstado().equals(Empresa.Estado.ACTIVA)) {
            throw new Exception("La empresa no está activa");
        }
        //5. Crear la guia
        Guia guia = new Guia();
        //Generar un numero de guia unico y mas humano y legible para el usuario
        String numeroGuia = "GUIA-" + System.currentTimeMillis();
        guia.setNumeroGuia(numeroGuia);
        //Generar un codigo interno unico para la guia
        String codigoInterno = "COD-" + System.currentTimeMillis();
        guia.setCodigoInterno(codigoInterno);
        guia.setEmpresa(empresa);
        guia.setSucursalOrigen(sucursal);
        guia.setCliente(clienteSaved);
        guia.setDireccionEntrega(direccionSaved);
        TipoServicio tipoServicio = tipoServicioRepository.findById(dto.getIdTipoServicio()).orElseThrow(() -> new Exception("Tipo de servicio no encontrado"));
        guia.setTipoServicio(tipoServicio);
        guia.setDescripcionContenido(dto.getDescripcionContenido());
        guia.setValorDeclarado(dto.getValorDeclarado());
        guia.setPesoKg(dto.getPesoKG());
        guia.setDimensiones(dto.getDimensiones());
        guia.setEsFragil(dto.getEsFragil());
        guia.setObservaciones(dto.getObservaciones());
        guia.setFechaCreacion(LocalDateTime.now());
        //Convertir la fecha programada de recoleccion de String a LocalDateTime
        //Formato AAAA-MM-DDTHH:MM
        if(dto.getFechaProgramadaRecoleccion() != null && !dto.getFechaProgramadaRecoleccion().isEmpty()) {
            guia.setFechaProgramadaRecoleccion(LocalDateTime.parse(dto.getFechaProgramadaRecoleccion() + "T00:00"));
        }
        guia.setPrioridad(Guia.Prioridad.valueOf(dto.getPrioridad()));
        //Calculos financieros
        guia.setSubtotal(dto.getSubtotal());
        guia.setDescuentos(dto.getDescuentos());
        guia.setRecargos(dto.getRecargos());
        guia.setTotal(guia.getSubtotal() - guia.getDescuentos() + guia.getRecargos());
        //Informacion de pago
        guia.setFormaPago(Guia.FormaPago.valueOf(dto.getFormaPago()));
        guia.setEstadoActual(Guia.EstadoActual.CREADA);
        Guia guiaSaved = guiaRepository.save(guia);

        //Guardar el estado inicial de la guia en la tabla guia_estado
        GuiaEstado guiaEstado = new GuiaEstado();
        guiaEstado.setGuia(guiaSaved);
        guiaEstado.setEstadoAnterior(GuiaEstado.Estado.CREADA); //No hay estado anterior, es la creacion
        guiaEstado.setEstadoNuevo(GuiaEstado.Estado.CREADA);
        guiaEstado.setUsuarioCambio(usuario);
        guiaEstado.setComentarios("Guía creada");
        guiaEstado.setMotivoCambio("Creación de la guía");
        guiaEstadoRepository.save(guiaEstado);

        //Notificar al usuario que se creo la guia por medio del correo electronico
        try {
            String emailTo = clienteSaved.getEmail();
            if(emailTo != null && !emailTo.isEmpty()) {
                emailService.sendNotificationEmailEstadoGuia(
                        emailTo,
                        guiaSaved.getNumeroGuia(),
                        guiaSaved.getEstadoActual().name(),
                        empresa.getNombreComercial()
                );
            }
        } catch (Exception e) {
            //Si hay un error al enviar el correo, no se revierte la transaccion, solo se loguea el error
            log.info("Error al enviar correo de notificacion: " + e.getMessage());

        }
        return guiaSaved;

    }

    //Crear guia con un cliente existente
    @Transactional
    public Guia crearGuiaConClienteExistente(CrearGuiaSinClienteDTO dto) throws Exception {
        //1. Validar y obtener el cliente
        Cliente clienteSaved = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new Exception("Cliente no encontrado"));
        //Validar que el cliente este activo
        if(!clienteSaved.getEstado().equals(Cliente.Estado.ACTIVO)) {
            throw new Exception("El cliente no está activo");
        }
        //2. Obtener la direccion de entrega del cliente
        Direccion direccionSaved = direccionEntregaRepository.findByClienteIdCliente(clienteSaved.getIdCliente()).getDireccion();
        //Validar que la direccion de entrega este activa
        if(direccionSaved == null) {
            throw new Exception("El cliente no tiene una dirección de entrega asignada");
        }
        //4. Validar y obtener la empresa y sucursal del usuario autenticado
        //Obtener la empresa y sucursal del usuario autenticado
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new Exception("Usuario no encontrado"));
        //Validamos que el usuario este activo
        if(!usuario.getEstado().equals(Usuario.Estado.ACTIVO)){
            throw new Exception("El usuario no está activo");
        }
        //Primero buscamos la sucursal en sucursal contacto
        Sucursal sucursal = sucursalPersonalRepository.findByUsuarioIdUsuario(dto.getIdUsuario()).getSucursal();
        if(sucursal == null){
            throw new Exception("El usuario no tiene una sucursal asignada");
        }
        //Validamos que la sucursal este activa
        if(!sucursal.getEstado().equals(Sucursal.Estado.ACTIVA)) {
            throw new Exception("La sucursal no está activa");
        }
        //Obtenemos la empresa
        Empresa empresa = sucursal.getEmpresa();
        if(empresa == null) {
            throw new Exception("La sucursal no tiene una empresa asignada");
        }
        //Validamos que la empresa este activa
        if(!empresa.getEstado().equals(Empresa.Estado.ACTIVA)) {
            throw new Exception("La empresa no está activa");
        }
        //5. Crear la guia
        Guia guia = new Guia();
        //Generar un numero de guia unico y mas humano y legible para el usuario
        String numeroGuia = "GUIA-" + System.currentTimeMillis();
        guia.setNumeroGuia(numeroGuia);
        //Generar un codigo interno unico para la guia
        String codigoInterno = "COD-" + System.currentTimeMillis();
        guia.setCodigoInterno(codigoInterno);
        guia.setEmpresa(empresa);
        guia.setSucursalOrigen(sucursal);
        guia.setCliente(clienteSaved);
        guia.setDireccionEntrega(direccionSaved);
        TipoServicio tipoServicio = tipoServicioRepository.findById(dto.getIdTipoServicio()).orElseThrow(() -> new Exception("Tipo de servicio no encontrado"));
        guia.setTipoServicio(tipoServicio);
        guia.setDescripcionContenido(dto.getDescripcionContenido());
        guia.setValorDeclarado(dto.getValorDeclarado());
        guia.setPesoKg(dto.getPesoKG());
        guia.setDimensiones(dto.getDimensiones());
        guia.setEsFragil(dto.getEsFragil());
        guia.setObservaciones(dto.getObservaciones());
        guia.setFechaCreacion(LocalDateTime.now());
        //Convertir la fecha programada de recoleccion de String a LocalDateTime
        //Formato AAAA-MM-DDTHH:MM
        if(dto.getFechaProgramadaRecoleccion() != null && !dto.getFechaProgramadaRecoleccion().isEmpty()) {
            guia.setFechaProgramadaRecoleccion(LocalDateTime.parse(dto.getFechaProgramadaRecoleccion() + "T00:00"));
        }
        guia.setPrioridad(Guia.Prioridad.valueOf(dto.getPrioridad()));
        //Calculos financieros
        guia.setSubtotal(dto.getSubtotal());
        guia.setDescuentos(dto.getDescuentos());
        guia.setRecargos(dto.getRecargos());
        guia.setTotal(guia.getSubtotal() - guia.getDescuentos() + guia.getRecargos());
        //Informacion de pago
        guia.setFormaPago(Guia.FormaPago.valueOf(dto.getFormaPago()));
        guia.setEstadoActual(Guia.EstadoActual.CREADA);
        Guia guiaSaved = guiaRepository.save(guia);

        //Guardar el estado inicial de la guia en la tabla guia_estado
        GuiaEstado guiaEstado = new GuiaEstado();
        guiaEstado.setGuia(guiaSaved);
        guiaEstado.setEstadoAnterior(GuiaEstado.Estado.CREADA); //No hay estado anterior, es la creacion
        guiaEstado.setEstadoNuevo(GuiaEstado.Estado.CREADA);
        guiaEstado.setUsuarioCambio(usuario);
        guiaEstado.setComentarios("Guía creada");
        guiaEstado.setMotivoCambio("Creación de la guía");
        guiaEstadoRepository.save(guiaEstado);


        //Notificar al usuario que se creo la guia por medio del correo electronico
        try {
            String emailTo = clienteSaved.getEmail();
            if(emailTo != null && !emailTo.isEmpty()) {
                emailService.sendNotificationEmailEstadoGuia(
                        emailTo,
                        guiaSaved.getNumeroGuia(),
                        guiaSaved.getEstadoActual().name(),
                        empresa.getNombreComercial()
                );
            }
        } catch (Exception e) {
            //Si hay un error al enviar el correo, no se revierte la transaccion, solo se loguea el error
            log.info("Error al enviar correo de notificacion: " + e.getMessage());

        }
        return guiaSaved;

    }

    //Obtener todas las guias de la sucursal del usuario autenticado
    public ArrayList<GetDetalleGuiaDTO> getGuiasDeSucursal(Long idUsuario) throws Exception {
        //Validar y obtener la sucursal del usuario autenticado
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
        //Validamos que el usuario este activo
        if (!usuario.getEstado().equals(Usuario.Estado.ACTIVO)) {
            throw new Exception("El usuario no está activo");
        }
        //Primero buscamos la sucursal en sucursal contacto
        Sucursal sucursal = sucursalPersonalRepository.findByUsuarioIdUsuario(idUsuario).getSucursal();
        if (sucursal == null) {
            throw new Exception("El usuario no tiene una sucursal asignada");
        }

        //Obtenemos las guias de la sucursal
        ArrayList<Guia> guias = guiaRepository.findBySucursalOrigenIdSucursal(sucursal.getIdSucursal());
        ArrayList<GetDetalleGuiaDTO> guiasDTO = new ArrayList<>();
        for (Guia g : guias) {
            GetDetalleGuiaDTO dto = new GetDetalleGuiaDTO();
            //Detalles de la guia
            GetDetalleGuiaDTO.GuiaDTO guiaDTO = new GetDetalleGuiaDTO.GuiaDTO();
            guiaDTO.setIdGuia(g.getIdGuia());
            guiaDTO.setNumeroGuia(g.getNumeroGuia());
            guiaDTO.setCodigoInterno(g.getCodigoInterno());
            guiaDTO.setDescripcionContenido(g.getDescripcionContenido());
            guiaDTO.setValorDeclarado(g.getValorDeclarado());
            guiaDTO.setPesoKg(g.getPesoKg());
            guiaDTO.setDimensiones(g.getDimensiones());
            guiaDTO.setObservaciones(g.getObservaciones());
            guiaDTO.setFechaCreacion(g.getFechaCreacion().toString());
            if (g.getFechaRecoleccionReal() != null) {
                guiaDTO.setFechaRecoleccionReal(g.getFechaRecoleccionReal().toString());
            } else {
                guiaDTO.setFechaRecoleccionReal("No recolectada");
            }
            guiaDTO.setPrioridad(g.getPrioridad().name());
            guiaDTO.setIntentosEntrega(g.getIntentosEntrega());
            guiaDTO.setTotal(g.getTotal());
            guiaDTO.setEstadoActual(g.getEstadoActual().name());

            //Detalles del cliente
            GetDetalleGuiaDTO.ClienteDTO clienteDTO = new GetDetalleGuiaDTO.ClienteDTO();
            clienteDTO.setIdCliente(g.getCliente().getIdCliente());
            clienteDTO.setNombreCompleto(g.getCliente().getNombreCompleto());
            clienteDTO.setEmail(g.getCliente().getEmail());
            clienteDTO.setTelefono(g.getCliente().getTelefono());
            guiaDTO.setCliente(clienteDTO);
            //Detalles de la direccion de entrega
            GetDetalleGuiaDTO.DireccionEntregaDTO direccionEntregaDTO = new GetDetalleGuiaDTO.DireccionEntregaDTO();
            direccionEntregaDTO.setIdDireccion(g.getDireccionEntrega().getIdDireccion());
            direccionEntregaDTO.setMunicipio(g.getDireccionEntrega().getMunicipio());
            direccionEntregaDTO.setDepartamento(g.getDireccionEntrega().getDepartamento());
            direccionEntregaDTO.setPais(g.getDireccionEntrega().getPais());
            direccionEntregaDTO.setCodigoPostal(g.getDireccionEntrega().getCodigoPostal());
            direccionEntregaDTO.setReferencias(g.getDireccionEntrega().getReferencias());
            guiaDTO.setDireccionEntrega(direccionEntregaDTO);

            //Listado de estados de la guia
            ArrayList<GetDetalleGuiaDTO.GuiaEstadoDTO> estadosDTO = new ArrayList<>();
            ArrayList<GuiaEstado> estados = guiaEstadoRepository.findByGuiaIdGuiaOrderByFechaCambioDesc(g.getIdGuia());
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
            guiaDTO.setEstadosEntregas(estadosDTO);
            dto.setGuia(guiaDTO);
            guiasDTO.add(dto);
        }
        return guiasDTO;
    }

}
