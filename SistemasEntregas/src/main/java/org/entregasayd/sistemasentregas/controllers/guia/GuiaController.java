package org.entregasayd.sistemasentregas.controllers.guia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.guias.*;
import org.entregasayd.sistemasentregas.services.guias.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sucursal/guia")
class GuiaController {

    private final GuiaSucursalService guiaSucursalService;
    @Autowired
    public GuiaController(GuiaSucursalService guiaSucursalService) {
        this.guiaSucursalService = guiaSucursalService;
    }

    //Obtenemos los clientes para asignar a una guia
    @GetMapping("/clientes")
    public ResponseEntity<?> getClientsForGuide() {
        try {
            return ResponseEntity.ok(guiaSucursalService.getClientes());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener los clientes: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtenemos los tipos de servicio para asignar a una guia
    @GetMapping("/tipos-servicio")
    public ResponseEntity<?> getServiceTypesForGuide() {
        try {
            return ResponseEntity.ok(guiaSucursalService.getTipoServicios());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al obtener los tipos de servicio: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Crear una guia con cliente nuevo
    @PostMapping("/nueva-guia-cliente-nuevo")
    public ResponseEntity<?> createGuideWithNewClient(@RequestBody CrearGuiaConClienteDTO guiaDTO) {
        try {
            Guia guia = guiaSucursalService.crearGuiaConClienteNuevo(guiaDTO);
            Map<String, String > map = new HashMap<>();
            map.put("message", "Guia creada exitosamente con numero de guia: " + guia.getNumeroGuia());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al crear la guia: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Crear una guia con cliente existente
    @PostMapping("/nueva-guia-cliente-existente")
    public ResponseEntity<?> createGuideWithExistingClient(@RequestBody CrearGuiaSinClienteDTO guiaDTO) {
        try {
            Guia guia = guiaSucursalService.crearGuiaConClienteExistente(guiaDTO);
            Map<String, String > map = new HashMap<>();
            map.put("message", "Guia creada exitosamente con numero de guia: " + guia.getNumeroGuia());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al crear la guia: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    //Obtener todas las guias de la sucursal
    @GetMapping("/listar-guias/{idUsuario}")
    public ResponseEntity<?> listAllGuides(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(guiaSucursalService.getGuiasDeSucursal(idUsuario));
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Error al listar las guias: " + e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }
}
