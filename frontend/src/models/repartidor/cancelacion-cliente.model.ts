export interface CancelacionClienteRequest {
  idGuia: number;
  idUsuario: number;
  descripcion: string;
}

export interface CancelacionClienteResponse {
  message: string;
}
