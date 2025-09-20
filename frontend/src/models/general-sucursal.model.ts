export interface Contacto {
  idEmpresaContacto: number;
  idPersona: number;
  cargo: string;
  fechaInicio: string;
  fechaFin: string;
  activo: boolean;
}

export interface Sucursal {
  idSucursal: number;
  codigoSucursal: string;
  nombreSucursal: string;
  horarioApertura: string;
  horarioCierre: string;
  diasOperacion: string;
  estado: string;
}

export interface EmpresaFidelizacion {
  idEmpresaFidelizacion: number;
  mes: number;
  anio: number;
  totalEntregas: number;
  montoTotalEntregas: number;
  totalCancelaciones: number;
  descuentoAplicado: number;
  penalizacionesAplicadas: number;
}

export interface NivelFidelizacion {
  idNivel: number;
  codigoNivel: string;
  nombreNivel: string;
  descripcion: string;
  numeroMinimoEntregas: number;
  numeroMaximoEntregas: number;
  descuentoPorcentaje: number;
  cancelacionesGratuitasMes: number;
  porcentajePenalizacion: number;
  activo: boolean;
  fechaInicioVigencia: string;
  fechaFinVigencia: string | null;
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
  contacto?: Contacto;
  sucursal?: Sucursal;
  empresaFidelizacion?: EmpresaFidelizacion;
  nivelFidelizacion?: NivelFidelizacion;
}

export interface EmpresaResponse {
  empresa: Empresa;
}