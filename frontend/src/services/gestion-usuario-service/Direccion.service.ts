import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DireccionModel } from '../../models/usuario-model/DireccionModel';

@Injectable({
  providedIn: 'root',
})
export class DireccionService {
  private API_URL: string = `${environment.apiUrl}/api/direcciones`;
  constructor(private http: HttpClient) {}

  public obtenerDirecciones(): Observable<DireccionModel[]> {
    return this.http.get<DireccionModel[]>(`${this.API_URL}/get-all`);
  }
  public crearDireccion(direccionData: DireccionModel): Observable<DireccionModel> {
    return this.http.post<DireccionModel>(`${this.API_URL}/create`, direccionData);
  }
  public actualizarDireccion(direccionData: DireccionModel): Observable<DireccionModel> {
    return this.http.put<DireccionModel>(`${this.API_URL}/update`, direccionData);
  }

  public obtenerDireccionPorId(id: number): Observable<DireccionModel> {
    return this.http.get<DireccionModel>(`${this.API_URL}/get-by-id/${id}`);
  }
}
