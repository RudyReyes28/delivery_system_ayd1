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
@RequestMapping("/sucursal/cancelaciones")
class CancelacionGuiaController {

    private final CancelacionSucursalService cancelacionGuiaService;

    public CancelacionGuiaController(CancelacionSucursalService cancelacionGuiaService) {
        this.cancelacionGuiaService = cancelacionGuiaService;
    }

    @GetMapping("/guias/{idUsuario}")
    public ResponseEntity<?> obtenerGuiasParaCancelacion(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(cancelacionGuiaService.obtenerGuiasParaCancelacion(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/guias/cancelar")
    public ResponseEntity<?> cancelarGuia(@RequestBody CancelacionGuiaDTO request) {
        try{
            String resultado = cancelacionGuiaService.cancelarGuia(request);
            return ResponseEntity.ok(Map.of("message", resultado));
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}
