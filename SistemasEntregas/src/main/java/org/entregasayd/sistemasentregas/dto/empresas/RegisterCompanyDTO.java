package org.entregasayd.sistemasentregas.dto.empresas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyDTO {
    //Empresa
    private String tipoEmpresa;
    private String nombreComercial;
    private String razonSocial;
    private String nit;
    private String registroMercantil;
    private String fechaConstitucion;
    private String fechaAfiliacion;
    private String fechaVencimientoAfiliacion;
    //Contacto
    private Long idPersona;
    private String cargo;
    private String fechaInicio;
    private String fechaFin;
    private Boolean activo=true;

}

/*
Ejemplo de JSON para RegisterCompanyDTO
{
    "tipoEmpresa": "COMERCIO_AFILIADO",
    "nombreComercial": "Mi Empresa",
    "razonSocial": "Mi Empresa S.A.",
    "nit": "1234567-8",
    "registroMercantil": "RM-12345",
    "fechaConstitucion": "2020-01-01",
    "fechaAfiliacion": "2023-01-01",
    "fechaVencimientoAfiliacion": "2024-01-01",
    "idPersona": 1,
    "cargo": "Gerente",
    "fechaInicio": "2023-01-01",
    "fechaFin": null,
    "activo": true
}


 */