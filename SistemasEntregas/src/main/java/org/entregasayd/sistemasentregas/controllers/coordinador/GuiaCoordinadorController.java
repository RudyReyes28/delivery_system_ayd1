package org.entregasayd.sistemasentregas.controllers.coordinador;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.coordinador.*;
import org.entregasayd.sistemasentregas.services.coordinador.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/coordinador/guias")
class GuiaCoordinadorController {

    private final GuiasCoordinadorService guiaCoordinadorService;
    @Autowired
    public GuiaCoordinadorController(GuiasCoordinadorService guiaCoordinadorService) {
        this.guiaCoordinadorService = guiaCoordinadorService;
    }

    //Obtenemos todas las guias
    @GetMapping("/obtener-guias")
    public ResponseEntity<?> getAllGuides() {
        try {
            return ResponseEntity.ok(guiaCoordinadorService.getTodasLasGuias());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener las guias: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtenes todas las guias sin repartidor
    @GetMapping("/guias-sin-repartidor")
    public ResponseEntity<?> getGuidesWithoutDeliveryPerson() {
        try {
            return ResponseEntity.ok(guiaCoordinadorService.getGuiasSinRepartidor());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener las guias sin repartidor: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtener todos los repartidores
    @GetMapping("/repartidores")
    public ResponseEntity<?> getAllDeliveryPersons() {
        try {
            return ResponseEntity.ok(guiaCoordinadorService.getDetallesRepartidores());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener los repartidores: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtener todos los repartidores activos
    @GetMapping("/repartidores-activos")
    public ResponseEntity<?> getAllActiveDeliveryPersons() {
        try {
            return ResponseEntity.ok(guiaCoordinadorService.getRepartidoresConContratoActivo());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener los repartidores activos: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Asignar un repartidor a una guia
    @PostMapping("/asignar-repartidor")
    public ResponseEntity<?> assignDeliveryPersonToGuide(@RequestBody AsignacionGuiaDTO asignarRepartidorDTO) {
        try {
            String guia = guiaCoordinadorService.asignarRepartidorAGuia(asignarRepartidorDTO);
            Map<String, String> map = new HashMap<>();
            map.put("message", guia);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al asignar el repartidor a la guia: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

}
