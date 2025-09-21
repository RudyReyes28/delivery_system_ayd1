package org.entregasayd.sistemasentregas.controllers.guia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.guias.*;
import org.entregasayd.sistemasentregas.services.guias.*;
import org.entregasayd.sistemasentregas.models.*;

@Controller
@RequestMapping("/public/guia")
class GuiaClienteController {
    @Autowired
    private GuiaClienteService guiaClienteService;

    //Endpoint para devolver el detalle de una guia para el cliente
    @GetMapping("/detalle/{numeroGuia}")
    public ResponseEntity<GuiaDetalleClienteDTO> getDetalleGuiaCliente(@PathVariable String numeroGuia) {
        try {
            GuiaDetalleClienteDTO guiaDetalleClienteDTO = guiaClienteService.getDetalleGuiaCliente(numeroGuia);
            return ResponseEntity.ok(guiaDetalleClienteDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
