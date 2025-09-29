export interface ReporteEntregas {
  entregasCompletadas: EntregaCompletada[];
  entregasCanceladas: EntregaCancelada[];
  entregasRechazadas: EntregaRechazada[];
}

export interface EntregaCompletada {
  entregasCompletadas: {
    guia: Guia;
  };
  totalEntregasCompletadas: number;
}

export interface EntregaCancelada {
  entregasCanceladas: {
    guia: Guia;
  };
  totalEntregasCanceladas: number;
}

export interface EntregaRechazada {
  entregasRechazadas: {
    guia: Guia;
  };
  motivoRechazo: string;
  fechaRechazo: string;
  totalEntregasRechazadas: number;
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
  prioridad: string;
  intentosEntrega: number;
  total: number;
  estadoActual: string;
  cliente: Cliente;
  direccionEntrega: Direccion;
  estadosEntregas: EstadoEntrega[];
  repartidor: Repartidor;
}

export interface Cliente {
  idCliente: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
}

export interface Direccion {
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
  comentarios: string | null;
  motivoCambio: string | null;
  fechaCambio: string;
}

export interface Repartidor {
  idRepartidor: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
}
