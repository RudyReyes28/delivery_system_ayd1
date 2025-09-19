package org.entregasayd.sistemasentregas.services.sucursales;

import org.entregasayd.sistemasentregas.dto.empresas.GetEmpresasDTO;
import org.entregasayd.sistemasentregas.dto.empresas.UsersSucursalDTO;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.dto.sucursales.*;

import java.util.ArrayList;
import java.time.LocalDate;

@Slf4j
@Service
public class BranchService {

    @Autowired
    private SucursalRepository branchRepository;

    @Autowired
    private SucursalPersonalRepository branchStaffRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository companyRepository;

    @Autowired
    private DireccionRepository addressRepository;

    @Autowired
    private EmpresaContactoRepository companyContactRepository;

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

    //Obtener las empresas registradas
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

    //Registrar una nueva sucursal y su personal encargado
    @Transactional
    public Sucursal registerBranch(RegisterBranchDTO branchDTO) throws Exception {
        try {

            //Creamos y guardamos la direccion de la sucursal
            Direccion address = new Direccion();
            address.setTipoDireccion(Direccion.TipoDireccion.valueOf(branchDTO.getTipoDireccion().toUpperCase()));
            address.setMunicipio(branchDTO.getMunicipio());
            address.setDepartamento(branchDTO.getDepartamento());
            address.setPais(branchDTO.getPais());
            address.setCodigoPostal(branchDTO.getCodigoPostal());
            address.setReferencias(branchDTO.getReferencias());
            address.setActiva(branchDTO.isActiva());
            Direccion savedAddress = addressRepository.save(address);

            //Crear y guardar la sucursal
            Sucursal branch = new Sucursal();
            Empresa company = companyRepository.findById(branchDTO.getIdEmpresa())
                    .orElseThrow(() -> new Exception("Empresa no encontrada con ID: " + branchDTO.getIdEmpresa()));
            branch.setEmpresa(company);
            branch.setCodigoSucursal(branchDTO.getCodigoSucursal());
            branch.setNombreSucursal(branchDTO.getNombreSucursal());
            branch.setDireccion(savedAddress);

            branch.setHorarioApertura(branchDTO.getHorarioApertura());
            branch.setHorarioCierre(branchDTO.getHorarioCierre());
            branch.setDiasOperacion(branchDTO.getDiasOperacion());

            try {
                Sucursal.Estado estado = Sucursal.Estado.valueOf(branchDTO.getEstado().toUpperCase());
                branch.setEstado(estado);
            } catch (IllegalArgumentException e) {
                throw new Exception("Estado de sucursal inválido: " + branchDTO.getEstado());
            }

            Sucursal savedBranch = branchRepository.save(branch);
            log.info("Sucursal registrada con ID: " + savedBranch.getIdSucursal());

            //Crear y guardar el personal de la sucursal
            SucursalPersonal branchStaff = new SucursalPersonal();
            Usuario user = usuarioRepository.findById(branchDTO.getIdUsuario())
                    .orElseThrow(() -> new Exception("Usuario no encontrado con ID: " + branchDTO.getIdUsuario()));

            //Como politica de la empresa, un usuario solo puede estar asignado a una sucursal
            SucursalPersonal existingStaff = branchStaffRepository.findByUsuarioIdUsuario(branchDTO.getIdUsuario());
            if (existingStaff != null) {
                throw new Exception("El usuario con ID " + branchDTO.getIdUsuario() + " ya está asignado a otra sucursal.");
            }

            branchStaff.setUsuario(user);
            branchStaff.setSucursal(savedBranch);
            branchStaff.setCargo(branchDTO.getCargo());
            branchStaff.setEsEncargado(branchDTO.isEsEncargado());

            try {
                if (branchDTO.getFechaFin() != null && !branchDTO.getFechaFin().isEmpty()) {
                    branchStaff.setFechaFin(LocalDate.parse(branchDTO.getFechaFin()));
                } else {
                    branchStaff.setFechaFin(null);
                }
            } catch (IllegalArgumentException e) {
                throw new Exception("Formato de fecha inválido para fechaFin: " + branchDTO.getFechaFin());
            }

            branchStaff.setActivo(branchDTO.isActivo());

            SucursalPersonal savedBranchStaff = branchStaffRepository.save(branchStaff);
            log.info("Personal de sucursal registrado con ID: " + savedBranchStaff.getIdSucursalPersonal());

            return savedBranch;
        } catch (Exception e) {
            log.error("Error al registrar la sucursal: " + e.getMessage());
            throw new Exception("Error al registrar la sucursal: " + e.getMessage());
        }
    }

