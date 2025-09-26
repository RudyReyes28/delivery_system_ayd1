import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpleadoRequestDto } from '../../models/usuario-model/RegisterEmpleadoModel';

@Injectable({
  providedIn: 'root',
})
export class EmpleadoService {
  private API_URL: string = `${environment.apiUrl}/api/empleados`;
  constructor(private http: HttpClient) {}

  public obtenerTodosLosEmpleados(): Observable<EmpleadoRequestDto[]> {
    return this.http.get<EmpleadoRequestDto[]>(`${this.API_URL}/all`);
  }


  public getEmpleadoPorIdsuario(idUsuario: number): Observable<EmpleadoRequestDto> {
    return this.http.get<EmpleadoRequestDto>(`${this.API_URL}/get-by-idUser/${idUsuario}`);
  }

  public actualizarEmpleado(empleado: EmpleadoRequestDto): Observable<EmpleadoRequestDto> {
    return this.http.put<EmpleadoRequestDto>(`${this.API_URL}/actualizar-empleado`, empleado);
  }
}
