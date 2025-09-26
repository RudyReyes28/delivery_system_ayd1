package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.user.RepartidorDTO;
import org.entregasayd.sistemasentregas.enums.Rol;
import org.entregasayd.sistemasentregas.mapper.RepartidorMap;
import org.entregasayd.sistemasentregas.models.Empleado;
import org.entregasayd.sistemasentregas.models.Repartidor;
import org.entregasayd.sistemasentregas.models.Usuario;
import org.entregasayd.sistemasentregas.repositories.RepartidorRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RepartidorMap repartidorMap;

    /**
     * Obtiene todos los repartidores
     * @return List<RepartidorDTO>
     */
    public List<RepartidorDTO> getRepartidores() {
        return repartidorRepository.findAll().stream().map(repartidorMap::toDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los repartidores por tipo licencia
     * @return List<RepartidorDTO>
     */
    public List<RepartidorDTO> getRepartidoresPorTipoLicencia(String tipoLicencia) {
        List<Repartidor> list = repartidorRepository.findRepartidorByTipoLicencia(Repartidor.TipoLicencia.valueOf(tipoLicencia.toUpperCase()));
        return list.stream().map(repartidorMap::toDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los repartidores por disponibilidad
     * @param disponibilidad disponiblidad del repartidor
     * @return List<RepartidorDTO>
     */
    public List<RepartidorDTO> getRepartidoresPorDisponibilidad(String disponibilidad) {
        List<Repartidor> list = repartidorRepository.findRepartidorByDisponibilidad(Repartidor.Disponibilidad.valueOf(disponibilidad.toUpperCase()));
        return list.stream().map(repartidorMap::toDTO).collect(Collectors.toList());
    }

    public RepartidorDTO findById(Long id) {
        return repartidorRepository.findById(id).map(repartidorMap::toDTO).orElseThrow(() -> new ErrorApi(400, "El repartidor no existe"));
    }

    public RepartidorDTO save(RepartidorDTO repartidorDTO) {
        Empleado empleado = empleadoService.findById(repartidorDTO.getIdEmpleado());
        if (empleado == null) {
            throw new ErrorApi(400, "El empleado no existe");
        }
        Usuario usuarioRepartidor = usuarioService.findById(empleado.getUsuario().getIdUsuario());
        if(usuarioRepartidor == null) {
            throw new ErrorApi(400, "El usuario no existe");
        }
        if(!usuarioRepartidor.getRol().getNombre().equalsIgnoreCase("REPARTIDOR")){
            throw new ErrorApi(400, String.format("El usuario no es un repartidor es un  %s", usuarioRepartidor.getRol().getNombre().toLowerCase()));
        }
        Repartidor repartidor = new Repartidor();
        repartidor.setEmpleado(empleado);
        return getRepartidorDTO(repartidorDTO, repartidor);
    }

    public RepartidorDTO update(RepartidorDTO repartidorDTO) {
        Repartidor repartidor = repartidorRepository.findById(repartidorDTO.getIdRepartidor()).orElse(null);
        if (repartidor == null) {
            throw new ErrorApi(400, "El repartidor no existe");
        }
        return getRepartidorDTO(repartidorDTO, repartidor);
    }

    private RepartidorDTO getRepartidorDTO(RepartidorDTO repartidorDTO, Repartidor repartidor) {
        repartidor.setNumeroLicencia(repartidorDTO.getNumeroLicencia());
        repartidor.setTipoLicencia(repartidorDTO.getTipoLicencia());
        repartidor.setFechaVencimientoLicencia(repartidorDTO.getFechaVencimientoLicencia());
        repartidor.setZonaAsignada(repartidorDTO.getZonaAsignada());
        repartidor.setRadioCoberturaKm(repartidorDTO.getRadioCoberturaKm());
        repartidor.setDisponibilidad(repartidorDTO.getDisponibilidad());
        repartidor.setCalificacionPromedio(repartidorDTO.getCalificacionPromedio());
        repartidor.setTotalEntregasCompletadas(repartidorDTO.getTotalEntregasCompletadas());
        repartidor.setTotalEntregasFallidas(repartidorDTO.getTotalEntregasFallidas());

        return repartidorMap.toDTO(repartidorRepository.save(repartidor));
    }

}
