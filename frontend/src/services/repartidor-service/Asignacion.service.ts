import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AsignacionItem } from '../../models/repartidor-models/AsignacionModel';

@Injectable({
  providedIn: 'root',
})
export class AsignacionService {
  private URL_API: string = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  public getAsignaciones(idUsuario: number): Observable<AsignacionItem[]> {
    return this.http.get<AsignacionItem[]>(
      `${this.URL_API}/repartidor/pedido/asignaciones/${idUsuario}`
    );
  }

  public aceptarAsignacion(idAsignacion: number): Observable<any> {
    return this.http.post<any>(
      `${this.URL_API}/repartidor/pedido/aceptar-asignacion/${idAsignacion}`,
      {}
    );
  }

  public rechazarAsignacion(idAsignacion: number): Observable<any> {
    return this.http.post<any>(
      `${this.URL_API}/repartidor/pedido/rechazar-asignacion/${idAsignacion}`,
      {}
    );
  }
}
