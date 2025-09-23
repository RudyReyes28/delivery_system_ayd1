// usuario-response.dto.ts
export interface UsuarioModel {
  idUsuario: number;
  idPersona: number;
  idRol: number;
  nombreRol: string;
  nombreUsuario: string;
  twoFactorEnabled: boolean;
  intentosFallidos: number;
  ultimaFechaAcceso: string; // ISO string de LocalDateTime
  estado: string; // viene de Usuario.Estado (enum en backend)
  fechaCreacion: string; // ISO string de LocalDateTime
  fechaUltimaActualizacion: string; // ISO string de LocalDateTime
  nombre: string;
  apellido: string;
  fechaNacimiento: string; // ISO string de LocalDate
  dpi: string;
  correo: string;
  telefono: string;
  direccion: string;
  estadoPersona: string;
}
