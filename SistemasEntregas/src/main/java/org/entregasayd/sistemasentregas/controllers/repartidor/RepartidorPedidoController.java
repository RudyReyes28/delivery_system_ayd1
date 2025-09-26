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
@RequestMapping("/repartidor/pedido")
class RepartidorPedidoController {

    private final PedidosRepartidorService repartidorPedidoService;
    public RepartidorPedidoController(PedidosRepartidorService repartidorPedidoService) {
        this.repartidorPedidoService = repartidorPedidoService;
    }

    //Obtenemos las asignaciones de un repartidor
    @GetMapping("/asignaciones/{idUsuario}")
    public ResponseEntity<?> getAssignmentsByDeliveryPerson(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(repartidorPedidoService.getAsignacionesRepartidor(idUsuario));
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener las asignaciones: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Repartidor acepta una asignacion
    @PostMapping("/aceptar-asignacion/{idAsignacion}")
    public ResponseEntity<?> acceptAssignment(@PathVariable Long idAsignacion) {
        try {
            String asignacion = repartidorPedidoService.aceptarAsignacion(idAsignacion);
            Map<String, String> map = new HashMap<>();
            map.put("message", asignacion);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al aceptar la asignacion: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Repartidor rechaza una asignacion
    @PostMapping("/rechazar-asignacion/{idAsignacion}")
    public ResponseEntity<?> rejectAssignment(@PathVariable Long idAsignacion, @RequestBody RechazoAsignacionDTO rechazoDTO) {
        try {
            String asignacion = repartidorPedidoService.rechazarAsignacion(idAsignacion, rechazoDTO);
            Map<String, String> map = new HashMap<>();
            map.put("message", asignacion);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al rechazar la asignacion: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtener los pedidos asignados a un repartidor
    @GetMapping("/pedidos-asignados/{idUsuario}")
    public ResponseEntity<?> getAssignedOrders(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(repartidorPedidoService.getDetallesGuiasAsignadas(idUsuario));
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener los pedidos asignados: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Cambiar el estado de una guia
    @PostMapping("/cambiar-estado")
    public ResponseEntity<?> changeOrderStatus(@RequestBody CambiarEstadoDTO cambiarEstadoDTO) {
        try {
            String estado = repartidorPedidoService.cambiarEstadoGuiaAsignada(cambiarEstadoDTO);
            Map<String, String> map = new HashMap<>();
            map.put("message", estado);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al cambiar el estado de la guia: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

}
