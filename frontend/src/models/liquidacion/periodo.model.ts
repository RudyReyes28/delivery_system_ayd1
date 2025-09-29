
export interface PeriodoLiquidacion {
    idPeriodo: number;
    descripcion: string;
    fechaInicio: string;
    fechaFin: string;
    fechaCreacion?: string;
    fechaCierre?: string;
    estado: string;
    totalComisiones: number;
    totalBonificaciones: number;
    totalDeducciones: number;
    totalNeto: number;
}

export enum EstadoPeriodo {
    ACTIVO = 'ACTIVO',
    CERRADO = 'CERRADO'
}

export interface CerrarPeriodoResponse {
    message: string;
}
