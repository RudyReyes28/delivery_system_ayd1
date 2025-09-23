export interface RepartidorVehiculoDTO {
  idRepartidorVehiculo: number;
  idRepartidor: number;
  idVehiculo: number;
  fechaAsignacion: string;
  fechaDesasignacion?: string;
  esVehiculoPrincipal: boolean;
  activo: boolean;
}
