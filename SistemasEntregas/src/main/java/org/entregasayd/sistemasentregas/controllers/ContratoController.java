package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.contrato.ContratoRequestDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.dto.contrato.RegisterContratoDTO;
import org.entregasayd.sistemasentregas.mapper.ContratoMap;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.entregasayd.sistemasentregas.services.ContratoComisionService;
import org.entregasayd.sistemasentregas.services.ContratoService;
import org.entregasayd.sistemasentregas.services.EmpleadoService;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
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
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private ContratoMap map;

    @PostMapping("/crear-contrato")
    public ContratoResponseDTO crearContrato(@RequestBody RegisterContratoDTO registerContratoDTO) {
        return contratoService.createWithComision(registerContratoDTO.getContrato(), registerContratoDTO.getComision());
    }

    @PutMapping("/actualizar-contrato")
    public ContratoResponseDTO actualizarContrato(@RequestBody ContratoResponseDTO contratoResponseDTO) {
        return map.toDTO(contratoService.update(contratoResponseDTO));
    }

    @GetMapping("/contratos-por-empleado/{id}")
    public List<ContratoResponseDTO> contratosPorEmpleado(@PathVariable Long id) {
        return contratoService.contratosPorEmpleado(id);
    }

}
