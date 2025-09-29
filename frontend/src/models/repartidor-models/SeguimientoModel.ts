export interface GuiaItem {
  guia: Guia;
  expanded?: boolean;
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
  motivoCambio: string;
  fechaCambio: string;
}
