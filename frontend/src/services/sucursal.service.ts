import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SucursalItem, EmpresaItem, UsuarioSucursal, SucursalFormData, CambiarEstadoSucursalRequest, CambiarEstadoSucursalResponse } from '../models/sucursal.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SucursalService {

  private readonly DIRECCION_API = `${environment.apiUrl}/empresa/sucursalr`;
  //private baseUrl = 'http://localhost:8081/empresa/sucursal';

  constructor(private http: HttpClient) { }

  listarSucursales(): Observable<SucursalItem[]> {
    return this.http.get<SucursalItem[]>(`${this.DIRECCION_API}/listar-sucursales`);
  }

  crearSucursal(sucursalData: SucursalFormData): Observable<any> {
    return this.http.post(`${this.DIRECCION_API}/nueva-sucursal`, sucursalData);
  }

  listarEmpresas(): Observable<EmpresaItem[]> {
    return this.http.get<EmpresaItem[]>(`${this.DIRECCION_API}/listar-empresas`);
  }

  obtenerUsuariosSucursal(): Observable<UsuarioSucursal[]> {
    return this.http.get<UsuarioSucursal[]>(`${this.DIRECCION_API}/usuarios-sucursal`);
  }

  cambiarEstadoSucursal(request: CambiarEstadoSucursalRequest): Observable<CambiarEstadoSucursalResponse> {
    return this.http.put<CambiarEstadoSucursalResponse>(`${this.DIRECCION_API}/cambiar-estado`, request);
  }
}