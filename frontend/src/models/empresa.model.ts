export interface UsuarioSucursal {
  idUsuario: number;
  nombreUsuario: string;
  idPersona: number;
  idRol: number;
  nombreRol: string;
  estado: string;
}

export interface Contacto {
  idEmpresaContacto: number;
  idPersona: number;
  cargo: string;
  fechaInicio: string;
  fechaFin: string;
  activo: boolean;
}

export interface Empresa {
  idEmpresa: number;
  tipoEmpresa: string;
  nombreComercial: string;
  razonSocial: string;
  nit: string;
  registroMercantil: string;
  fechaConstitucion: string;
  fechaAfiliacion: string;
  fechaVencimientoAfiliacion: string;
  estado: string;
  contacto: Contacto | null;
}

export interface EmpresaItem {
  empresa: Empresa;
  expanded?: boolean;
  actualizandoEstado?: boolean;
}

export interface CambiarEstadoRequest {
  idEmpresa: number;
  nuevoEstado: string;
}

export interface CambiarEstadoResponse {
  idEmpresa: number;
  tipoEmpresa: string;
  nombreComercial: string;
  razonSocial: string;
  nit: string;
  registroMercantil: string;
  fechaConstitucion: string;
  fechaAfiliacion: string;
  fechaVencimientoAfiliacion: string;
  estado: string;
}

export interface EmpresaFormData {
  tipoEmpresa: string;
  nombreComercial: string;
  razonSocial: string;
  nit: string;
  registroMercantil: string;
  fechaConstitucion: string;
  fechaAfiliacion: string;
  fechaVencimientoAfiliacion: string;
  idPersona: number;
  cargo: string;
  fechaInicio: string;
  fechaFin: string | null;
  activo: boolean;
}