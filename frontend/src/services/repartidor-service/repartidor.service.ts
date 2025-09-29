import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CancelacionClienteRequest, CancelacionClienteResponse } from '../../models/repartidor/cancelacion-cliente.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RepartidorService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Registra una cancelación de guía por parte del cliente
   * @param request Datos de la cancelación
   * @returns Respuesta con mensaje de confirmación
   */
  registrarCancelacionCliente(request: CancelacionClienteRequest): Observable<CancelacionClienteResponse> {
    return this.http.post<CancelacionClienteResponse>(
      `${this.apiUrl}/cliente/cancelaciones/guias/incidencia`, 
      request
    );
  }

  // Aquí se pueden agregar otros métodos relacionados con el repartidor según sea necesario
}
