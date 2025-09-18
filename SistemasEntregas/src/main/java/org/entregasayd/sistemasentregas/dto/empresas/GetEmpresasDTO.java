package org.entregasayd.sistemasentregas.dto.empresas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetEmpresasDTO {
    private EmpresaDTO empresa;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmpresaDTO {
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
        private ContactoDTO contacto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactoDTO {
        private Long idEmpresaContacto;
        private Long idPersona;
        private String cargo;
        private String fechaInicio;
        private String fechaFin;
        private Boolean activo;
    }

}
