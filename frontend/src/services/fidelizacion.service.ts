import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpresaData, AsignarCambiarRequest, AsignarCambiarResponse, AccionFidelizacion, Empresa } from '../models/fidelizacion.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FidelizacionService {

  private readonly DIRECCION_API = `${environment.apiUrl}/empresa/fidelizacion`;
  //private readonly baseUrl = 'http://localhost:8081/empresa/fidelizacion';

  constructor(private http: HttpClient) {}

  obtenerEmpresas(): Observable<EmpresaData[]> {
    return this.http.get<EmpresaData[]>(`${this.DIRECCION_API}/listar-programas`);
  }

  asignarProgramaFidelizacion(request: AsignarCambiarRequest): Observable<AsignarCambiarResponse> {
    return this.http.post<AsignarCambiarResponse>(`${this.DIRECCION_API}/nuevo-programa`, request);
  }

  cambiarNivelFidelizacion(request: AsignarCambiarRequest): Observable<AsignarCambiarResponse> {
    return this.http.put<AsignarCambiarResponse>(`${this.DIRECCION_API}/cambiar-nivel`, request);
  }

  ejecutarAccionFidelizacion(accion: AccionFidelizacion, request: AsignarCambiarRequest): Observable<AsignarCambiarResponse> {
    return accion === 'asignar' 
      ? this.asignarProgramaFidelizacion(request)
      : this.cambiarNivelFidelizacion(request);
  }

  generarMensajeResultado(response: AsignarCambiarResponse, accion: AccionFidelizacion): string {
    const accionTexto = accion === 'asignar' ? 'asignado' : 'cambiado';
    
    return `¡Programa de fidelización ${accionTexto} exitosamente!
    
    Empresa: ${response.empresa.nombreComercial}
    Nivel: ${response.nivelFidelizacion.nombreNivel}
    Descuento: ${response.nivelFidelizacion.descuentoPorcentaje}%
    Periodo: ${response.mes}/${response.anio}

    Estadísticas actualizadas:
    • Entregas: ${response.totalEntregas}
    • Monto total: $${response.montoTotalEntregas}
    • Cancelaciones: ${response.totalCancelaciones}
    • Descuento aplicado: $${response.descuentoAplicado}`;
  }

  tienePrograma(empresa: Empresa): boolean {
    return !!(empresa.empresaFidelizacion && empresa.nivelFidelizacion);
  }

  puedeAsignar(empresa: Empresa): boolean {
    return !this.tienePrograma(empresa);
  }

  puedeCambiar(empresa: Empresa): boolean {
    return this.tienePrograma(empresa);
  }

  obtenerEstadoPrograma(empresa: Empresa): string {
    if (!this.tienePrograma(empresa)) {
      return 'Sin programa';
    }
    
    return empresa.nivelFidelizacion?.nombreNivel || 'Programa asignado';
  }

  formatearMoneda(cantidad: number): string {
    if (cantidad === null || cantidad === undefined) return '$0.00';
    
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'USD'
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

  validarDatosAccion(empresaId: number, codigoNivel: string): boolean {
    return !!(empresaId && codigoNivel && codigoNivel.trim());
  }
}