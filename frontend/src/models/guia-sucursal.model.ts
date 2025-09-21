/* Tipo de Servicio */
export interface TipoServicioResponse {
    idTipoServicio: number,
    codigoServicio: "STD" | "EXP" | "SAME" | "INTL",
    nombreServicio: string,
    descripcion: string,
    tiempoEntregaHoras: number,
    precioBase: number,
    pesoMaximoKg: number,
    volumenMaximoM3: number,
    activo: boolean
}

/* Cliente */
export interface ClienteRegistradoResponse {
    idCliente: number,
    codigoCliente: string,
    nombreCompleto: string,
    telefono: string,
    email: string
}

/* Guia con Cliente Nuevo*/
export interface NuevaGuiaClienteNuevoRequest {
  nombreCompleto: string;
  telefono: string;
  email: string;
  horarioPreferidoInicio: string;
  horarioPreferidoFin: string;
  instruccionesEntrega: string;
  aceptaEntregasVecinos: boolean;
  requiereIdentificacion: boolean;
  municipio: string;
  departamento: string;
  pais: string;
  codigoPostal: string;
  referencias: string;
  aliasDireccion: string;
  instruccionesEspecificas: string;
  puntoReferencia: string;
  personaRecibe: string;
  idUsuario: number;
  idTipoServicio: number;
  descripcionContenido: string;
  valorDeclarado: number;
  pesoKG: number;
  dimensiones: string;
  esFragil: boolean;
  observaciones: string;
  fechaProgramadaRecoleccion: string;
  prioridad: 'NORMAL' | 'ALTA' | 'URGENTE';
  subtotal: number;
  descuentos: number;
  recargos: number;
  formaPago: 'CONTADO' | 'CREDITO' | 'CONTRA_ENTREGA';
}

export interface NuevaGuiaClienteNuevoResponse {
    message: string
}

/* Guia con Cliente Existente */
export interface NuevaGuiaClienteExistenteRequest {
  idCliente: number;
  idUsuario: number;
  idTipoServicio: number;
  descripcionContenido: string;
  valorDeclarado: number;
  pesoKG: number;
  dimensiones: string;
  esFragil: boolean;
  observaciones: string;
  fechaProgramadaRecoleccion: string;
  prioridad: 'NORMAL' | 'ALTA' | 'URGENTE';
  subtotal: number;
  descuentos: number;
  recargos: number;
  formaPago: 'CONTADO' | 'CREDITO' | 'CONTRA_ENTREGA';
}

export interface NuevaGuiaClienteExistenteResponse {
    message: string
}

/* Guía */
export interface GuiaResponse {
  guia: {
    idGuia: number;
    numeroGuia: string;
    codigoInterno: string;
    descripcionContenido: string;
    valorDeclarado: number;
    pesoKg: number;
    dimensiones: string;
    observaciones: string;
    fechaCreacion: string;
    fechaRecoleccionReal: string;
    prioridad: 'NORMAL' | 'ALTA' | 'URGENTE';
    intentosEntrega: number;
    total: number;
    estadoActual: string;
    cliente: {
      idCliente: number;
      nombreCompleto: string;
      email: string;
      telefono: string;
    };
    direccionEntrega: {
      idDireccion: number;
      municipio: string;
      departamento: string;
      pais: string;
      codigoPostal: string;
      referencias: string;
    };
    estadosEntregas: Array<{
      idGuiaEstado: number;
      estadoAnterior: string;
      estadoNuevo: string;
      comentarios: string;
      motivoCambio: string;
      fechaCambio: string;
    }>;
  };
}
