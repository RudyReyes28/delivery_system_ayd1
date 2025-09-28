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

@Injectable({
  providedIn: 'root'
})
export class AdminLiquidacionService {
  private readonly baseUrl = 'http://localhost:8081/admin/liquidacion/repartidor';

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
}