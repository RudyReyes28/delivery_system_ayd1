import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GuiaItem } from '../../models/repartidor-models/SeguimientoModel';

@Injectable({
  providedIn: 'root',
})
export class SeguimientoService {
  private URL_API: string = `${environment.apiUrl}`;
  constructor(private http: HttpClient) {}

  public getPedidosAsignados(idUsuario: number): Observable<GuiaItem[]> {
    return this.http.get<GuiaItem[]>(
      `${this.URL_API}/repartidor/pedido/pedidos-asignados/${idUsuario}`
    );
  }

  public cambiarEstadoPedido(
    idGuia: number,
    idUsuario: number,
    nuevoEstado: string,
    motivoCambio: string,
    comentarios: string): Observable<any> {
    return this.http.post<any>(`${this.URL_API}/repartidor/pedido/cambiar-estado`, {
      idGuia: idGuia,
      idUsuario: idUsuario,
      nuevoEstado: nuevoEstado,
      motivoCambio: motivoCambio,
      comentarios: comentarios
    });
  }
}
