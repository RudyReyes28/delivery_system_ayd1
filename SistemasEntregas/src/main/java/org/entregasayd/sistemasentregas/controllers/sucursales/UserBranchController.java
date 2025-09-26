package org.entregasayd.sistemasentregas.controllers.sucursales;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.sucursales.*;
import org.entregasayd.sistemasentregas.services.sucursales.*;
import org.entregasayd.sistemasentregas.models.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sucursal/manager")
class UserBranchController {
    private final UserBranchService userBranchService;

    @Autowired
    public UserBranchController(UserBranchService userBranchService) {
        this.userBranchService = userBranchService;
    }

    //Obtener la empresa y la sucursal del usuario logueado
    @GetMapping("/informacion/{idUsuario}")
    public ResponseEntity<?> getBranchCompanyInfo(@PathVariable long idUsuario) {
        try {
            return ResponseEntity.ok(userBranchService.getInformationBranchCompany(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener la información: " + e.getMessage());
        }
    }

    //Obtener fidelizacion de la empresa del usuario logueado
    @GetMapping("/fidelizacion/{idUsuario}")
    public ResponseEntity<?> getCompanyFidelization(@PathVariable long idUsuario) {
        try {
            return ResponseEntity.ok(userBranchService.getFidelizacionCompany(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener la información: " + e.getMessage());
        }
    }

}
