package org.entregasayd.sistemasentregas.dto.sucursales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBranchCompanyDTO {
    //Aqui vamos a obtener La empresa y la sucursal donde trabaja el usuario logueado
    private EmpresaUsuarioDTO empresa;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmpresaUsuarioDTO {
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
        private ContactoUsuarioDTO contacto;
        private SucursalDTO sucursal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactoUsuarioDTO {
        private Long idEmpresaContacto;
        private Long idPersona;
        private String cargo;
        private String fechaInicio;
        private String fechaFin;
        private Boolean activo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SucursalDTO {
        private Long idSucursal;
        private String codigoSucursal;
        private String nombreSucursal;
        private String horarioApertura;
        private String horarioCierre;
        private String diasOperacion;
        private String estado;
    }

}
