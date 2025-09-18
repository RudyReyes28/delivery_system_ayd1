package org.entregasayd.sistemasentregas.dto.fidelizacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCompaniesFidelizacionDTO {
    //Empresa
    private EmpresaFideDTO empresa;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmpresaFideDTO {
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
        private EmpresaFidelizacionDTO empresaFidelizacion;
        private NivelFindelizacionDTO nivelFidelizacion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmpresaFidelizacionDTO {
        private Long idEmpresaFidelizacion;
        private Integer mes;
        private Integer anio;
        private Integer totalEntregas;
        private Double montoTotalEntregas;
        private Integer totalCancelaciones;
        private Double descuentoAplicado;
        private Double penalizacionesAplicadas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NivelFindelizacionDTO {
        private Long idNivel;
        private String codigoNivel;
        private String nombreNivel;
        private String descripcion;
        private Integer numeroMinimoEntregas;
        private Integer numeroMaximoEntregas;
        private Double descuentoPorcentaje;
        private Integer cancelacionesGratuitasMes;
        private Double porcentajePenalizacion;
        private Boolean activo;
        private String fechaInicioVigencia;
        private String fechaFinVigencia;
    }


}
