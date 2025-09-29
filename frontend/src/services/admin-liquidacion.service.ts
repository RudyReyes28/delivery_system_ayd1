import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  PeriodoLiquidacion, 
  RepartidorResponse, 
  NuevoPeriodoRequest, 
  ProcesarLiquidacionRequest, 
  ApiResponse 
} from '../models/admin-liquidacion.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminLiquidacionService {
  private readonly baseUrl = `${environment.apiUrl}/admin/liquidacion/repartidor`;

  constructor(private http: HttpClient) {}

  /**
   * Crea un nuevo periodo de liquidación
   */
  crearNuevoPeriodo(periodo: NuevoPeriodoRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/nuevo-periodo`, periodo);
  }

  /**
   * Obtiene el periodo activo actual
   */
  obtenerPeriodoActivo(): Observable<PeriodoLiquidacion> {
    return this.http.get<PeriodoLiquidacion>(`${this.baseUrl}/periodo-activo`);
  }

  /**
   * Procesa la liquidación de un repartidor
   */
  procesarLiquidacion(request: ProcesarLiquidacionRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/procesar-liquidacion`, request);
  }

  /**
   * Obtiene todos los repartidores con sus liquidaciones
   */
  obtenerRepartidoresLiquidacion(): Observable<RepartidorResponse[]> {
    return this.http.get<RepartidorResponse[]>(`${this.baseUrl}/liquidaciones`);
  }
  
  /**
   * Cierra un periodo de liquidación
   * @param idPeriodo ID del periodo a cerrar
   */
  cerrarPeriodo(idPeriodo: number): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/cerrar-periodo/${idPeriodo}`, {});
  }
}