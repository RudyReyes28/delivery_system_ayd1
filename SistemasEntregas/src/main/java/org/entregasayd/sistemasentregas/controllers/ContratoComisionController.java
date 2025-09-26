package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.contrato.ComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.mapper.ContratoMap;
import org.entregasayd.sistemasentregas.services.ContratoComisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contrato-comision")
public class ContratoComisionController {
    @Autowired
    private ContratoComisionService contratoComisionService;
    @Autowired
    private ContratoMap map;

    @GetMapping("/obtener-por-id/{id}")
    public ContratoComisionDTO obtenerPorID(@PathVariable Long id) {
        return  map.toComisionDTO(contratoComisionService.findById(id));
    }

    @GetMapping("/obtener-por-contrato/{idContrato}")
    public ContratoComisionDTO obtenerPorComision(@PathVariable Long idContrato) {
        return map.toComisionDTO(contratoComisionService.findByContrato(idContrato));
    }

    @PutMapping("/actualizar-comision")
    public ContratoComisionDTO udpate(@RequestBody ContratoComisionDTO  contratoComisionDTO) {
        System.out.println(String.format("Datos recibidos: %s",
                contratoComisionDTO.toString()));
        return map.toComisionDTO(contratoComisionService.update(contratoComisionDTO));
    }

}
