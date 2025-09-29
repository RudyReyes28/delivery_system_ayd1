import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// Importaciones de Angular Material
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

//servicios y tipos
import { AsignacionService } from '../../../services/repartidor-service/Asignacion.service';
import { AsignacionItem } from '../../../models/repartidor-models/AsignacionModel';
import { ESTADOS_ASIGNACION, ESTADOS_GUIA } from '../../../models/repartidor-models/EstadosGuia';

@Component({
  selector: 'app-gestion-pedidos',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTabsModule,
    MatListModule,
    MatChipsModule,
    MatDividerModule,
    MatSelectModule,
    MatSlideToggleModule,
  ],
  templateUrl: './gestion-pedidos.html',
  styleUrl: './gestion-pedidos.scss',
})
export class GestionPedidos implements OnInit {
  asignaciones: AsignacionItem[] = [];
  loadingAsignaciones = false;

  //estados asignaion
  estadosAsginacion: string[] = ESTADOS_ASIGNACION;

  private readonly ID_USUARIO_ACTUAL = Number(sessionStorage.getItem('idUsuario'));

  constructor(private snackBar: MatSnackBar, private asignacionService: AsignacionService) {}
  ngOnInit(): void {
    this.setAsignaciones();
  }

  setAsignaciones(): void {
    this.asignacionService.getAsignaciones(this.ID_USUARIO_ACTUAL).subscribe({
      next: (data) => {
        this.asignaciones = data;
        this.asignaciones.forEach((element: AsignacionItem) => {
          element.expanded = false;
        });
      },
      error: (error) => {
        console.error('Error al obtener las asignaciones:', error);
        this.showSnackbar('Error al obtener las asignaciones', 'error');
      },
    });
  }

  aceptarAsignacion(idAsignacion: number): void {
    this.asignacionService.aceptarAsignacion(idAsignacion).subscribe({
      next: (data) => {
        this.showSnackbar('Asignación aceptada con éxito', 'success-snackbar');
        this.setAsignaciones();
      },
      error: (error) => {
        console.error('Error al aceptar la asignación:', error);
        this.showSnackbar('Error al aceptar la asignación', 'error');
      },
    });
  }

  recharzarAsignacion(idAsignacion: number): void {
    this.asignacionService.rechazarAsignacion(idAsignacion).subscribe({
      next: (data) => {
        this.showSnackbar('Asignación rechazada con éxito', 'success-snackbar');
        this.setAsignaciones();
      },
      error: (error) => {
        console.error('Error al rechazar la asignación:', error);
        this.showSnackbar('Error al rechazar la asignación', 'error');
      },
    });
  }

  filtrarPorEstado(estado: string): void {
    this.asignacionService.getAsignaciones(this.ID_USUARIO_ACTUAL).subscribe(
      {
        next: (data) => {
          this.asignaciones = data.filter((item) => item.asignacion.estadoAsignacion === estado);
        },
        error: (error) => {
          console.error('Error al filtrar las asignaciones:', error);
          this.showSnackbar('Error al filtrar las asignaciones', 'error');
        },
      }
    );
  }

  toggleExpanded(item: AsignacionItem): void {
    item.expanded = !item.expanded;
  }

  showSnackbar(message: string, tipo: string): void {
    const mensaje = message;
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }
}
