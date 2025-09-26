package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.contrato.ComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.entregasayd.sistemasentregas.repositories.ContratoComisionRepository;
import org.entregasayd.sistemasentregas.repositories.ContratoRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratoComisionService {
    @Autowired
    private ContratoComisionRepository repository;
    @Autowired
    private ContratoRepository contratoRepository;

    public ContratoComision create(ContratoComision contratoComision) {
        return repository.save(contratoComision);
    }

    public ContratoComision findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ErrorApi(404, "Detalle del contrato no encontrado"));
    }

    public ContratoComision findByContrato(Long idContrato) {
        return repository.findByContrato_IdContrato(idContrato);
    }

    public ContratoComision update(ContratoComisionDTO contratoComision) {
        Optional<ContratoComision> comisionActual = repository.findById(contratoComision.getIdContratoComision());
        if (comisionActual.isEmpty()) {
            throw new ErrorApi(404, "Detalle del contrato no encontrado");
        }
        Contrato contratoActual = contratoRepository
                .findById(
                        contratoComision.getIdContrato())
                .orElseThrow(() -> new ErrorApi(404, "Contrato no encontrado"));

        ContratoComision comisionActualizar = comisionActual.get();

        comisionActualizar.setContrato(contratoActual);

        comisionActualizar.setTipoComision(contratoComision.getTipoComision());
        comisionActualizar.setPorcentaje(contratoComision.getPorcentaje());
        comisionActualizar.setMontoFijo(contratoComision.getMontoFijo());
        comisionActualizar.setAplicaDesde(contratoComision.getAplicaDesde());
        comisionActualizar.setAplicaHasta(contratoComision.getAplicaHasta());
        comisionActualizar.setActivo(contratoComision.getActivo());
        comisionActualizar.setMinimoEntregasMes(contratoComision.getMinimoEntregasMes());
        comisionActualizar.setMaximoEntregasMes(contratoComision.getMaximoEntregasMes());
        comisionActualizar.setFactorMultiplicador(contratoComision.getFactorMultiplicador());
        return repository.save(comisionActualizar);
    }

    public List<ContratoComision> findAll() {
        return (List<ContratoComision>) repository.findAll();
    }
}
