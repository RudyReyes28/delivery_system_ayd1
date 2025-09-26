import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { PersonaModel } from '../../models/usuario-model/PersonaModel';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PersonaService {
  private API_URL: string = `${environment.apiUrl}/api/personas`;
  constructor(private http: HttpClient) {}

  public getPersonas(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.API_URL}/get-all`);
  }

  public getPersonaPorDpi(dpi: string): Observable<PersonaModel> {
    return this.http.get<PersonaModel>(`${this.API_URL}/get-by-dpi/${dpi}`);
  }

  public getPersonaPorId(id: number): Observable<PersonaModel> {
    return this.http.get<PersonaModel>(`${this.API_URL}/get-by-id/${id}`);
  }
}
