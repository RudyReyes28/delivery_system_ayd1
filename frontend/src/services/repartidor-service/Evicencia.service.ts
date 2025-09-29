import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EvidenciaModel } from '../../models/repartidor-models/EvidenciaModel';

@Injectable({
  providedIn: 'root',
})
export class EvidenciaService {
  private URL_API: string = `${environment.apiUrl}/api/evidencias`;

  constructor(private http: HttpClient) {}

  public crearEvidencia(evidencia: EvidenciaModel): Observable<EvidenciaModel> {
    return this.http.post<EvidenciaModel>(`${this.URL_API}/create`, evidencia);
  }

  public actualizarEvidencia(evidencia: EvidenciaModel): Observable<EvidenciaModel> {
    return this.http.put<EvidenciaModel>(`${this.URL_API}/update`, evidencia);
  }

  public getEvidenciasPorGuia(idGuia: number): Observable<EvidenciaModel[]> {
    return this.http.get<EvidenciaModel[]>(`${this.URL_API}/get-by-guia/${idGuia}`);
  }
}
