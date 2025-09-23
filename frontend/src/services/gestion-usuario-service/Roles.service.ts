import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rol } from '../../models/usuario-model/RolModel';

@Injectable({
  providedIn: 'root'
})
export class RolesService {

  private API_URL: string = `${environment.apiUrl}/api/roles`;
  constructor(private http: HttpClient) { }

  public getRoles():Observable<Rol[]> {
    return this.http.get<Rol[]>(`${this.API_URL}/get-all`);
  }

}
