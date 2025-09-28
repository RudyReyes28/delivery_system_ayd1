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
public class CancelacionClienteService {
    @Autowired
    private GuiaRespository guiaRepository;

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public String registrarIncidenciaCliente(IncidenciaCancelacionDTO incidenciaDTO) {
        Guia guia = guiaRepository.findById(incidenciaDTO.getIdGuia())
                .orElseThrow(() -> new RuntimeException("Guía no encontrada con ID: " + incidenciaDTO.getIdGuia()));

        Usuario usuario = usuarioRepository.findById(incidenciaDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + incidenciaDTO.getIdUsuario()));

        Repartidor repartidor = guia.getRepartidor();
        if (repartidor == null) {
            throw new RuntimeException("La guía no tiene un repartidor asignado.");
        }

        Incidencia incidencia = new Incidencia();
        incidencia.setGuia(guia);
        incidencia.setRepartidor(repartidor);
        incidencia.setCodigoIncidencia("INC" + System.currentTimeMillis());
        incidencia.setTipoIncidencia(Incidencia.TipoIncidencia.CLIENTE_RECHAZA);
        incidencia.setSeveridad(Incidencia.Severidad.CRITICA);
        incidencia.setDescripcion(incidenciaDTO.getDescripcion());
        incidencia.setRequiereDevolucion(false);
        incidencia.setCostoAdicional(0.00);
        incidencia.setFechaReporte(LocalDateTime.now());

        incidenciaRepository.save(incidencia);
        guia.setEstadoActual(Guia.EstadoActual.CANCELADA);
        guiaRepository.save(guia);

        return "Incidencia registrada y guía cancelada exitosamente.";
    }


}
