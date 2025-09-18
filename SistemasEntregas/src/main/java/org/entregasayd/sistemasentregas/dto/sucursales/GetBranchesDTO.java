package org.entregasayd.sistemasentregas.dto.sucursales;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBranchesDTO {
    private BranchDTO branch;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BranchDTO {
        private Long idSucursal;
        private CompanyDTO company;
        private String codigoSucursal;
        private String nombreSucursal;
        private AddressDTO address;
        private ArrayList<StaffDTO> staff;
        private String horarioApertura;
        private String horarioCierre;
        private String diasOperacion;
        private String estado;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompanyDTO {
        private Long idEmpresa;
        private String tipoEmpresa;
        private String nombreComercial;
        private String razonSocial;
        private String nit;
        private String registroMercantil;
        private String fechaConstitucion;
        private String fechaAfiliacion;
        private String fechaVencimientoAfiliacion;
        private String estado;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressDTO {
        private Long idDireccion;
        private String tipoDireccion;
        private String municipio;
        private String departamento;
        private String pais;
        private String codigoPostal;
        private String referencias;
        private Boolean activa;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StaffDTO {
        private Long idSucursalPersonal;
        private Long idUsuario;
        private String nombreUsuario;
        private Long idPersona;
        private String cargo;
        private Boolean esEncargado;
        private String fechaAsignacion;
        private String fechaFin;
        private Boolean activo;
    }
}
