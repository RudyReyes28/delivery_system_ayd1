export interface PeriodoLiquidacion {
  idPeriodo: number;
  descripcion: string;
  fechaInicio: string;
  fechaFin: string;
  estado: 'ABIERTO' | 'CERRADO';
  totalComisiones: number;
  totalBonificaciones: number;
  totalDeducciones: number;
  totalNeto: number;
}

export interface EmpleadoDetalle {
  idEmpleado: number;
  codigoEmpleado: string;
  estadoEmpleado: string;
}

export interface ContratoDetalle {
  idContrato: number;
  tipoContrato: string;
  fechaInicio: string;
  fechaFin: string;
  estadoContrato: string;
}

export interface LiquidacionRepartidor {
  idLiquidacion: number | null;
  totalEntregas: number | null;
  totalComisiones: number | null;
  totalBonificaciones: number | null;
  totalDeducciones: number | null;
  subtotal: number | null;
  descuentoIgss: number | null;
  descuentoIsr: number | null;
  otrosDescuentos: number | null;
  totalDescuentos: number | null;
  totalNeto: number | null;
  metodoPago: string | null;
  estado: string | null;
  periodoLiquidacion: PeriodoLiquidacion | null;
}

export interface Repartidor {
guiasAsignadas: any;
  idRepartidor: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
  zonaAsignada: string;
  radioCobertura: number;
  disponibilidad: string;
  calificacionPromedio: number;
  totalEntregasCompletadas: number;
  totalEntregasFallidas: number;
  empleadoDetalle: EmpleadoDetalle;
  contratoDetalle: ContratoDetalle;
  liquidacionRepartidor: LiquidacionRepartidor;
}

export interface RepartidorResponse {
  repartidor: Repartidor;
}

export interface NuevoPeriodoRequest {
  descripcion: string;
  fechaInicio: string;
  fechaFin: string;
}

export interface ProcesarLiquidacionRequest {
  idRepartidor: number;
  idPeriodoLiquidacion: number;
}

export interface ApiResponse {
  message: string;
}