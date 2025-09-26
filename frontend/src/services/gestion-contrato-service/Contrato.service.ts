import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContratoModel, RegisterContratoDTO } from '../../models/contrato-model.ts/ContratoModel';

@Injectable({
  providedIn: 'root',
})
export class ContratoService {
  private API_URL: string = `${environment.apiUrl}/api/contratos`;
  constructor(private http: HttpClient) {}

  public crearContrato(contratoData: RegisterContratoDTO): Observable<ContratoModel> {
    return this.http.post<ContratoModel>(`${this.API_URL}/crear-contrato`, contratoData);
  }

  public actualizarContrato(contratoData: ContratoModel): Observable<ContratoModel> {
    return this.http.put<ContratoModel>(`${this.API_URL}/actualizar-contrato`, contratoData);
  }

  public contratosPorEmpleado(idEmpleado: number): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(`${this.API_URL}/contratos-por-empleado/${idEmpleado}`);
  }

  public obtenerTodosLosContratos(): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(`${this.API_URL}/get-all`);
  }

  public contratosPorEstado(estado: string): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(`${this.API_URL}/contratos-por-estado/${estado}`);
  }

  public contratosPorModalidad(modalidad: string): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(`${this.API_URL}/contratos-por-modalidad/${modalidad}`);
  }

  public contratosPorTipoContrato(tipoContrato: string): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(
      `${this.API_URL}/contratos-por-tipo-contrato/${tipoContrato}`
    );
  }

  public contratosEntreFechas(fecha1: string, fecha2: string): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(
      `${this.API_URL}/contratos-entre-fechas/${fecha1}/${fecha2}`
    );
  }

  public contratosApuntoDeCaducir(): Observable<ContratoModel[]> {
    return this.http.get<ContratoModel[]>(`${this.API_URL}/a-punto-de-caducir`);
  }
}
