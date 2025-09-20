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
  empresaFidelizacion?: EmpresaFidelizacion | null;
  nivelFidelizacion?: NivelFidelizacion | null;
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
  numeroMinimoEntregas?: number;
  numeroMaximoEntregas?: number;
  entregasMinimas?: number;
  entregasMaximas?: number;
  descuentoPorcentaje: number;
  cancelacionesGratuitasMes: number;
  porcentajePenalizacion: number;
  activo: boolean;
  fechaInicioVigencia: string;
  fechaFinVigencia: string | null;
}

export interface EmpresaData {
  empresa: Empresa;
}

export interface AsignarCambiarRequest {
  idEmpresa: number;
  codigoNivel: string;
}

export interface AsignarCambiarResponse {
  idEmpresaFidelizacion: number;
  empresa: Empresa;
  nivelFidelizacion: NivelFidelizacion;
  mes: number;
  anio: number;
  totalEntregas: number;
  montoTotalEntregas: number;
  totalCancelaciones: number;
  descuentoAplicado: number;
  penalizacionesAplicadas: number;
}

export type AccionFidelizacion = 'asignar' | 'cambiar';

export interface ResultadoOperacion {
  exito: boolean;
  mensaje: string;
  datos?: AsignarCambiarResponse;
}