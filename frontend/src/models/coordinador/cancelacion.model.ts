export interface SolicitudCancelacion {
  idCancelacion: number;
  motivoCategoria: MotivoCancelacion;
  motivoDetalle: string;
  fechaCancelacion: string;
  estadoCancelacion: EstadoCancelacion;
  guia: GuiaCancelacion;
}

export interface GuiaCancelacion {
  idGuia: number;
  numeroGuia: string;
  codigoInterno: string;
  descripcionContenido: string;
  valorDeclarado: number;
  pesoKg: number;
  dimensiones: string;
  observaciones: string;
  fechaCreacion: string;
  fechaRecoleccionReal: string | null;
  prioridad: string;
  intentosEntrega: number;
  total: number;
  estadoActual: string;
}

export enum MotivoCancelacion {
  ERROR_INFORMACION = 'ERROR_INFORMACION',
  CAMBIO_CLIENTE = 'CAMBIO_CLIENTE',
  PRODUCTO_NO_DISPONIBLE = 'PRODUCTO_NO_DISPONIBLE',
  FUERZA_MAYOR = 'FUERZA_MAYOR',
  DECISION_COMERCIAL = 'DECISION_COMERCIAL',
  OTRO = 'OTRO'
}

export enum EstadoCancelacion {
  SOLICITADA = 'SOLICITADA',
  AUTORIZADA = 'AUTORIZADA',
  PROCESADA = 'PROCESADA',
  RECHAZADA = 'RECHAZADA'
}

export interface AceptarCancelacionRequest {
  idCancelacion: number;
  idUsuario: number;
}

export interface CancelacionResponse {
  message: string;
}
