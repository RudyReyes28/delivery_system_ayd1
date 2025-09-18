package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.entregasayd.sistemasentregas.repositories.ContratoComisionRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratoComisionService {
    @Autowired
    private ContratoComisionRepository repository;

    public ContratoComision create(ContratoComision contratoComision){
        return repository.save(contratoComision);
    }

    public ContratoComision findById(Long id){
        return repository.findById(id).orElseThrow(()-> new ErrorApi(404, "Detalle del contrato no encontrado"));
    }

    public ContratoComision update(ContratoComisionDTO contratoComision){
        Optional<ContratoComision> comisionActual = repository.findById(contratoComision.getIdContrato());
        if(comisionActual.isEmpty()){
            throw new ErrorApi(404, "Detalle del contrato no encontrado");
        }
        ContratoComision contratoUpdate = comisionActual.get();
        contratoUpdate.setTipoComision(contratoComision.getTipoComision());
        contratoUpdate.setPorcentaje(contratoComision.getPorcentaje());
        contratoUpdate.setMontoFijo(contratoComision.getMontoFijo());
        contratoUpdate.setAplicaDesde(contratoComision.getAplicaDesde());
        contratoUpdate.setAplicaHasta(contratoComision.getAplicaHasta());
        contratoUpdate.setActivo(contratoComision.getActivo());
        contratoUpdate.setMinimoEntregasMes(contratoComision.getMinimoEntregasMes());
        contratoUpdate.setMaximoEntregasMes(contratoComision.getMaximoEntregasMes());
        contratoUpdate.setFactorMultiplicador(contratoComision.getFactorMultiplicador());
        return repository.save(contratoUpdate);
    }

    public List<ContratoComision> findAll(){
        return (List<ContratoComision>) repository.findAll();
    }
}
