package org.entregasayd.sistemasentregas.services.sucursales;

import org.entregasayd.sistemasentregas.dto.fidelizacion.GetCompaniesFidelizacionDTO;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.dto.sucursales.*;

import java.util.ArrayList;

@Slf4j
@Service
public class UserBranchService {

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

    @Autowired
    private EmpresaFidelizacionRepository empresaFidelizacionRepository;


    public GetBranchCompanyDTO getInformationBranchCompany(long idUsuario) {
        //Aqui vamos a obtener La empresa y la sucursal donde trabaja el usuario logueado
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        SucursalPersonal sucursalPersonal = branchStaffRepository.findByUsuarioIdUsuario(idUsuario);
        if (sucursalPersonal == null) {
            throw new RuntimeException("El usuario no está asignado a ninguna sucursal");
        }
        Sucursal sucursal = sucursalPersonal.getSucursal();
        Empresa empresa = sucursal.getEmpresa();
        EmpresaContacto empresaContacto = companyContactRepository.findByEmpresaIdEmpresa(empresa.getIdEmpresa());

        //Construimos el DTO
        GetBranchCompanyDTO.EmpresaUsuarioDTO empresaDTO = GetBranchCompanyDTO.EmpresaUsuarioDTO.builder()
                .idEmpresa(empresa.getIdEmpresa())
                .tipoEmpresa(empresa.getTipoEmpresa().toString())
                .nombreComercial(empresa.getNombreComercial())
                .razonSocial(empresa.getRazonSocial())
                .nit(empresa.getNit())
                .registroMercantil(empresa.getRegistroMercantil())
                .fechaConstitucion(empresa.getFechaConstitucion().toString())
                .fechaAfiliacion(empresa.getFechaAfiliacion().toString())
                .fechaVencimientoAfiliacion(empresa.getFechaVencimientoAfiliacion().toString())
                .estado(empresa.getEstado().toString())
                .contacto(empresaContacto != null ? GetBranchCompanyDTO.ContactoUsuarioDTO.builder()
                        .idEmpresaContacto(empresaContacto.getIdEmpresaContacto())
                        .idPersona(empresaContacto.getPersona().getIdPersona())
                        .cargo(empresaContacto.getCargo())
                        .fechaInicio(empresaContacto.getFechaInicio().toString())
                        .fechaFin(empresaContacto.getFechaFin() != null ? empresaContacto.getFechaFin().toString() : null)
                        .activo(empresaContacto.getActivo())
                        .build() : null)
                .sucursal(GetBranchCompanyDTO.SucursalDTO.builder()
                        .idSucursal(sucursal.getIdSucursal())
                        .codigoSucursal(sucursal.getCodigoSucursal())
                        .nombreSucursal(sucursal.getNombreSucursal())
                        .horarioApertura(sucursal.getHorarioApertura())
                        .horarioCierre(sucursal.getHorarioCierre())
                        .diasOperacion(sucursal.getDiasOperacion())
                        .estado(sucursal.getEstado().toString())
                        .build())
                .build();

        GetBranchCompanyDTO result = GetBranchCompanyDTO.builder()
                .empresa(empresaDTO)
                .build();

        return result;
    }

    //Obtener la fidelizacion de la empresa a la que pertenece la sucursal del usuario logueado
    public GetCompaniesFidelizacionDTO getFidelizacionCompany(long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        SucursalPersonal sucursalPersonal = branchStaffRepository.findByUsuarioIdUsuario(idUsuario);
        if (sucursalPersonal == null) {
            throw new RuntimeException("El usuario no está asignado a ninguna sucursal");
        }
        Sucursal sucursal = sucursalPersonal.getSucursal();
        Empresa empresa = sucursal.getEmpresa();
        EmpresaFidelizacion empresaFidelizacion = empresaFidelizacionRepository.findByEmpresaIdEmpresa(empresa.getIdEmpresa());
        if (empresaFidelizacion == null) {
            throw new RuntimeException("La empresa no tiene un programa de fidelización asignado");
        }
        GetCompaniesFidelizacionDTO.EmpresaFidelizacionDTO empresaFidelizacionDTO = new GetCompaniesFidelizacionDTO.EmpresaFidelizacionDTO(
                empresaFidelizacion.getIdEmpresaFidelizacion(),
                empresaFidelizacion.getMes(),
                empresaFidelizacion.getAnio(),
                empresaFidelizacion.getTotalEntregas(),
                empresaFidelizacion.getMontoTotalEntregas(),
                empresaFidelizacion.getTotalCancelaciones(),
                empresaFidelizacion.getDescuentoAplicado(),
                empresaFidelizacion.getPenalizacionesAplicadas()
        );
        NivelFindelizacion nivel = empresaFidelizacion.getNivelFidelizacion();
        GetCompaniesFidelizacionDTO.NivelFindelizacionDTO nivelFindelizacionDTO = null;
        if (nivel != null) {
            nivelFindelizacionDTO = new GetCompaniesFidelizacionDTO.NivelFindelizacionDTO(
                    nivel.getIdNivel(),
                    nivel.getCodigoNivel(),
                    nivel.getNombreNivel(),
                    nivel.getDescripcion(),
                    nivel.getEntregasMinimas(),
                    nivel.getEntregasMaximas(),
                    nivel.getDescuentoPorcentaje(),
                    nivel.getCancelacionesGratuitasMes(),
                    nivel.getPorcentajePenalizacion(),
                    nivel.getActivo(),
                    nivel.getFechaInicioVigencia().toString(),
                    nivel.getFechaFinVigencia() != null ? nivel.getFechaFinVigencia().toString() : null
            );
        }
        GetCompaniesFidelizacionDTO.EmpresaFideDTO empresaFideDTO = new GetCompaniesFidelizacionDTO.EmpresaFideDTO(
                empresa.getIdEmpresa(),
                empresa.getTipoEmpresa().toString(),
                empresa.getNombreComercial(),
                empresa.getRazonSocial(),
                empresa.getNit(),
                empresa.getRegistroMercantil(),
                empresa.getFechaConstitucion().toString(),
                empresa.getFechaAfiliacion().toString(),
                empresa.getFechaVencimientoAfiliacion().toString(),
                empresa.getEstado().toString(),
                empresaFidelizacionDTO,
                nivelFindelizacionDTO
        );
        GetCompaniesFidelizacionDTO result = new GetCompaniesFidelizacionDTO(empresaFideDTO);
        return result;
    }

}
