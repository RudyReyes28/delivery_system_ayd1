package org.entregasayd.sistemasentregas.controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
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

    @PutMapping("/actualizar-comision-contrato")
    public ContratoComisionDTO udpate(@RequestBody ContratoComisionDTO contratoComisionDTO) {
        return map.toComisionDTO(contratoComisionService.update(contratoComisionDTO));
    }

}
