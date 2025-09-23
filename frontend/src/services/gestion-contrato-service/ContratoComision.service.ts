import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContratoComisionDTO } from '../../models/contrato-model.ts/ContratoModel';

@Injectable({
  providedIn: 'root',
})
export class ContratoComisionService {
  private API_URL: string = `${environment.apiUrl}/api/contrato-comision`;

  constructor(private http: HttpClient) {}

  public actualizarContratoComision(
    contratoComisionData: ContratoComisionDTO
  ): Observable<ContratoComisionDTO> {
    return this.http.put<ContratoComisionDTO>(
      `${this.API_URL}/actualizar-comision-contrato`,
      contratoComisionData
    );
  }

  public obtenerContratoComisionPorId(id: number): Observable<ContratoComisionDTO> {
    return this.http.get<ContratoComisionDTO>(
      `${this.API_URL}/obtener-por-id/${id}`
    );
  }
}
