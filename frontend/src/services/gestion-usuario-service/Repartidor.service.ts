import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RepartidorDTO } from '../../models/usuario-model/RepartidorModel';

@Injectable({
  providedIn: 'root',
})
export class RepartidorService {
  private API_URL: string = `${environment.apiUrl}/api/repartidores`;
  constructor(private http: HttpClient) {}


  public crearRepartidor(repartidor: RepartidorDTO): Observable<RepartidorDTO> {
    return this.http.post<RepartidorDTO>(`${this.API_URL}/crear-repartidor`, repartidor);
  }

  public actualizarRepartidor(repartidor: RepartidorDTO): Observable<RepartidorDTO> {
    return this.http.put<RepartidorDTO>(`${this.API_URL}/actualizar-repartidor`, repartidor);
  }

  public obtenerRepartidores(): Observable<RepartidorDTO[]> {
    return this.http.get<RepartidorDTO[]>(`${this.API_URL}/all`);
  }

  public obtenerRepartidorPorId(id: number): Observable<RepartidorDTO> {
    return this.http.get<RepartidorDTO>(`${this.API_URL}/find-by-id/${id}`);
  }

  public obtenerRepartidoresPorTipoLicencia(tipoLicencia: string): Observable<RepartidorDTO[]> {
    return this.http.get<RepartidorDTO[]>(`${this.API_URL}/find-by-tipo-licencia/${tipoLicencia}`);
  }

  public obtenerRepartidoresPorDisponibilidad(disponibilidad: string): Observable<RepartidorDTO[]> {
    return this.http.get<RepartidorDTO[]>(`${this.API_URL}/find-by-disponibilidad/${disponibilidad}`);
  }
}
