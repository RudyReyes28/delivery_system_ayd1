package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.RepartidorVehiculoDTO;
import org.entregasayd.sistemasentregas.mapper.RepartidorVehiculoMap;
import org.entregasayd.sistemasentregas.models.Repartidor;
import org.entregasayd.sistemasentregas.models.RepartidorVehiculo;
import org.entregasayd.sistemasentregas.models.Vehiculo;
import org.entregasayd.sistemasentregas.repositories.RepartidorRepository;
import org.entregasayd.sistemasentregas.repositories.RepartidorVehiculoRepository;
import org.entregasayd.sistemasentregas.repositories.VehiculoRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepartidorVehiculoService {

    @Autowired
    private RepartidorVehiculoRepository repartidorVehiculoRepository;
    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private RepartidorVehiculoMap map;


    public RepartidorVehiculoDTO save(RepartidorVehiculoDTO repartidorVehiculoDTO) {
        RepartidorVehiculo repartidorVehiculo = repartidorVehiculoRepository
                .getByRepartidor_IdRepartidorAndVehiculo_IdVehiculo(
                        repartidorVehiculoDTO.getIdRepartidor(),
                        repartidorVehiculoDTO.getIdVehiculo()
                );
        if (repartidorVehiculo != null) {
            throw new ErrorApi(400, "El repartidor ya tiene asignado este vehiculo");
        }
        Optional<Repartidor> repartidor = repartidorRepository.findById(repartidorVehiculoDTO.getIdRepartidor());
        if (repartidor.isEmpty()) {
            throw new ErrorApi(400, "El repartidor no existe");
        }

        Optional<Vehiculo> vehiculo = vehiculoRepository.findById(repartidorVehiculoDTO.getIdVehiculo());
        if (vehiculo.isEmpty()) {
            throw new ErrorApi(400, "El vehiculo no existe");
        }

        RepartidorVehiculo repartidorVehiculoSave = new RepartidorVehiculo();
        repartidorVehiculoSave.setRepartidor(repartidor.get());
        repartidorVehiculoSave.setVehiculo(vehiculo.get());
        repartidorVehiculoSave.setEsVehiculoPrincipal(repartidorVehiculoDTO.getEsVehiculoPrincipal());

        return map.toDTO(repartidorVehiculoRepository.save(repartidorVehiculoSave));
    }

    /**
     * Actualiza un registro de repartidor vehiculo
     * @param repartidorVehiculoDTO datos de entrada
     * @return RepartidorVehiculoDTO
     */
    public RepartidorVehiculoDTO update(RepartidorVehiculoDTO repartidorVehiculoDTO) {
        RepartidorVehiculo repartidorVehiculo = repartidorVehiculoRepository.
                findById(repartidorVehiculoDTO.getIdRepartidorVehiculo()).orElse(null);
        if (repartidorVehiculo == null) {
            throw new ErrorApi(404, "No se encontro el registro");
        }
        repartidorVehiculo.setFechaAsignacion(repartidorVehiculoDTO.getFechaAsignacion());
        repartidorVehiculo.setFechaDesasignacion(repartidorVehiculoDTO.getFechaDesasignacion());
        repartidorVehiculo.setEsVehiculoPrincipal(repartidorVehiculoDTO.getEsVehiculoPrincipal());
        repartidorVehiculo.setActivo(repartidorVehiculoDTO.getActivo());

        return map.toDTO(repartidorVehiculoRepository.save(repartidorVehiculo));
    }

    /**
     * Obtiene todos los vehiculos asignados a un repartidor
     * @param idRepartidor id del repartidor
     * @return List<RepartidorVehiculoDTO>
     */
    public List<RepartidorVehiculoDTO> vehiculosAsignados(Long idRepartidor) {
        List<RepartidorVehiculo> list = repartidorVehiculoRepository.getByRepartidor_IdRepartidor(idRepartidor);
        return list.stream().map(map::toDTO).toList();
    }

    public List<RepartidorVehiculoDTO> getAll() {
        return repartidorVehiculoRepository.findAll().stream().map(map::toDTO).toList();
    }
}
