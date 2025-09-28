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
@RequestMapping("/coordinador/cancelaciones")
class CancelacionCoordinadorController {

    private final CancelacionesCoordinadorService cancelacionCoordinadorService;

    public CancelacionCoordinadorController(CancelacionesCoordinadorService cancelacionCoordinadorService) {
        this.cancelacionCoordinadorService = cancelacionCoordinadorService;
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<?> obtenerSolicitudesCancelacion() {
        try{
            var solicitudes = cancelacionCoordinadorService.obtenerSolicitudesCancelacion();
            return ResponseEntity.ok(solicitudes);
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    //Aceptar solicitud de cancelacion
    @PostMapping("/solicitud/aceptar")
    public ResponseEntity<?> aceptarSolicitudCancelacion(@RequestBody AutorizacionCancelacionDTO request){
        try{
            String resultado = cancelacionCoordinadorService.autorizarSolicitudCancelacion(request);
            return ResponseEntity.ok(Map.of("message", resultado));
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/solicitud/cancelar/{idSolicitud}")
    public ResponseEntity<?> cancelarSolicitudCancelacion(@PathVariable Long idSolicitud){
        try{
            String resultado = cancelacionCoordinadorService.rechazarSolicitudCancelacion(idSolicitud);
            return ResponseEntity.ok(Map.of("message", resultado));
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


}
