package org.entregasayd.sistemasentregas.controllers.company;

import org.entregasayd.sistemasentregas.dto.empresas.ChangeStateCompanyDTO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.entregasayd.sistemasentregas.dto.empresas.RegisterCompanyDTO;
import org.entregasayd.sistemasentregas.services.companys.CompanyService;
import org.entregasayd.sistemasentregas.models.*;

@RestController
@RequestMapping("/empresa")
class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Crear una empresa junto con su contacto
    @PostMapping("/nueva-empresa")
    public ResponseEntity<?> createCompany(@RequestBody RegisterCompanyDTO companyDTO) {
        try {
            Empresa empresa = companyService.createCompany(companyDTO);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear la empresa: " + e.getMessage());
        }
    }

    //Obtener todas las empresas
    @GetMapping("/listar-empresas")
    public ResponseEntity<?> getAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener las empresas: " + e.getMessage());
        }
    }

    //Cambiar estado de una empresa
    @PutMapping("/cambiar-estado")
    public ResponseEntity<?> changeCompanyState(@RequestBody ChangeStateCompanyDTO changeState) {
        try {
            Empresa empresa = companyService.changeCompanyState(changeState);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cambiar el estado de la empresa: " + e.getMessage());
        }
    }

    //Editar una empresa
    @PutMapping("/editar-empresa/{idEmpresa}")
    public ResponseEntity<?> editCompany(@PathVariable Long idEmpresa, @RequestBody RegisterCompanyDTO companyDTO) {
        try {
            Empresa empresa = companyService.editCompany(idEmpresa, companyDTO);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al editar la empresa: " + e.getMessage());
        }
    }

    //Obtener usuarios para asignar a una empresa
    @GetMapping("/usuarios-sucursal")
    public ResponseEntity<?> getUsersForCompany() {
        try {
            return ResponseEntity.ok(companyService.getBranchUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

}
