import { RegisterContratoDTO } from "../contrato-model.ts/ContratoModel";
import { DireccionModel } from "./DireccionModel";

// usuario-request.dto.ts
export interface UsuarioRequestDto {
  idUsuario: number;
  idPersona: number;
  idRol: number;
  idDireccion: number;
  nombreUsuario: string;
  contrasena: string;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  dpi: string;
  correo: string;
  telefono: string;
  estado: string;
}

// empleado-request.dto.ts
export interface EmpleadoRequestDto {
  idEmpleado: number;
  idUsuario: number;
  codigoEmpleado: string;
  numeroIgss: string;
  numeroIrtra: string;
  tipoSangre: string;
  estadoCivil: string;
  numeroDependientes: number;
  contactoEmergenciaNombre: string;
  contactoEmergenciaTelefono: string;
  estadoEmpleado: string;
  fechaIngreso: string;
  fechaSalida: string | null;
  motivoSalida: string | null;
}

export interface RegisterEmpleadoModel {
  usuarioRequestDdto: UsuarioRequestDto;
  empleadoRequestDdto: EmpleadoRequestDto;
  contrato: RegisterContratoDTO;
  direccion: DireccionModel;
}

