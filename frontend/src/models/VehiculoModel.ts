export const tiposVehiculo: string[] = [
  'BICICLETA',
  'MOTOCICLETA',
  'AUTOMOVIL',
  'PICKUP',
  'VAN',
  'A_PIE',
];

export const estadosVehiculo: string[] = ['ACTIVO', 'MANTENIMIENTO', 'AVERIADO', 'INACTIVO'];

// Interfaz para la entidad Vehiculo
export interface Vehiculo {
  idVehiculo: number;
  tipoVehiculo: number;
  marca?: string;
  modelo?: string;
  anio?: number;
  placa: string;
  color?: string;
  capacidadCargaKg: number;
  capacidadVolumenM3: number;
  numeroTarjetaCirculacion?: string;
  numeroPolizaSeguro?: string;
  fechaVencimientoSeguro?: string;
  estadoVehiculo: number;
}
