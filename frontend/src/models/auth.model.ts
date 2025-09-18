/* Creación de Usuario */
export interface UsuarioRequest {
  tipoDireccion: string,
  municipio: string,
  departamento: string,
  pais: string,
  codigoPostal: string,
  referencias: string,
  nombre: string,
  apellido: string,
  fechaNacimiento: string,
  dpi: string,
  correo: string,
  telefono: string,
  nombreUsuario: string,
  contraseniaHash: string,
}

export interface UsuarioResponse {
  status: string,
  message: string
}

/* Inicio de Sesión */
export interface CredencialResquest {
  nombreUsuario: string,
  contrasenia: string
}

export interface CredencialResponse {
  autenticacion: Autenticacion;
  credenciales: Credenciales;
}

export interface Autenticacion {
  autenticacion: boolean;
}

export interface Credenciales {
  mensaje: string;
  idUsuario: number;
  rol: number | null;
  nombreRol: string | null;
  nombreUsuario: string | null;
  token: string | null;
}

/* Verificación de dos pasos */
export interface VerificacionResquest {
  token: string,
  codigoVerificacion: number
}

export interface VerificacionResponse {
  idUsuario: number,
  nombreRol: string,
  mensaje: string,
  nombreUsuario: string,
  rol: number
}

/* Recuperar Contraseña */
export interface RecuperarResquest {
  correo: string
}

export interface RecuperarResponse {
  mensaje: string,
  user: number,
  token: string
}

/* Verificar Cambio de Contraseña */
export interface SolicitarRequest {
  token: string
  codigoVerificacion: string
}

export interface SolicitarResponse {
  idUsuario: number,
  mensaje: string,
  nombreUsuario: string,
  token: string
}

/* Cambiar Contraseña */
export interface ActualizarResquest {
  idUsuario: number,
  token: string,
  nuevaContrasenia: string
}

export interface ActualizarResponse {
  message: string,
  status: string
}

/* Activar-Desactivar Autetificación */
export interface ActualizarAutetificacionResponse {
  message: string,
  status: string
}
