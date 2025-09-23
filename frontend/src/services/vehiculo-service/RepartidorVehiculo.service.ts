import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { RepartidorVehiculoDTO } from '../../models/RepartidorVehiculoModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RepartidorVehiculoService {
  private API_URL: string = `${environment.apiUrl}/api/repartidor-vehiculo`;
  constructor(private http: HttpClient) {}

  public crearRepartidorVehiculo(
    repartidorVehiculoDTO: RepartidorVehiculoDTO
  ): Observable<RepartidorVehiculoDTO> {
    return this.http.post<RepartidorVehiculoDTO>(
      `${this.API_URL}/crear-repartidor-vehiculo`,
      repartidorVehiculoDTO
    );
  }

  public actualizarRepartidorVehiculo(
    repartidorVehiculoDTO: RepartidorVehiculoDTO
  ): Observable<RepartidorVehiculoDTO> {
    return this.http.put<RepartidorVehiculoDTO>(
      `${this.API_URL}/actualizar-repartidor-vehiculo`,
      repartidorVehiculoDTO
    );
  }

  public vehiculosAsignados(idRepartidor: number): Observable<RepartidorVehiculoDTO[]> {
    return this.http.get<RepartidorVehiculoDTO[]>(
      `${this.API_URL}/get-vehiculos-asignados/${idRepartidor}`
    );
  }

  public getAsignacionesVehiculares(): Observable<RepartidorVehiculoDTO[]> {
    return this.http.get<RepartidorVehiculoDTO[]>(`${this.API_URL}/get-all`);
  }
}
