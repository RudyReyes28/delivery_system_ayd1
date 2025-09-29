import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SolicitudCancelacion, AceptarCancelacionRequest, CancelacionResponse } from '../../models/coordinador/cancelacion.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CoordinadorService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las solicitudes de cancelación de guías
   */
  getSolicitudesCancelacion(): Observable<SolicitudCancelacion[]> {
    return this.http.get<SolicitudCancelacion[]>(`${this.baseUrl}/coordinador/cancelaciones/solicitudes`);
  }

  /**
   * Acepta una solicitud de cancelación
   * @param request Datos de la solicitud a aceptar
   */
  aceptarSolicitudCancelacion(request: AceptarCancelacionRequest): Observable<CancelacionResponse> {
    return this.http.post<CancelacionResponse>(`${this.baseUrl}/coordinador/cancelaciones/solicitud/aceptar`, request);
  }

  /**
   * Rechaza una solicitud de cancelación
   * @param idCancelacion ID de la cancelación a rechazar
   */
  rechazarSolicitudCancelacion(idCancelacion: number): Observable<CancelacionResponse> {
    return this.http.post<CancelacionResponse>(`${this.baseUrl}/coordinador/cancelaciones/solicitud/cancelar/${idCancelacion}`, {});
  }
}
