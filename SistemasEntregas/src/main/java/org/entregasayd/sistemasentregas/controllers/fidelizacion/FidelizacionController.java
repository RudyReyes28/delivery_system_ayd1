package org.entregasayd.sistemasentregas.controllers.fidelizacion;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.fidelizacion.*;
import org.entregasayd.sistemasentregas.services.fidelizacion.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/empresa/fidelizacion")
class FidelizacionController {

    private final LoyaltyProgramService fidelizacionService;

    @Autowired
    public FidelizacionController(LoyaltyProgramService fidelizacionService) {
        this.fidelizacionService = fidelizacionService;
    }

    //Crear un programa de fidelización
    @PostMapping("/nuevo-programa")
    public ResponseEntity<?> createFidelizationProgram(@RequestBody CreateLoyaltyProgramDTO programDTO) {
        try {
            EmpresaFidelizacion programa = fidelizacionService.createLoyaltyProgram(programDTO);
            return ResponseEntity.ok(programa);
        } catch (Exception e) {
            //Crear un map para mandar el status y el mensaje
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al crear el programa de fidelización: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //Listar todos los programas de fidelización
    @GetMapping("/listar-programas")
    public ResponseEntity<?> listAllFidelizationPrograms() {
        try {
            return ResponseEntity.ok(fidelizacionService.getAllCompanies());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al obtener los programas de fidelización: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //Cambiar el nivel de fidelizacion
    @PutMapping("/cambiar-nivel")
    public ResponseEntity<?> changeFidelizationLevel(@RequestBody CreateLoyaltyProgramDTO levelDTO) {
        try {
            EmpresaFidelizacion programa = fidelizacionService.changeLoyaltyLevel(levelDTO);
            return ResponseEntity.ok(programa);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al cambiar el nivel de fidelización: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /*
    //Cambiar los detalles del nivel de fidelizacion
    @PutMapping("/cambiar-detalles")
    public ResponseEntity<?> changeFidelizationDetails(@RequestBody ChangeLoyaltyLevelDTO detailsDTO) {
        try {
            NivelFindelizacion programa = fidelizacionService.updateLoyaltyLevelDetails(detailsDTO);
            return ResponseEntity.ok(programa);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al cambiar los detalles del programa de fidelización: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }*/


}
