package org.entregasayd.sistemasentregas.services;

import jakarta.transaction.Transactional;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoRequestDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.mapper.ContratoMap;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.entregasayd.sistemasentregas.models.Empleado;
import org.entregasayd.sistemasentregas.repositories.ContratoRepository;
import org.entregasayd.sistemasentregas.repositories.EmpleadoRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContratoService {
    @Autowired
    private ContratoRepository repository;
    @Autowired
    private ContratoComisionService comisionService;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ContratoComisionService contratoComisionService;
    @Autowired
    private ContratoMap contratoMap;

    public Contrato create(Contrato contrato) {
        return repository.save(contrato);
    }

    public Contrato findById(Long id){
        return repository.findByIdContrato(id);
    }

    public Contrato searchByNumberContrato(String numeroContrato){
        return repository.findByNumeroContrato(numeroContrato);
    }

    @Transactional
    public ContratoResponseDTO createWithComision(
            ContratoRequestDTO contratoRequestDTO,
            ContratoComisionDTO contratoComisionRequestDTO){
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(contratoRequestDTO.getIdEmpleado());
        if (empleadoOptional.isEmpty()) {
            throw new ErrorApi(404,"Empleado no encontrado");
        }

        if(searchByNumberContrato(contratoRequestDTO.getNumeroContrato()) != null){
            throw new ErrorApi(404,String.format("El numero del contrato %s ya existe",contratoRequestDTO.getNumeroContrato()));
        }

        Empleado empleado = empleadoOptional.get();
        Contrato contrato = new Contrato();
        contrato.setEmpleado(empleado);
        contrato.setNumeroContrato(contratoRequestDTO.getNumeroContrato());
        contrato.setTipoContrato(contratoRequestDTO.getTipoContrato());
        contrato.setModalidadTrabajo(contratoRequestDTO.getModalidadTrabajo());
        contrato.setFechaInicio(contratoRequestDTO.getFechaInicio());
        contrato.setFechaFin(contratoRequestDTO.getFechaFin());
        contrato.setRenovacionAutomatica(contratoRequestDTO.getRenovacionAutomatica());
        contrato.setSalarioBase(contratoRequestDTO.getSalarioBase());
        contrato.setFrecuenciaPago(contratoRequestDTO.getFrecuenciaPago());
        contrato.setIncluyeAguinaldo(contratoRequestDTO.getIncluyeAguinaldo());
        contrato.setIncluyeBono14(contratoRequestDTO.getIncluyeBono14());
        contrato.setIncluyeVacaciones(contratoRequestDTO.getIncluyeVacaciones());
        contrato.setEstadoContrato(contratoRequestDTO.getEstadoContrato());

        Contrato contratoSave = repository.save(contrato);

        ContratoComision contratoComision = new ContratoComision();
        contratoComision.setContrato(contratoSave);
        contratoComision.setTipoComision(contratoComisionRequestDTO.getTipoComision());
        contratoComision.setPorcentaje(contratoComisionRequestDTO.getPorcentaje());
        contratoComision.setMontoFijo(contratoComisionRequestDTO.getMontoFijo());
        contratoComision.setAplicaDesde(contratoComisionRequestDTO.getAplicaDesde());
        contratoComision.setAplicaHasta(contratoComisionRequestDTO.getAplicaHasta());
        contratoComision.setMinimoEntregasMes(contratoComisionRequestDTO.getMinimoEntregasMes());
        contratoComision.setMaximoEntregasMes(contratoComisionRequestDTO.getMaximoEntregasMes());
        contratoComision.setFactorMultiplicador(contratoComisionRequestDTO.getFactorMultiplicador());
        comisionService.create(contratoComision);
        return contratoMap.toDTO(contratoSave);
    }

    public Contrato update(ContratoResponseDTO contrato){
        Contrato contratoUpdate = repository.findByIdContrato(contrato.getIdContrato());
        if(contratoUpdate == null){
            throw new ErrorApi(404, "Contrato no encontrado.");
        }
        contratoUpdate.setNumeroContrato(contrato.getNumeroContrato());
        contratoUpdate.setTipoContrato(contrato.getTipoContrato());
        contratoUpdate.setModalidadTrabajo(contrato.getModalidadTrabajo());
        contratoUpdate.setFechaInicio(contrato.getFechaInicio());
        contratoUpdate.setFechaFin(contrato.getFechaFin());
        contratoUpdate.setRenovacionAutomatica(contrato.getRenovacionAutomatica());
        contratoUpdate.setSalarioBase(contrato.getSalarioBase());
        contratoUpdate.setFrecuenciaPago(contrato.getFrecuenciaPago());
        contratoUpdate.setIncluyeAguinaldo(contrato.getIncluyeAguinaldo());
        contratoUpdate.setIncluyeBono14(contrato.getIncluyeBono14());
        contratoUpdate.setIncluyeVacaciones(contrato.getIncluyeVacaciones());
        contratoUpdate.setEstadoContrato(contrato.getEstadoContrato());
        return repository.save(contratoUpdate);
    }

    public List<Contrato> findAll(){
        return repository.findAll();
    }

    /**
     * Lista todos los contratos
     *
     * @return
     */
    public List<ContratoResponseDTO> getAll(){
        return repository.findAll().stream().map(contratoMap::toDTO).collect(Collectors.toList());
    }

    public List<ContratoResponseDTO> contratosPorEmpleado(Long idEmpleado){
        //return repository.findByEmpleado_IdEmpleado(idEmpleado).stream().map(contrato -> contratoMap.toDTO(contrato)).collect(Collectors.toList());
        return repository.findByEmpleado_IdEmpleado(idEmpleado).stream().map(contratoMap::toDTO).collect(Collectors.toList());
    }

}
