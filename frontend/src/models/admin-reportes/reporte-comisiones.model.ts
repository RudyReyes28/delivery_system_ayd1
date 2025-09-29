export interface ReporteComisiones {
  periodo: Periodo;
}

export interface Periodo {
  idPeriodo: number;
  descripcion: string;
  fechaInicio: string;
  fechaFin: string;
  estado: string;
  repartidores: RepartidorComisiones[];
}

export interface RepartidorComisiones {
  idLiquidacion: number;
  totalComisiones: number;
  totalBonificaciones: number;
  totalDeducciones: number;
  subtotal: number;
  descuentoIgss: number;
  descuentoIsr: number;
  otrosDescuentos: number;
  totalDescuentos: number;
  totalNeto: number;
  nombreRepartidor: string;
  idRepartidor: number;
  detallesComision: DetalleComision[];
}

export interface DetalleComision {
  idDetalle: number;
  fechaEntrega: string;
  numeroGuia: string;
  tipoComision: string;
  montoBaseCalculo: number;
  valorComision: number;
  montoComision: number;
  bonificacion: number;
  deduccion: number;
  montoNeto: number;
  estado: string;
}
