package org.entregasayd.sistemasentregas.controllers.cancelaciones;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.cancelaciones.*;
import org.entregasayd.sistemasentregas.services.cancelaciones.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.Map;

@RestController
@RequestMapping("/cliente/cancelaciones")
class CancelacionClienteController {

    private final CancelacionClienteService cancelacionClienteService;

    public CancelacionClienteController(CancelacionClienteService cancelacionClienteService) {
        this.cancelacionClienteService = cancelacionClienteService;
    }

    //Revisa las cancelaciones del cliente
    @PostMapping("/guias/incidencia")
    public ResponseEntity<?> registrarIncidenciaCliente(@RequestBody IncidenciaCancelacionDTO incidenciaDTO) {
        try{
            String resultado = cancelacionClienteService.registrarIncidenciaCliente(incidenciaDTO);
            return ResponseEntity.ok(Map.of("message", resultado));
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}
