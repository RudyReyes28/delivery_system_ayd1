export interface AsignacionItem {
  asignacion: Asignacion;
  expanded?: boolean;
}

export interface Asignacion {
  idAsignacion: number;
  fechaAsignacion: string; // formato ISO string
  estadoAsignacion: string;
  guia: Guia;
}

export interface Guia {
  idGuia: number;
  numeroGuia: string;
  codigoInterno: string;
  descripcionContenido: string;
  valorDeclarado: number;
  pesoKg: number;
  dimensiones: string;
  estadoGuia: string;
  cliente: Cliente;
  direccionEntrega: Direccion;
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
