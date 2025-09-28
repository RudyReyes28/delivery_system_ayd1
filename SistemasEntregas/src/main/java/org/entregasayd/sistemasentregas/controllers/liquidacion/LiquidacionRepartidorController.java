package org.entregasayd.sistemasentregas.controllers.liquidacion;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.liquidacion.*;
import org.entregasayd.sistemasentregas.services.liquidacion.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/liquidacion/repartidor")
class LiquidacionRepartidorController {

    private final LiquidacionRepartidorService liquidacionRepartidorService;

    public LiquidacionRepartidorController(LiquidacionRepartidorService liquidacionRepartidorService) {
        this.liquidacionRepartidorService = liquidacionRepartidorService;
    }

    //Obtener los repartidores
    @GetMapping("/liquidaciones")
    public ResponseEntity<?> getRepartidoresWithLiquidacion() {
        try {
            return ResponseEntity.ok(liquidacionRepartidorService.obtenerRepartidoresConPeriodoLiquidacionActivo());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los repartidores con liquidacion: " + e.getMessage());
        }
    }

    //Nuevo periodo de liquidacion
    @PostMapping("/nuevo-periodo")
    public ResponseEntity<?> crearNuevoPeriodoLiquidacion(@RequestBody NuevoPeriodoLiquidacion periodoDTO) {
        try {
            String nuevoPeriodo = liquidacionRepartidorService.crearNuevoPeriodo(periodoDTO);
            Map<String, String> response = Map.of("message", nuevoPeriodo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //Procesar liquidacion de un repartidor
    @PostMapping("/procesar-liquidacion")
    public ResponseEntity<?> procesarLiquidacionRepartidor(@RequestBody AgregarPeriodoRepartidorDTO procesarDTO) {
        try {
            String resultado = liquidacionRepartidorService.agregarRepartidorAPeriodo(procesarDTO);
            Map<String, String> response = Map.of("message", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //Obtener el periodo de liquidacion activo
    @GetMapping("/periodo-activo")
    public ResponseEntity<?> getPeriodoLiquidacionActivo() {
        try {
            RepartidorLiquidacionDTO.PeriodoLiquidacionDTO periodo = liquidacionRepartidorService.obtenerPeriodosLiquidacion();
            return ResponseEntity.ok(periodo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el periodo de liquidacion activo: " + e.getMessage());
        }
    }

    //Cerrar periodo de liquidacion
    @PostMapping("/cerrar-periodo/{idPeriodo}")
    public ResponseEntity<?> cerrarPeriodoLiquidacion(@PathVariable Long idPeriodo) {
        try {
            String resultado = liquidacionRepartidorService.cerrarPeriodoLiquidacion(idPeriodo);
            Map<String, String> response = Map.of("message", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
