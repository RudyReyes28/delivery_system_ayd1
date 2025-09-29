import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CancelacionGuia, CancelacionResponse } from '../models/cancelacion-guia.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CancelacionService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las guías disponibles para cancelación para un usuario de sucursal
   * @param idUsuario ID del usuario de la sucursal
   * @returns Lista de guías cancelables
   */
  getGuiasCancelables(idUsuario: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/sucursal/cancelaciones/guias/${idUsuario}`);
  }

  /**
   * Registra una solicitud de cancelación de guía
   * @param cancelacion Datos de la cancelación
   * @returns Respuesta de la operación
   */
  cancelarGuia(cancelacion: CancelacionGuia): Observable<CancelacionResponse> {
    return this.http.post<CancelacionResponse>(`${this.baseUrl}/sucursal/cancelaciones/guias/cancelar`, cancelacion);
  }
}
