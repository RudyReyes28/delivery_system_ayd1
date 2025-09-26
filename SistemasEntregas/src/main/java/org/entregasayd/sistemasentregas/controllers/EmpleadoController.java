package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.user.EmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.mapper.EmpleadoMap;
import org.entregasayd.sistemasentregas.models.Empleado;
import org.entregasayd.sistemasentregas.repositories.EmpleadoRepository;
import org.entregasayd.sistemasentregas.services.EmpleadoService;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository repository;
    @Autowired
    private EmpleadoMap map;

    @GetMapping("/all")
    public List<EmpleadoRequestDTO> getAll(){
        return empleadoService.getAll().stream().map(map::toDto).collect(Collectors.toList());
    }

    @GetMapping("/get-by-idUser/{idUser}")
    public EmpleadoRequestDTO getByIdUser(@PathVariable Long idUser){
        return map.toDto(empleadoService.getByIdUsuario(idUser));
    }
    @PutMapping("/actualizar-empleado")
    public EmpleadoRequestDTO updateEmployee(@RequestBody EmpleadoRequestDTO empleadoRequestDTO){
        Optional<Empleado> empleadoOptional = repository.findById(empleadoRequestDTO.getIdEmpleado());
        if(empleadoOptional.isEmpty()){
           throw new ErrorApi(404,"Empleado no encontrado.");
        }
        Empleado empleadoUpdate = empleadoOptional.get();
        empleadoUpdate.setCodigoEmpleado(empleadoRequestDTO.getCodigoEmpleado());
        empleadoUpdate.setNumeroIgss(empleadoRequestDTO.getNumeroIgss());
        empleadoUpdate.setNumeroIrtra(empleadoRequestDTO.getNumeroIrtra());
        empleadoUpdate.setTipoSangre(empleadoRequestDTO.getTipoSangre());
        empleadoUpdate.setEstadoCivil(empleadoRequestDTO.getEstadoCivil());
        empleadoUpdate.setNumeroDependientes(empleadoRequestDTO.getNumeroDependientes());
        empleadoUpdate.setContactoEmergenciaNombre(empleadoRequestDTO.getContactoEmergenciaNombre());
        empleadoUpdate.setContactoEmergenciaTelefono(empleadoRequestDTO.getContactoEmergenciaTelefono());
        empleadoUpdate.setEstadoEmpleado(empleadoRequestDTO.getEstadoEmpleado());
        empleadoUpdate.setFechaIngreso(empleadoRequestDTO.getFechaIngreso());
        empleadoUpdate.setFechaSalida(empleadoRequestDTO.getFechaSalida());
        empleadoUpdate.setMotivoSalida(empleadoRequestDTO.getMotivoSalida());
        return map.toDto(empleadoService.update(empleadoUpdate));
    }
}
