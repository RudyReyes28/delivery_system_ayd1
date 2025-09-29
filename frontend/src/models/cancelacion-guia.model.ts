export interface CancelacionGuia {
  idGuia: number;
  motivoCategoria: MotivoCancelacion;
  motivoDetalle: string;
  idUsuario: number;
}

export enum MotivoCancelacion {
  ERROR_INFORMACION = 'ERROR_INFORMACION',
  CAMBIO_CLIENTE = 'CAMBIO_CLIENTE',
  PRODUCTO_NO_DISPONIBLE = 'PRODUCTO_NO_DISPONIBLE',
  FUERZA_MAYOR = 'FUERZA_MAYOR',
  DECISION_COMERCIAL = 'DECISION_COMERCIAL',
  OTRO = 'OTRO'
}

export interface CancelacionResponse {
  message: string;
}
