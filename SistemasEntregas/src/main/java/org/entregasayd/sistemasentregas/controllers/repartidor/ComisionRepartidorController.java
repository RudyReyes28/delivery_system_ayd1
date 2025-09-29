package org.entregasayd.sistemasentregas.controllers.repartidor;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.repartidor.*;
import org.entregasayd.sistemasentregas.services.repartidor.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/repartidor/comisiones")
class ComisionRepartidorController {

    private final ComisionRepartidorService comisionRepartidorService;
    public ComisionRepartidorController(ComisionRepartidorService comisionRepartidorService) {
        this.comisionRepartidorService = comisionRepartidorService;
    }

    //Obtenemos las comisiones de un repartidor
    @GetMapping("mis-comisiones/{idUsuario}")
    public ResponseEntity<?> getComisionesByRepartidor(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(comisionRepartidorService.obtenerComisionRepartidor(idUsuario));
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener las comisiones: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }


}
