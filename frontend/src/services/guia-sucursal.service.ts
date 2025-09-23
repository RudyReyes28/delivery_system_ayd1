import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ClienteRegistradoResponse, GuiaResponse, NuevaGuiaClienteExistenteRequest, NuevaGuiaClienteExistenteResponse, NuevaGuiaClienteNuevoRequest, NuevaGuiaClienteNuevoResponse, TipoServicioResponse } from "../models/guia-sucursal.model";

@Injectable({providedIn: "root"})
export class GuiaService {

    private readonly DIRECCION_API = `${environment.apiUrl}/sucursal/guia`;
        
    constructor(private httpClient: HttpClient) {}

    /* Métodos POST */

    public crearGuiaClienteNuevo(guiaNueva: NuevaGuiaClienteNuevoRequest): Observable<NuevaGuiaClienteNuevoResponse> {
        return this.httpClient.post<NuevaGuiaClienteNuevoResponse>(this.DIRECCION_API + "/nueva-guia-cliente-nuevo", guiaNueva);
    }

    public crearGuiaClienteExistente(guiaNueva: NuevaGuiaClienteExistenteRequest): Observable<NuevaGuiaClienteExistenteResponse> {
        return this.httpClient.post<NuevaGuiaClienteExistenteResponse>(this.DIRECCION_API + "/nueva-guia-cliente-existente", guiaNueva);
    }

    /* Métodos GET */  
    public obtenerClientes(): Observable<ClienteRegistradoResponse[]> {
        return this.httpClient.get<ClienteRegistradoResponse[]>(this.DIRECCION_API + "/clientes")
    }

    public obtenerTiposServicios(): Observable<TipoServicioResponse[]> {
        return this.httpClient.get<TipoServicioResponse[]>(this.DIRECCION_API + "/tipos-servicio")
    }

    public obtenerGuiaUsuario(idUsuario: number): Observable<GuiaResponse[]> {
        return this.httpClient.get<GuiaResponse[]>(this.DIRECCION_API + "/listar-guias/" + idUsuario);
    }

    /* Métodos PUT */

    /* Métodos DELETE */
}