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
  contacto?: {
    idEmpresaContacto: number;
    idPersona: number;
    cargo: string;
    fechaInicio: string;
    fechaFin: string;
    activo: boolean;
  };
}

export interface EmpresaItem {
  empresa: Empresa;
}

export interface Direccion {
  idDireccion: number;
  tipoDireccion: string;
  municipio: string;
  departamento: string;
  pais: string;
  codigoPostal: string;
  referencias: string;
  activa: boolean;
}

export interface Personal {
  idSucursalPersonal: number;
  idUsuario: number;
  nombreUsuario: string;
  idPersona: number;
  cargo: string;
  esEncargado: boolean;
  fechaAsignacion: string;
  fechaFin: string;
  activo: boolean;
}

export interface Sucursal {
  idSucursal: number;
  company: Empresa;
  codigoSucursal: string;
  nombreSucursal: string;
  address: Direccion;
  staff: Personal[];
  horarioApertura: string;
  horarioCierre: string;
  diasOperacion: string;
  estado: string;
}

export interface SucursalItem {
  branch: Sucursal;
  expanded?: boolean;
  actualizandoEstado?: boolean;
}

export interface UsuarioSucursal {
  idUsuario: number;
  nombreUsuario: string;
  idPersona: number;
  idRol: number;
  nombreRol: string;
  estado: string;
}

export interface SucursalFormData {
  idEmpresa: number;
  codigoSucursal: string;
  nombreSucursal: string;
  horarioApertura: string;
  horarioCierre: string;
  diasOperacion: string;
  estado: string;
  // Dirección
  tipoDireccion: string;
  municipio: string;
  departamento: string;
  pais: string;
  codigoPostal: string;
  referencias: string;
  activa: boolean;
  // Personal
  idUsuario: number;
  cargo: string;
  esEncargado: boolean;
  fechaFin: string;
  activo: boolean;
}

export interface CambiarEstadoSucursalRequest {
  idSucursal: number;
  estado: string;
}

export interface CambiarEstadoSucursalResponse {
  message: string;
  status: string;
}