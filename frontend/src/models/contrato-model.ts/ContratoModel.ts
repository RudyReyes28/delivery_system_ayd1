export interface ContratoModel {
  idContrato: number;
  idEmpleado: number;
  numeroContrato: string;
  tipoContrato: string;
  modalidadTrabajo: string;
  fechaInicio: string;
  fechaFin?: string;
  renovacionAutomatica: boolean;
  salarioBase: number;
  moneda: string;
  frecuenciaPago: string;
  incluyeAguinaldo: boolean;
  incluyeBono14: boolean;
  incluyeVacaciones: boolean;
  incluyeIgss: boolean;
  estadoContrato: string;
}

export interface ContratoRequestDTO {
  idContrato: number;
  idEmpleado: number;
  numeroContrato: string;
  tipoContrato: string;
  modalidadTrabajo: string;
  fechaInicio: string;
  fechaFin?: string;
  renovacionAutomatica: boolean;
  salarioBase: number;
  moneda: string;
  frecuenciaPago: string;
  incluyeAguinaldo: boolean;
  incluyeBono14: boolean;
  incluyeVacaciones: boolean;
  incluyeIgss: boolean;
  estadoContrato: string;
}

// Interfaz para la solicitud de la Comisión del Contrato
export interface ContratoComisionDTO {
  idContratoComision: number;
  idContrato: number;
  tipoComision: string;
  porcentaje?: number;
  montoFijo?: number;
  aplicaDesde: string;
  aplicaHasta?: string;
  activo: boolean;
  minimoEntregasMes?: number;
  maximoEntregasMes?: number;
  factorMultiplicador?: number;
  createdAt: string;
}

// Interfaz principal para el registro del Contrato
export interface RegisterContratoDTO {
  contrato: ContratoRequestDTO;
  comision: ContratoComisionDTO;
}

export const TIPOS_CONTRATO: string[] = ['INDEFINIDO', 'TEMPORAL', 'POR_SERVICIOS', 'PRACTICANTE'];

export const MODALIDADES_TRABAJO: string[] = ['PRESENCIAL', 'REMOTO', 'HIBRIDO'];

export const FRECUENCIA_PAGO: string[] = ['SEMANAL', 'QUINCENAL', 'MENSUAL'];

export const ESTADOS_CONTRATO: string[] = [
  'BORRADOR',
  'ACTIVO',
  'SUSPENDIDO',
  'TERMINADO',
  'RESCINDIDO',
];

export const TIPOS_COMISION: string[] = ['PORCENTAJE', 'FIJO_POR_ENTREGA', 'ESCALONADO'];



