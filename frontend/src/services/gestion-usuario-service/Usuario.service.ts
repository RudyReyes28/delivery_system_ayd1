import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioModel } from '../../models/usuario-model/UsuarioModel';
import { EmpleadoRequestDto, RegisterEmpleadoModel, UsuarioRequestDto } from '../../models/usuario-model/RegisterEmpleadoModel';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private API_URL: string = `${environment.apiUrl}/api/users`;

  constructor(private http: HttpClient) {}

  public crearUsuario(usuario: RegisterEmpleadoModel): Observable<EmpleadoRequestDto> {
    return this.http.post<EmpleadoRequestDto>(`${this.API_URL}/create`, usuario);
  }

  public actualizarUsuario(usuario: UsuarioRequestDto): Observable<UsuarioModel> {
    return this.http.put<UsuarioModel>(`${this.API_URL}/update`, usuario);
  }

  public obtenerUsuarios(): Observable<UsuarioModel[]> {
    return this.http.get<UsuarioModel[]>(`${this.API_URL}/all`);
  }
}
