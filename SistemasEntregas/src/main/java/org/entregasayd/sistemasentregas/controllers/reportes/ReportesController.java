package org.entregasayd.sistemasentregas.controllers.reportes;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.reportes.*;
import org.entregasayd.sistemasentregas.services.reportes.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/administrador/reportes")
class ReportesController {

    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @GetMapping("/entregas")
    public ResponseEntity<EntregasDTO> obtenerReporteEntregas() {
        try{
            EntregasDTO reporte = reportesService.getEntregasDTO();
            return ResponseEntity.ok(reporte);
        }catch(Exception e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener el reporte de entregas: ");
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/comisiones-repartidores")
    public ResponseEntity<?> obtenerReporteComisionesRepartidores() {
        try{
            List<ComisionRepartidorReporteDTO> reporte = reportesService.comisionesRepartidores();
            return ResponseEntity.ok(reporte);
        }catch(Exception e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener el reporte de comisiones de repartidores: ");
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/descuentos-aplicados")
    public ResponseEntity<?> obtenerReporteDescuentosAplicados() {
        try {
            List<DescuentosAplicadosDTO> reporte = reportesService.descuentosAplicados();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener el reporte de descuentos aplicados: ");
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/cancelaciones-empresa")
    public ResponseEntity<?> obtenerReporteCancelacionesPorEmpresa() {
        try {
            List<CancelacionesEmpresaDTO> reporte = reportesService.cancelacionesEmpresa();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener el reporte de cancelaciones por empresa: ");
            return ResponseEntity.status(500).body(null);
        }
    }




}
