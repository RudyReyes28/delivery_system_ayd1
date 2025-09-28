import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GuiaResponse, Repartidor, RepartidorCompleto,AsignarRepartidorRequest, AsignarRepartidorResponse } from '../models/guia-repartidor.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GuiaRepartidorService {

  private readonly DIRECCION_API = `${environment.apiUrl}/coordinador/guias`;
  //readonly DIRECCION_API = 'http://localhost:8081/coordinador/guias';

  constructor(private http: HttpClient) { }

  obtenerTodasLasGuias(): Observable<GuiaResponse[]> {
    return this.http.get<GuiaResponse[]>(`${this.DIRECCION_API}/obtener-guias`);
  }

  obtenerGuiasSinRepartidor(): Observable<GuiaResponse[]> {
    return this.http.get<GuiaResponse[]>(`${this.DIRECCION_API}/guias-sin-repartidor`);
  }

  obtenerRepartidoresActivos(): Observable<Repartidor[]> {
    return this.http.get<Repartidor[]>(`${this.DIRECCION_API}/repartidores-activos`);
  }

  obtenerTodosLosRepartidores(): Observable<RepartidorCompleto[]> {
    return this.http.get<RepartidorCompleto[]>(`${this.DIRECCION_API}/repartidores`);
  }

  asignarRepartidor(request: AsignarRepartidorRequest): Observable<AsignarRepartidorResponse> {
    return this.http.post<AsignarRepartidorResponse>(`${this.DIRECCION_API}/asignar-repartidor`, request);
  }
}