package org.entregasayd.sistemasentregas.services.companys;
import lombok.extern.slf4j.Slf4j;
import org.entregasayd.sistemasentregas.dto.authenticate.RegisterUserDTO;
import org.entregasayd.sistemasentregas.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.repositories.EmpresaRepository;
import org.entregasayd.sistemasentregas.repositories.EmpresaContactoRepository;
import org.entregasayd.sistemasentregas.dto.empresas.*;
import org.entregasayd.sistemasentregas.models.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Service
public class CompanyService {

    @Autowired
    private EmpresaRepository companyRepository;

    @Autowired
    private EmpresaContactoRepository companyContactRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para crear una empresa junto con su contacto
    @Transactional
    public Empresa createCompany(RegisterCompanyDTO companyDTO) throws Exception {
        try {
            //Verificar si ya existe una empresa con el mismo NIT
            if (companyRepository.existsByNit(companyDTO.getNit())) {
                throw new Exception("Ya existe una empresa con el mismo NIT");
            }

            // Crear y guardar la empresa
            Empresa company = new Empresa();
            company.setTipoEmpresa(Empresa.TipoEmpresa.valueOf(companyDTO.getTipoEmpresa()));
            company.setNombreComercial(companyDTO.getNombreComercial());
            company.setRazonSocial(companyDTO.getRazonSocial());
            company.setNit(companyDTO.getNit());
            company.setRegistroMercantil(companyDTO.getRegistroMercantil());
            if (companyDTO.getFechaConstitucion() != null) {
                company.setFechaConstitucion(LocalDate.parse(companyDTO.getFechaConstitucion()));
            }
            company.setFechaAfiliacion(LocalDate.parse(companyDTO.getFechaAfiliacion()));
            if (companyDTO.getFechaVencimientoAfiliacion() != null) {
                company.setFechaVencimientoAfiliacion(LocalDate.parse(companyDTO.getFechaVencimientoAfiliacion()));
            }
            company.setEstado(Empresa.Estado.ACTIVA);
            Empresa savedCompany = companyRepository.save(company);

            // Crear y guardar el contacto de la empresa
            EmpresaContacto companyContact = new EmpresaContacto();
            Persona persona = new Persona();
            persona.setIdPersona(companyDTO.getIdPersona());
            companyContact.setEmpresa(savedCompany);
            companyContact.setPersona(persona);
            companyContact.setCargo(companyDTO.getCargo());
            companyContact.setFechaInicio(LocalDate.parse(companyDTO.getFechaInicio()));
            if (companyDTO.getFechaFin() != null) {
                companyContact.setFechaFin(LocalDate.parse(companyDTO.getFechaFin()));
            }
            companyContact.setActivo(companyDTO.getActivo() != null ? companyDTO.getActivo() : true);
            companyContactRepository.save(companyContact);

            return savedCompany;
        } catch (Exception e) {
            log.error("Error creating company: {}", e.getMessage());
            throw new Exception("Ha ocurrido un error al crear la empresa"); // Re-lanzar la excepción para que la transacción se revierta
        }

    }

    //Obtener todas las empresas y su contacto
    public ArrayList<GetEmpresasDTO> getAllCompanies() {
        ArrayList<Empresa> companies = (ArrayList<Empresa>) companyRepository.findAll();
        ArrayList<GetEmpresasDTO> companiesDTO = new ArrayList<>();

        for (Empresa company : companies) {
            EmpresaContacto companyContact = companyContactRepository.findByEmpresaIdEmpresa(company.getIdEmpresa());
            GetEmpresasDTO.ContactoDTO contactDTO = null;
            if (companyContact != null) {
                contactDTO = GetEmpresasDTO.ContactoDTO.builder()
                        .idEmpresaContacto(companyContact.getIdEmpresaContacto())
                        .idPersona(companyContact.getPersona().getIdPersona())
                        .cargo(companyContact.getCargo())
                        .fechaInicio(companyContact.getFechaInicio() != null ? companyContact.getFechaInicio().toString() : null)
                        .fechaFin(companyContact.getFechaFin() != null ? companyContact.getFechaFin().toString() : null)
                        .activo(companyContact.getActivo())
                        .build();
            }

            GetEmpresasDTO.EmpresaDTO companyDTO = GetEmpresasDTO.EmpresaDTO.builder()
                    .idEmpresa(company.getIdEmpresa())
                    .tipoEmpresa(company.getTipoEmpresa().name())
                    .nombreComercial(company.getNombreComercial())
                    .razonSocial(company.getRazonSocial())
                    .nit(company.getNit())
                    .registroMercantil(company.getRegistroMercantil())
                    .fechaConstitucion(company.getFechaConstitucion() != null ? company.getFechaConstitucion().toString() : null)
                    .fechaAfiliacion(company.getFechaAfiliacion() != null ? company.getFechaAfiliacion().toString() : null)
                    .fechaVencimientoAfiliacion(company.getFechaVencimientoAfiliacion() != null ? company.getFechaVencimientoAfiliacion().toString() : null)
                    .estado(company.getEstado().name())
                    .contacto(contactDTO)
                    .build();

            companiesDTO.add(GetEmpresasDTO.builder().empresa(companyDTO).build());
        }
        return companiesDTO;
    }



