import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IncidenciaModel } from '../../models/repartidor-models/IncidenciaModel';

@Injectable({
  providedIn: 'root',
})
export class IncidenciaService {
  private URL_API: string = `${environment.apiUrl}/api/incidencias`;
  constructor(private http: HttpClient) {}

  public crearIncidencia(incidenciaDTO: IncidenciaModel): Observable<IncidenciaModel> {
    return this.http.post<IncidenciaModel>(`${this.URL_API}/crear`, incidenciaDTO);
  }

  public actualizarIncidencia(incidenciaDTO: IncidenciaModel): Observable<IncidenciaModel> {
    return this.http.put<IncidenciaModel>(`${this.URL_API}/actualizar`, incidenciaDTO);
  }

  public getIncidenciasPorGuia(idGuia: number): Observable<IncidenciaModel[]> {
    return this.http.get<IncidenciaModel[]>(`${this.URL_API}/obtener-por-guia/${idGuia}`);
  }

  public getByTipoIncidencia(tipoIncidencia: string): Observable<IncidenciaModel[]> {
    return this.http.get<IncidenciaModel[]>(`${this.URL_API}/obtener-por-tipo/${tipoIncidencia}`);
  }

  public getBySeveridad(severidad: string): Observable<IncidenciaModel[]> {
    return this.http.get<IncidenciaModel[]>(`${this.URL_API}/obtener-por-severidad/${severidad}`);
  }

  public getByEstado(estado: string): Observable<IncidenciaModel[]> {
    return this.http.get<IncidenciaModel[]>(`${this.URL_API}/obtener-por-estado/${estado}`);
  }

  public getIncidencias(): Observable<IncidenciaModel[]> {
    return this.http.get<IncidenciaModel[]>(`${this.URL_API}/listar`);
  }
}
