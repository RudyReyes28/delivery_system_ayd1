import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTabsModule } from '@angular/material/tabs';

import { CancelacionService } from '../../../services/cancelacion.service';
import { CancelacionGuia, MotivoCancelacion } from '../../../models/cancelacion-guia.model';
import { CancelacionDialogComponent } from './cancelacion-dialog.component'

@Component({
  selector: 'app-cancelacion-guias',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatDividerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTabsModule
  ],
  templateUrl: './cancelacion-guias.component.html',
  styleUrls: ['./cancelacion-guias.component.css']
})
export class CancelacionGuiasComponent implements OnInit {
  guias: any[] = [];
  expandedGuias: { [key: number]: boolean } = {};
  loading: boolean = false;
  idUsuario: number = 0; // Se obtendrá de la sesión del usuario
  
  motivosCancelacion = Object.values(MotivoCancelacion);

  constructor(
    private cancelacionService: CancelacionService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // Simular obtención del ID de usuario desde la sesión o localStorage
    this.idUsuario = Number(localStorage.getItem('idUsuario')) || 2;
    this.loadGuias();
  }

  loadGuias(): void {
    this.loading = true;
    this.cancelacionService.getGuiasCancelables(this.idUsuario).subscribe({
      next: (data) => {
        this.guias = data;
        // Inicializar el estado expandido para cada guía
        this.guias.forEach(guia => {
          this.expandedGuias[guia.guia.idGuia] = false;
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar guías cancelables:', error);
        this.showSnackBar('Error al cargar las guías disponibles para cancelación');
        this.loading = false;
      }
    });
  }

  toggleExpanded(guiaId: number): void {
    this.expandedGuias[guiaId] = !this.expandedGuias[guiaId];
  }

  openCancelDialog(guia: any): void {
    const dialogRef = this.dialog.open(CancelacionDialogComponent, {
      width: '500px',
      data: {
        guia: guia.guia,
        motivosCancelacion: this.motivosCancelacion
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cancelarGuia(result);
      }
    });
  }

  cancelarGuia(data: {idGuia: number, motivoCategoria: MotivoCancelacion, motivoDetalle: string}): void {
    const cancelacion: CancelacionGuia = {
      idGuia: data.idGuia,
      motivoCategoria: data.motivoCategoria,
      motivoDetalle: data.motivoDetalle,
      idUsuario: this.idUsuario
    };

    this.loading = true;
    this.cancelacionService.cancelarGuia(cancelacion).subscribe({
      next: (response) => {
        this.showSnackBar(response.message);
        this.loadGuias(); // Recargar la lista después de cancelar
      },
      error: (error) => {
        console.error('Error al cancelar guía:', error);
        this.showSnackBar('Error al procesar la solicitud de cancelación');
        this.loading = false;
      }
    });
  }

  showSnackBar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }

  getEstadoClase(estado: string): string {
    switch (estado) {
      case 'CREADA': return 'estado-creada';
      case 'ASIGNADA': return 'estado-asignada';
      default: return '';
    }
  }
}
