import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Vehiculo } from '../../models/VehiculoModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VehiculoService {
  ///api/vehiculos
  private API_URL: string = `${environment.apiUrl}/api/vehiculos`;
  constructor(private http: HttpClient) {}

  /**
   * @PostMapping("/create")
    public Vehiculo create(@RequestBody Vehiculo vehiculo){
        return vehiculoService.create(vehiculo);
    }

    @PutMapping("/update/{id}")
    public Vehiculo update(@PathVariable Long id, @RequestBody Vehiculo vehiculo){
        return vehiculoService.update(vehiculo);
    }

    @GetMapping("/get-all")
    public List<Vehiculo> getAll(){
        return vehiculoService.findAll();
    }

    @GetMapping("/get-by-id/{id}")
    public Vehiculo getById(@PathVariable Long id){
        return vehiculoService.findById(id);
    }
   */
  public crearVehiculo(vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.post<Vehiculo>(`${this.API_URL}/create`, vehiculo);
  }

  public actualizarVehiculo(vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.put<Vehiculo>(`${this.API_URL}/update`, vehiculo);
  }

  public obtenerVehiculos(): Observable<Vehiculo[]> {
    return this.http.get<Vehiculo[]>(`${this.API_URL}/get-all`);
  }

  public obtenerVehiculoPorId(id: number): Observable<Vehiculo> {
    return this.http.get<Vehiculo>(`${this.API_URL}/get-by-id/${id}`);
  }
}
