import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CredencialResquest, CredencialResponse, UsuarioRequest, UsuarioResponse, VerificacionResquest, VerificacionResponse, RecuperarResquest, RecuperarResponse, ActualizarAutetificacionResponse, SolicitarRequest, SolicitarResponse, ActualizarResquest, ActualizarResponse } from "../models/auth.model";
import { environment } from "../environments/environment";


@Injectable({providedIn: "root"})
export class AuthService {

    private readonly DIRECCION_API = `${environment.apiUrl}/login`;
    //readonly DIRECCION_API = "http://localhost:8081/login";

    constructor(private httpClient: HttpClient) {}

    /* Métodos POST */
    public registrarUsuario(usuarioNuevo: UsuarioRequest): Observable<(UsuarioResponse)> {
        return this.httpClient.post<UsuarioResponse>(this.DIRECCION_API + "/register", usuarioNuevo);
    }

    public iniciarSesion(credencial: CredencialResquest): Observable<CredencialResponse> {
        return this.httpClient.post<CredencialResponse>(this.DIRECCION_API, credencial);
    }

    public verificacionDosPasos(verificacion: VerificacionResquest): Observable<VerificacionResponse> {
        return this.httpClient.post<VerificacionResponse>(this.DIRECCION_API + "/verify-2fa", verificacion);
    }

    public recuperarContrasenia(recuperar: RecuperarResquest): Observable<RecuperarResponse> {
        return this.httpClient.post<RecuperarResponse>(this.DIRECCION_API + "/recover-password", recuperar);
    }

    public solicitarCambioContrasenia(solitar: SolicitarRequest): Observable<SolicitarResponse> {
        return this.httpClient.post<SolicitarResponse>(this.DIRECCION_API + "/verify-recovery", solitar);
    }

    public cambiarContrasenia(actualizar: ActualizarResquest): Observable<ActualizarResponse> {
        return this.httpClient.post<ActualizarResponse>(this.DIRECCION_API + "/reset-password", actualizar);
    }

    /* Métodos GET */



    /* Métodos PUT */
    public desactivarAutetificacion(idUsuario: number): Observable<ActualizarAutetificacionResponse> {
        return this.httpClient.put<ActualizarAutetificacionResponse>(this.DIRECCION_API + "/toggle-2fa/" + idUsuario,{});
    }

    /* Métodos DELETE */

    
}