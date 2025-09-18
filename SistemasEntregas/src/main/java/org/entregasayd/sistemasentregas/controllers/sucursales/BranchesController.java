package org.entregasayd.sistemasentregas.controllers.sucursales;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.sucursales.*;
import org.entregasayd.sistemasentregas.services.sucursales.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/empresa/sucursal")
class BranchesController {

    private final BranchService branchesService;

    @Autowired
    public BranchesController(BranchService branchesService) {
        this.branchesService = branchesService;
    }

    //Obtener usuarios para asignar a una sucursal
    @GetMapping("/usuarios-sucursal")
    public ResponseEntity<?> getUsersForBranch() {
        try {
            return ResponseEntity.ok(branchesService.getBranchUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    //Obtener todas las empresas
    @GetMapping("/listar-empresas")
    public ResponseEntity<?> getAllCompanies() {
        try {
            return ResponseEntity.ok(branchesService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener las empresas: " + e.getMessage());
        }
    }

    //Crear una sucursal
    @PostMapping("/nueva-sucursal")
    public ResponseEntity<?> createBranch(@RequestBody RegisterBranchDTO branchDTO) {
        try {
            Sucursal sucursal = branchesService.registerBranch(branchDTO);
            return ResponseEntity.ok(sucursal);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear la sucursal: " + e.getMessage());
        }
    }

    //Listar todas las sucursales
    @GetMapping("/listar-sucursales")
    public ResponseEntity<?> listAllBranches() {
        try {
            return ResponseEntity.ok(branchesService.getAllBranches());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al listar las sucursales: " + e.getMessage());
        }
    }

    //Cambiar estado de una sucursal
    @PutMapping("/cambiar-estado")
    public ResponseEntity<?> changeBranchStatus( @RequestBody ChangeStateBranchDTO statusDTO) {
        try {
            Sucursal updatedBranch = branchesService.changeBranchStatus(statusDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Estado de la sucursal actualizado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cambiar el estado de la sucursal: " + e.getMessage());
        }
    }



}
