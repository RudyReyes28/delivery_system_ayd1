import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { GuiaClienteDetalleResponse } from "../models/guia.cliente.model";

@Injectable({providedIn: "root"})
export class GuiaClienteService {

    private readonly DIRECCION_API = `${environment.apiUrl}/public/guia`;
    
    constructor(private httpClient: HttpClient) {}

    /* Métodos POST */

    /* Métodos GET */

    public obtenerDetalleGuiaCliente(numeroGuia: number): Observable<GuiaClienteDetalleResponse> {
        return this.httpClient.get<GuiaClienteDetalleResponse>(this.DIRECCION_API + "/detalle/GUIA-" + numeroGuia);
    }

    /* Métodos PUT */

    /* Métodos DELETE */
}