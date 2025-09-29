export interface IncidenciaModel {
  idIncidencia: number;
  idGuia: number;
  idRepartidor: number;
  codigoIncidencia: string;
  tipoIncidencia: string;
  severidad: string;
  descripcion: string;
  solucionAplicada: string;
  requiereDevolucion: boolean;
  costoAdicional: number;
  fechaReporte: string;
  fechaAtencion: string;
  fechaResolucion: string;
  estado: string;
}

export const TIPOS_INCIDENCIAS: string[] = [
  'CLIENTE_AUSENTE',
  'DIRECCION_INCORRECTA',
  'PAQUETE_DANADO',
  'VEHICULO_AVERIADO',
  'ACCIDENTE_TRANSITO',
  'ZONA_INSEGURA',
  'CONDICIONES_CLIMATICAS',
  'CLIENTE_RECHAZA',
  'OTRO',
];
export const SEVERIDADES: string[] = ['BAJA', 'MEDIA', 'ALTA', 'CRITICA'];

export const ESTADOS_INCIDENCIA: string[] = ['ABIERTA', 'EN_ATENCION', 'RESUELTA', 'CERRADA'];
