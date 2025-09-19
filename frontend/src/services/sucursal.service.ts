import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SucursalItem, EmpresaItem, UsuarioSucursal, SucursalFormData, CambiarEstadoSucursalRequest, CambiarEstadoSucursalResponse } from '../models/sucursal.model';

@Injectable({
  providedIn: 'root'
})
export class SucursalService {
  private baseUrl = 'http://localhost:8081/empresa/sucursal';

  constructor(private http: HttpClient) { }

  listarSucursales(): Observable<SucursalItem[]> {
    return this.http.get<SucursalItem[]>(`${this.baseUrl}/listar-sucursales`);
  }

  crearSucursal(sucursalData: SucursalFormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/nueva-sucursal`, sucursalData);
  }

  listarEmpresas(): Observable<EmpresaItem[]> {
    return this.http.get<EmpresaItem[]>(`${this.baseUrl}/listar-empresas`);
  }

  obtenerUsuariosSucursal(): Observable<UsuarioSucursal[]> {
    return this.http.get<UsuarioSucursal[]>(`${this.baseUrl}/usuarios-sucursal`);
  }

  cambiarEstadoSucursal(request: CambiarEstadoSucursalRequest): Observable<CambiarEstadoSucursalResponse> {
    return this.http.put<CambiarEstadoSucursalResponse>(`${this.baseUrl}/cambiar-estado`, request);
  }
}