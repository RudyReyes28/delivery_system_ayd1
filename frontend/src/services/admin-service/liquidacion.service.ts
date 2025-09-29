
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PeriodoLiquidacion, CerrarPeriodoResponse } from '../../models/liquidacion/periodo.model';

@Injectable({
  providedIn: 'root'
})
export class LiquidacionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Método para cerrar un periodo de liquidación
  cerrarPeriodoLiquidacion(idPeriodo: number): Observable<CerrarPeriodoResponse> {
    return this.http.post<CerrarPeriodoResponse>(
      `${this.apiUrl}/admin/liquidacion/repartidor/cerrar-periodo/${idPeriodo}`, 
      {}
    );
  }

  // ...existing methods...
}