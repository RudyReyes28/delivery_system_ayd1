import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  CancelacionClienteResponse, 
  AtenderCancelacionRequest,
  AtenderCancelacionResponse
} from '../../models/coordinador/cancelacion-cliente.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CancelacionClienteService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las cancelaciones de cliente
   */
  getCancelacionesCliente(): Observable<CancelacionClienteResponse[]> {
    return this.http.get<CancelacionClienteResponse[]>(
      `${this.baseUrl}/coordinador/cancelaciones/cliente/cancelaciones`
    );
  }

  /**
   * Atiende una cancelación de cliente
   * @param request Datos de la atención de la cancelación
   */
  atenderCancelacion(request: AtenderCancelacionRequest): Observable<AtenderCancelacionResponse> {
    return this.http.post<AtenderCancelacionResponse>(
      `${this.baseUrl}/coordinador/cancelaciones/cliente/cancelacion/atender`,
      request
    );
  }
}
