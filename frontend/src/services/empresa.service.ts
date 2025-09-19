import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { UsuarioSucursal, EmpresaItem, CambiarEstadoRequest, CambiarEstadoResponse, EmpresaFormData } from "../models/empresa.model";

@Injectable({providedIn: "root"})
export class EmpresaService {

    readonly DIRECCION_API = "http://localhost:8081/empresa";
    

    constructor(private httpClient: HttpClient) {}

    /* Métodos GET */
    public obtenerUsuariosSucursal(): Observable<UsuarioSucursal[]> {
        return this.httpClient.get<UsuarioSucursal[]>(this.DIRECCION_API + "/usuarios-sucursal");
    }

    public listarEmpresas(): Observable<EmpresaItem[]> {
        return this.httpClient.get<EmpresaItem[]>(this.DIRECCION_API + "/listar-empresas");
    }

    /* Métodos POST */
    public crearEmpresa(empresaData: EmpresaFormData): Observable<any> {
        return this.httpClient.post(this.DIRECCION_API + "/nueva-empresa", empresaData);
    }

    /* Métodos PUT */
    public editarEmpresa(idEmpresa: number, empresaData: EmpresaFormData): Observable<any> {
        return this.httpClient.put(this.DIRECCION_API + "/editar-empresa/" + idEmpresa, empresaData);
    }

    public cambiarEstadoEmpresa(request: CambiarEstadoRequest): Observable<CambiarEstadoResponse> {
        return this.httpClient.put<CambiarEstadoResponse>(this.DIRECCION_API + "/cambiar-estado", request);
    }

    /* Métodos DELETE */
}