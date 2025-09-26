import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';
import { Empresa, EmpresaResponse } from '../models/general-sucursal.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private readonly DIRECCION_API = `${environment.apiUrl}/sucursal/manager`;
  //private readonly baseUrl = 'http://localhost:8081/sucursal/manager';

  constructor(private http: HttpClient) {}

  obtenerInformacionEmpresa(idUsuario: number): Observable<Empresa> {
    return this.http.get<EmpresaResponse>(`${this.DIRECCION_API}/informacion/${idUsuario}`)
      .pipe(
        map(response => response.empresa)
      );
  }

  obtenerInformacionFidelizacion(idUsuario: number): Observable<Empresa> {
    return this.http.get<EmpresaResponse>(`${this.DIRECCION_API}/fidelizacion/${idUsuario}`)
      .pipe(
        map(response => response.empresa)
      );
  }

  obtenerInformacionCompleta(idUsuario: number): Observable<{empresa: Empresa, empresaFidelizacion: Empresa}> {
    return forkJoin({
      empresa: this.obtenerInformacionEmpresa(idUsuario),
      empresaFidelizacion: this.obtenerInformacionFidelizacion(idUsuario)
    });
  }

  formatearDiasOperacion(dias: string): string {
    if (!dias) return '';
    
    const diasMap: { [key: string]: string } = {
      'L': 'Lun',
      'M': 'Mar',
      'J': 'Mié',
      'V': 'Jue',
      'S': 'Vie',
      'D': 'Dom'
    };
    
    return dias.split('').map(dia => diasMap[dia] || dia).join(', ');
  }

  obtenerColorEstado(estado: string): string {
    if (!estado) return 'accent';
    
    switch (estado.toUpperCase()) {
      case 'ACTIVA':
      case 'ACTIVO':
        return 'primary';
      case 'INACTIVA':
      case 'INACTIVO':
        return 'warn';
      default:
        return 'accent';
    }
  }

  formatearMoneda(cantidad: number): string {
    if (cantidad === null || cantidad === undefined) return 'Q 0.00';
    
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(cantidad);
  }


  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    
    return new Date(fecha).toLocaleDateString('es-GT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  obtenerNombreMes(mes: number): string {
    const meses = [
      'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
      'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];
    return meses[mes - 1] || '';
  }
}