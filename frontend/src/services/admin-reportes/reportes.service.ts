import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ReporteEntregas } from '../../models/admin-reportes/reporte-entregas.model';
import { ReporteComisiones } from '../../models/admin-reportes/reporte-comisiones.model';
import { ReporteDescuentos } from '../../models/admin-reportes/reporte-descuentos.model';
import { ReporteCancelaciones } from '../../models/admin-reportes/reporte-cancelaciones.model';

@Injectable({
  providedIn: 'root'
})
export class ReportesService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene el reporte de entregas (completadas, canceladas, rechazadas)
   */
  getReporteEntregas(): Observable<ReporteEntregas> {
    return this.http.get<ReporteEntregas>(`${this.apiUrl}/administrador/reportes/entregas`);
  }

  /**
   * Obtiene el reporte de comisiones por repartidor y periodo
   */
  getReporteComisiones(): Observable<ReporteComisiones[]> {
    return this.http.get<ReporteComisiones[]>(`${this.apiUrl}/administrador/reportes/comisiones-repartidores`);
  }

  /**
   * Obtiene el reporte de descuentos aplicados por nivel de tarjeta
   */
  getReporteDescuentos(): Observable<ReporteDescuentos> {
    return this.http.get<ReporteDescuentos>(`${this.apiUrl}/administrador/reportes/descuentos-aplicados`);
  }

  /**
   * Obtiene el reporte de cancelaciones por empresa y categoría
   */
  getReporteCancelaciones(): Observable<ReporteCancelaciones> {
    return this.http.get<ReporteCancelaciones>(`${this.apiUrl}/administrador/reportes/cancelaciones-empresa`);
  }
}
