package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.contrato.ContratoRequestDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.dto.contrato.RegisterContratoDTO;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.entregasayd.sistemasentregas.services.ContratoComisionService;
import org.entregasayd.sistemasentregas.services.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;
    @Autowired
    private ContratoComisionService contratoComisionService;

    @PostMapping("/crear-contrato")
    public ContratoResponseDTO crearContrato(@RequestBody RegisterContratoDTO registerContratoDTO) {
        return contratoService.createWithComision(registerContratoDTO.getContrato(), registerContratoDTO.getComision());
    }
}
