// Interface principal que coincide con la respuesta del API
export interface GuiaClienteDetalleResponse {
    guia: GuiaDetalle;
}

// Interface para los datos de la guía
export interface GuiaDetalle {
    idGuia: number;
    numeroGuia: string;
    descripcionContenido: string;
    valorDeclarado: number;
    pesoKg: number;
    dimensiones: string;
    observaciones: string;
    fechaCreacion: string;
    fechaRecoleccionReal: string | null;
    prioridad: 'NORMAL' | 'ALTA' | 'URGENTE';
    intentosEntrega: number;
    total: number;
    estadoActual: string;
    estadosEntregas: GuiaEstado[];
    repartidor: Repartidor | null;
}

export interface GuiaEstado {
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
}