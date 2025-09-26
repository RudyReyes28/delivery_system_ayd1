package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.models.Empleado;
import org.entregasayd.sistemasentregas.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado create(Empleado empleado){
        return empleadoRepository.save(empleado);
    }

    public Empleado update(Empleado empleado){
        return empleadoRepository.save(empleado);
    }

    public Empleado getByIdUsuario(Long idUsuario){
        return empleadoRepository.getByUsuario_IdUsuario(idUsuario);
    }

    public Empleado findById(Long id){
        return empleadoRepository.findById(id).orElse(null);
    }

    public List<Empleado> getAll(){
        return empleadoRepository.findAll();
    }

}
