import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';
import { MatBadgeModule } from '@angular/material/badge';

import { CancelacionClienteService } from '../../../services/coordinador-service/cancelacion-cliente.service';
import { 
  CancelacionClienteResponse,
  AtenderCancelacionRequest
} from '../../../models/coordinador/cancelacion-cliente.model';
import { AtenderCancelacionDialogComponent } from './atender-cancelacion-dialog.component';

@Component({
  selector: 'app-cancelaciones-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatTabsModule,
    MatBadgeModule
  ],
  templateUrl: './cancelaciones-cliente.component.html',
  styleUrls: ['./cancelaciones-cliente.component.css']
})
export class CancelacionesClienteComponent implements OnInit {
  cancelaciones: CancelacionClienteResponse[] = [];
  cancelacionesExpandidas: { [key: number]: boolean } = {};
  cargando: boolean = false;
  idUsuario: number = 0;
  cancelacionesFiltradas: CancelacionClienteResponse[] = [];
  filtroActual: string = 'TODAS';

  // Definir los estados posibles como constantes
  readonly ESTADO_ABIERTA = 'ABIERTA';
  readonly ESTADO_EN_ATENCION = 'EN_ATENCION';
  readonly ESTADO_RESUELTA = 'RESUELTA';
  readonly ESTADO_CERRADA = 'CERRADA';

  constructor(
    private cancelacionClienteService: CancelacionClienteService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // Obtener el ID del usuario de la sesión
    this.idUsuario = Number(sessionStorage.getItem('idUsuario')) || 0;
    this.cargarCancelaciones();
  }

  cargarCancelaciones(): void {
    this.cargando = true;
    this.cancelacionClienteService.getCancelacionesCliente().subscribe({
      next: (data) => {
        this.cancelaciones = data;
        this.aplicarFiltro(this.filtroActual);
        
        // Inicializar el estado expandido de cada cancelación
        this.cancelaciones.forEach(cancelacion => {
          this.cancelacionesExpandidas[cancelacion.incidencia.idIncidencia] = false;
        });
        
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar cancelaciones:', error);
        this.mostrarMensaje('Error al cargar las cancelaciones de cliente');
        this.cargando = false;
      }
    });
  }

  aplicarFiltro(filtro: string): void {
    this.filtroActual = filtro;
    
    if (filtro === 'TODAS') {
      this.cancelacionesFiltradas = [...this.cancelaciones];
    } else {
      this.cancelacionesFiltradas = this.cancelaciones.filter(
        cancelacion => cancelacion.incidencia.estadoIncidencia === filtro
      );
    }
  }

  contarPorEstado(estado: string): number {
    return this.cancelaciones.filter(
      cancelacion => cancelacion.incidencia.estadoIncidencia === estado
    ).length;
  }

  toggleExpandir(idIncidencia: number): void {
    this.cancelacionesExpandidas[idIncidencia] = !this.cancelacionesExpandidas[idIncidencia];
  }

  abrirDialogoAtender(cancelacion: CancelacionClienteResponse): void {
    const dialogRef = this.dialog.open(AtenderCancelacionDialogComponent, {
      width: '500px',
      data: {
        incidencia: cancelacion.incidencia
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.solucion) {
        this.atenderCancelacion(cancelacion.incidencia.idIncidencia, result.solucion);
      }
    });
  }

  atenderCancelacion(idIncidencia: number, solucion: string): void {
    this.cargando = true;
    
    const request: AtenderCancelacionRequest = {
      idIncidencia,
      idUsuario: this.idUsuario,
      solucionAplicada: solucion
    };

    this.cancelacionClienteService.atenderCancelacion(request).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message);
        this.cargarCancelaciones(); // Recargar la lista
      },
      error: (error) => {
        console.error('Error al atender cancelación:', error);
        this.mostrarMensaje('Error al atender la solicitud de cancelación');
        this.cargando = false;
      }
    });
  }

  mostrarMensaje(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }

  obtenerClasePrioridad(prioridad: string): string {
    switch (prioridad) {
      case 'ALTA': return 'prioridad-alta';
      case 'NORMAL': return 'prioridad-normal';
      case 'URGENTE': return 'prioridad-urgente';
      default: return '';
    }
  }

  obtenerClaseSeveridad(severidad: string): string {
    switch (severidad) {
      case 'BAJA': return 'severidad-baja';
      case 'MEDIA': return 'severidad-media';
      case 'ALTA': return 'severidad-alta';
      case 'CRITICA': return 'severidad-critica';
      default: return '';
    }
  }

  obtenerClaseEstado(estado: string): string {
    switch (estado) {
      case this.ESTADO_ABIERTA: return 'estado-abierta';
      case this.ESTADO_EN_ATENCION: return 'estado-en-atencion';
      case this.ESTADO_RESUELTA: return 'estado-resuelta';
      case this.ESTADO_CERRADA: return 'estado-cerrada';
      default: return '';
    }
  }

  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleString();
  }

  esCancelacionAtendible(estado: string | undefined): boolean {
    // Solo es atendible si el estado es explícitamente ABIERTA
    return estado === this.ESTADO_ABIERTA;
  }
}