    //Cambiar estado de una empresa
    @Transactional
    public Empresa changeCompanyState(ChangeStateCompanyDTO changeState) throws Exception {
        try {
            Empresa company = companyRepository.findById(changeState.getIdEmpresa())
                    .orElseThrow(() -> new Exception("No se encontró la empresa con ID: " + changeState.getIdEmpresa()));
            company.setEstado(Empresa.Estado.valueOf(changeState.getNuevoEstado()));
            return companyRepository.save(company);
        } catch (Exception e) {
            log.error("Error changing company state: {}", e.getMessage());
            throw new Exception("Ha ocurrido un error al cambiar el estado de la empresa");
        }
    }

    //Editar empresa y contacto
    @Transactional
    public Empresa editCompany(long idEmpresa, RegisterCompanyDTO registerCompanyDTO) throws Exception {
        try {
            Empresa company = companyRepository.findById(idEmpresa)
                    .orElseThrow(() -> new Exception("No se encontró la empresa con ID: " + idEmpresa));

            // Actualizar los campos de la empresa
            company.setTipoEmpresa(Empresa.TipoEmpresa.valueOf(registerCompanyDTO.getTipoEmpresa()));
            company.setNombreComercial(registerCompanyDTO.getNombreComercial());
            company.setRazonSocial(registerCompanyDTO.getRazonSocial());
            company.setNit(registerCompanyDTO.getNit());
            company.setRegistroMercantil(registerCompanyDTO.getRegistroMercantil());
            if (registerCompanyDTO.getFechaConstitucion() != null) {
                company.setFechaConstitucion(LocalDate.parse(registerCompanyDTO.getFechaConstitucion()));
            } else {
                company.setFechaConstitucion(null);
            }
            company.setFechaAfiliacion(LocalDate.parse(registerCompanyDTO.getFechaAfiliacion()));
            if (registerCompanyDTO.getFechaVencimientoAfiliacion() != null) {
                company.setFechaVencimientoAfiliacion(LocalDate.parse(registerCompanyDTO.getFechaVencimientoAfiliacion()));
            } else {
                company.setFechaVencimientoAfiliacion(null);
            }
            Empresa updatedCompany = companyRepository.save(company);

            // Actualizar o crear el contacto de la empresa
            EmpresaContacto companyContact = companyContactRepository.findByEmpresaIdEmpresa(idEmpresa);
            if (companyContact == null) {
                companyContact = new EmpresaContacto();
                Persona persona = new Persona();
                persona.setIdPersona(registerCompanyDTO.getIdPersona());
                companyContact.setPersona(persona);
                companyContact.setEmpresa(updatedCompany);
                companyContact.setCargo(registerCompanyDTO.getCargo());
                companyContact.setFechaInicio(LocalDate.parse(registerCompanyDTO.getFechaInicio()));
                if (registerCompanyDTO.getFechaFin() != null) {
                    companyContact.setFechaFin(LocalDate.parse(registerCompanyDTO.getFechaFin()));
                }
                companyContact.setActivo(registerCompanyDTO.getActivo() != null ? registerCompanyDTO.getActivo() : true);
                companyContactRepository.save(companyContact);

            } else {
                // Si el contacto ya existe, actualizar la persona asociada
                Persona persona = new Persona();
                persona.setIdPersona(registerCompanyDTO.getIdPersona());
                companyContact.setPersona(persona);
                companyContact.setCargo(registerCompanyDTO.getCargo());
                companyContact.setFechaInicio(LocalDate.parse(registerCompanyDTO.getFechaInicio()));
                if (registerCompanyDTO.getFechaFin() != null) {
                    companyContact.setFechaFin(LocalDate.parse(registerCompanyDTO.getFechaFin()));
                } else {
                    companyContact.setFechaFin(null);
                }
                companyContact.setActivo(registerCompanyDTO.getActivo() != null ? registerCompanyDTO.getActivo() : true);
                companyContactRepository.save(companyContact);
            }
            return updatedCompany;
        } catch (Exception e) {
            log.error("Error editing company: {}", e.getMessage());
            throw new Exception("Ha ocurrido un error al editar la empresa");
        }
    }

    //Obtener usuarios de rol SUCURSAL para asignar a una empresa
    public ArrayList<UsersSucursalDTO> getBranchUsers() {
        ArrayList<Usuario> users = usuarioRepository.findByRol_Nombre("SUCURSAL");
        ArrayList<UsersSucursalDTO> sucursalDTOs = new ArrayList<>();

        for (Usuario user : users) {
            UsersSucursalDTO sucursalDTO = UsersSucursalDTO.builder()
                    .idUsuario(user.getIdUsuario())
                    .nombreUsuario(user.getNombreUsuario())
                    .idPersona(user.getPersona().getIdPersona())
                    .idRol(user.getRol().getIdRol())
                    .nombreRol(user.getRol().getNombre())
                    .estado(user.getEstado().name())
                    .build();
            sucursalDTOs.add(sucursalDTO);

        }
        return sucursalDTOs;

    }


}
