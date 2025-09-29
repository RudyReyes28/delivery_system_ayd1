export interface CancelacionClienteResponse {
    incidencia: IncidenciaCliente;
}

export enum EstadoIncidencia {
  ABIERTA = 'ABIERTA',
  EN_ATENCION = 'EN_ATENCION',
  RESUELTA = 'RESUELTA',
  CERRADA = 'CERRADA'
}

export interface IncidenciaCliente {
    idIncidencia: number;
    codigoIncidencia: string;
    tipoIncidencia: string;
    severidad: string;
    descripcion: string;
    fechaReporte: string;
    estadoIncidencia: string; // Campo ajustado según el JSON real
    guia: GuiaCancelacion;
    solucionAplicada?: string;
    estado?: string; // Este puede ser uno de los valores de EstadoIncidencia
}

export interface GuiaCancelacion {
    idGuia: number;
    numeroGuia: string;
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
    estadosEntregas: EstadoEntrega[];
    repartidor: Repartidor;
}

export interface EstadoEntrega {
    idGuiaEstado: number;
    estadoAnterior: string;
    estadoNuevo: string;
    comentarios: string | null;
    motivoCambio: string;
    fechaCambio: string;
}

export interface Repartidor {
    idRepartidor: number;
    nombreCompleto: string;
    email: string;
    telefono: string;
}

export interface AtenderCancelacionRequest {
    idIncidencia: number;
    idUsuario: number;
    solucionAplicada: string;
}

export interface AtenderCancelacionResponse {
    message: string;
}