    //Obtener todas las sucursales registradas con su informacion
    public ArrayList<GetBranchesDTO> getAllBranches() {
        ArrayList<Sucursal> branches = (ArrayList<Sucursal>) branchRepository.findAll();
        ArrayList<GetBranchesDTO> branchesDTO = new ArrayList<>();
        for (Sucursal branch : branches) {
            //Obtener la empresa asociada a la sucursal
            Empresa company = branch.getEmpresa();
            GetBranchesDTO.CompanyDTO companyDTO = GetBranchesDTO.CompanyDTO.builder()
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
                    .build();

            //Obtener la direccion de la sucursal
            Direccion address = branch.getDireccion();
            GetBranchesDTO.AddressDTO addressDTO = GetBranchesDTO.AddressDTO.builder()
                    .idDireccion(address.getIdDireccion())
                    .tipoDireccion(address.getTipoDireccion().name())
                    .municipio(address.getMunicipio())
                    .departamento(address.getDepartamento())
                    .pais(address.getPais())
                    .codigoPostal(address.getCodigoPostal())
                    .referencias(address.getReferencias())
                    .activa(address.isActiva())
                    .build();

            //Obtener el personal asociado a la sucursal
            ArrayList<SucursalPersonal> branchStaffList = branchStaffRepository.findBySucursalIdSucursal(branch.getIdSucursal());
            ArrayList<GetBranchesDTO.StaffDTO> staffDTOs = new ArrayList<>();
            for (SucursalPersonal staff : branchStaffList) {
                Usuario user = staff.getUsuario();
                GetBranchesDTO.StaffDTO staffDTO = GetBranchesDTO.StaffDTO.builder()
                        .idSucursalPersonal(staff.getIdSucursalPersonal())
                        .idUsuario(user.getIdUsuario())
                        .nombreUsuario(user.getNombreUsuario())
                        .idPersona(user.getPersona().getIdPersona())
                        .cargo(staff.getCargo())
                        .esEncargado(staff.isEsEncargado())
                        .fechaFin(staff.getFechaFin() != null ? staff.getFechaFin().toString() : null)
                        .fechaAsignacion(staff.getFechaInicio() != null ? staff.getFechaInicio().toString() : null)
                        .activo(staff.isActivo())
                        .build();
                staffDTOs.add(staffDTO);

            }
            //Crear el DTO de la sucursal
            GetBranchesDTO.BranchDTO branchDTO = GetBranchesDTO.BranchDTO.builder()
                    .idSucursal(branch.getIdSucursal())
                    .company(companyDTO)
                    .codigoSucursal(branch.getCodigoSucursal())
                    .nombreSucursal(branch.getNombreSucursal())
                    .address(addressDTO)
                    .staff(staffDTOs)
                    .horarioApertura(branch.getHorarioApertura())
                    .horarioCierre(branch.getHorarioCierre())
                    .diasOperacion(branch.getDiasOperacion())
                    .estado(branch.getEstado().name())
                    .build();

            branchesDTO.add(GetBranchesDTO.builder().branch(branchDTO).build());

        }
        return branchesDTO;
    }


    //Cambiar el estado de una sucursal
    @Transactional
    public Sucursal changeBranchStatus(ChangeStateBranchDTO changeStateBranchDTO) throws Exception {
        try {
            Sucursal branch = branchRepository.findById(changeStateBranchDTO.getIdSucursal())
                    .orElseThrow(() -> new Exception("Sucursal no encontrada con ID: " + changeStateBranchDTO.getIdSucursal()));

            try {
                Sucursal.Estado newState = Sucursal.Estado.valueOf(changeStateBranchDTO.getEstado().toUpperCase());
                branch.setEstado(newState);
            } catch (IllegalArgumentException e) {
                throw new Exception("Estado de sucursal inválido: " + changeStateBranchDTO.getEstado());
            }

            branchRepository.save(branch);
            log.info("Estado de la sucursal con ID " + branch.getIdSucursal() + " cambiado a " + branch.getEstado().name());
            return branch;
        } catch (Exception e) {
            log.error("Error al cambiar el estado de la sucursal: " + e.getMessage());
            throw new Exception("Error al cambiar el estado de la sucursal: " + e.getMessage());
        }
    }

}