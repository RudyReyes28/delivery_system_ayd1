export interface RepartidorDTO {
  idRepartidor: number;
  idEmpleado: number;
  numeroLicencia: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: string;
  zonaAsignada: string;
  radioCoberturaKm: number;
  disponibilidad: string;
  calificacionPromedio: number;
  totalEntregasCompletadas: number;
  totalEntregasFallidas: number;
}

//tipo_licencia ENUM('A','B','C','M','NINGUNA') DEFAULT 'NINGUNA',
export const TIPOS_LICENCIA: string[] = ['A', 'B', 'C', 'M', 'NINGUNA'];
// disponibilidad ENUM('disponible','ocupado','descanso','inactivo') DEFAULT 'disponible',
export const DISPONIBILIDAD_REPARTIDOR: string[] = ['DISPONIBLE', 'OCUPADO', 'DESCANSO', 'INACTIVO'];
