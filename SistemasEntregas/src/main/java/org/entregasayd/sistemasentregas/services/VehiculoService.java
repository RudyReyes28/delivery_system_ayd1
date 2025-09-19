package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.models.Vehiculo;
import org.entregasayd.sistemasentregas.repositories.VehiculoRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<Vehiculo> findAll(){
        return vehiculoRepository.findAll();
    }

    public Vehiculo create(Vehiculo vehiculo){
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo update(Vehiculo vehiculo){
        Vehiculo vehiculoUpdate = vehiculoRepository.findById(vehiculo.getIdVehiculo()).orElse(null);
        if(vehiculoUpdate == null){
            throw new ErrorApi(404,"El vehiculo no existe");
        }
        vehiculoUpdate.setMarca(vehiculo.getMarca());
        vehiculoUpdate.setModelo(vehiculo.getModelo());
        vehiculoUpdate.setAnio(vehiculo.getAnio());
        vehiculoUpdate.setTipoVehiculo(vehiculo.getTipoVehiculo());
        vehiculoUpdate.setPlaca(vehiculo.getPlaca());
        vehiculoUpdate.setColor(vehiculo.getColor());
        vehiculoUpdate.setEstadoVehiculo(vehiculo.getEstadoVehiculo());
        return vehiculoRepository.save(vehiculoUpdate);
    }

    public Vehiculo findById(Long id){
        return vehiculoRepository.findById(id).orElse(null);
    }


}
