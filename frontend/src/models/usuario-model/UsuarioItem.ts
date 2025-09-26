import { EmpleadoRequestDto } from "./RegisterEmpleadoModel";
import { UsuarioModel } from "./UsuarioModel";

export interface UsuarioItem {
  usuario: UsuarioModel;
  empleado: EmpleadoRequestDto;
  expanded: boolean;
}
