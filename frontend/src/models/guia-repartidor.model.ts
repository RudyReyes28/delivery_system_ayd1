export interface Cliente {
  idCliente: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
}

export interface DireccionEntrega {
  idDireccion: number;
  municipio: string;
  departamento: string;
  pais: string;
  codigoPostal: string;
  referencias: string;
}

export interface EstadoEntrega {
  idGuiaEstado: number;
  estadoAnterior: string;
  estadoNuevo: string;
  comentarios: string;
  motivoCambio: string;
  fechaCambio: string;
}

export interface Repartidor {
  idRepartidor: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
  zonaAsignada?: string;
  radioCobertura?: number;
  disponibilidad?: 'DISPONIBLE' | 'OCUPADO' | 'INACTIVO';
  calificacionPromedio?: number;
  totalEntregasCompletadas?: number;
  totalEntregasFallidas?: number;
  empleadoDetalle?: EmpleadoDetalle;
  contratoDetalle?: ContratoDetalle;
  guiasAsignadas?: GuiaAsignada[];
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

export interface GuiaAsignada {
  idGuia: number;
  numeroGuia: string;
  estadoActual: string;
  fechaCreacion: string;
  descripcionContenido: string;
}

export interface RepartidorCompleto {
  repartidor: Repartidor;
}

export interface Guia {
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
  prioridad: 'NORMAL' | 'ALTA' | 'BAJA';
  intentosEntrega: number;
  total: number;
  estadoActual: string;
  cliente: Cliente;
  direccionEntrega: DireccionEntrega;
  estadosEntregas: EstadoEntrega[];
  repartidor: Repartidor | null;
}

export interface GuiaResponse {
  guia: Guia;
}

export interface AsignarRepartidorRequest {
  idGuia: number;
  idRepartidor: number;
  fechaEntrega: string;
}

export interface AsignarRepartidorResponse {
  message: string;
}