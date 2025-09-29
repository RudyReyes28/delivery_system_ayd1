import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';

import { CoordinadorService } from '../../../services/coordinador-service/coordinador.service';
import { SolicitudCancelacion, EstadoCancelacion } from '../../../models/coordinador/cancelacion.model';
import { ConfirmacionDialogComponent } from './confirmacion-dialog.component';

@Component({
  selector: 'app-solicitudes-cancelacion',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatTabsModule
  ],
  templateUrl: './solicitudes-cancelacion.component.html',
  styleUrls: ['./solicitudes-cancelacion.component.css']
})
export class SolicitudesCancelacionComponent implements OnInit {
  solicitudes: SolicitudCancelacion[] = [];
  solicitudesExpandidas: { [key: number]: boolean } = {};
  cargando: boolean = false;
  idUsuario: number = 0;

  // Para comparación en la plantilla
  readonly ESTADO_SOLICITADA = EstadoCancelacion.SOLICITADA;

  constructor(
    private coordinadorService: CoordinadorService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // Obtener el ID del usuario del almacenamiento de sesión
    this.idUsuario = Number(sessionStorage.getItem('idUsuario')) || 0;
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando = true;
    this.coordinadorService.getSolicitudesCancelacion().subscribe({
      next: (data) => {
        this.solicitudes = data;
        // Inicializar el estado expandido de las solicitudes
        this.solicitudes.forEach(solicitud => {
          this.solicitudesExpandidas[solicitud.idCancelacion] = false;
        });
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar solicitudes de cancelación:', error);
        this.mostrarMensaje('Error al cargar las solicitudes de cancelación');
        this.cargando = false;
      }
    });
  }

  toggleExpandir(idCancelacion: number): void {
    this.solicitudesExpandidas[idCancelacion] = !this.solicitudesExpandidas[idCancelacion];
  }

  abrirDialogoAceptar(solicitud: SolicitudCancelacion): void {
    const dialogRef = this.dialog.open(ConfirmacionDialogComponent, {
      width: '500px',
      data: {
        titulo: 'Aceptar Cancelación',
        mensaje: `¿Estás seguro de que deseas aceptar la cancelación de la guía ${solicitud.guia.numeroGuia}?`,
        tipo: 'aceptar',
        idCancelacion: solicitud.idCancelacion
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.aceptarCancelacion(solicitud.idCancelacion);
      }
    });
  }

  abrirDialogoRechazar(solicitud: SolicitudCancelacion): void {
    const dialogRef = this.dialog.open(ConfirmacionDialogComponent, {
      width: '500px',
      data: {
        titulo: 'Rechazar Cancelación',
        mensaje: `¿Estás seguro de que deseas rechazar la cancelación de la guía ${solicitud.guia.numeroGuia}?`,
        tipo: 'rechazar',
        idCancelacion: solicitud.idCancelacion
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.rechazarCancelacion(solicitud.idCancelacion);
      }
    });
  }

  aceptarCancelacion(idCancelacion: number): void {
    this.cargando = true;
    this.coordinadorService.aceptarSolicitudCancelacion({
      idCancelacion,
      idUsuario: this.idUsuario
    }).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message);
        this.cargarSolicitudes(); // Recargar la lista
      },
      error: (error) => {
        console.error('Error al aceptar cancelación:', error);
        this.mostrarMensaje('Error al procesar la solicitud de cancelación');
        this.cargando = false;
      }
    });
  }

  rechazarCancelacion(idCancelacion: number): void {
    this.cargando = true;
    this.coordinadorService.rechazarSolicitudCancelacion(idCancelacion).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message);
        this.cargarSolicitudes(); // Recargar la lista
      },
      error: (error) => {
        console.error('Error al rechazar cancelación:', error);
        this.mostrarMensaje('Error al procesar el rechazo de la cancelación');
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

  obtenerClaseEstadoCancelacion(estado: string): string {
    switch (estado) {
      case 'SOLICITADA': return 'estado-solicitada';
      case 'AUTORIZADA': return 'estado-autorizada';
      case 'PROCESADA': return 'estado-procesada';
      case 'RECHAZADA': return 'estado-rechazada';
      default: return '';
    }
  }

  obtenerClasePrioridad(prioridad: string): string {
    switch (prioridad) {
      case 'ALTA': return 'prioridad-alta';
      case 'NORMAL': return 'prioridad-normal';
      case 'URGENTE': return 'prioridad-urgente';
      default: return '';
    }
  }

  obtenerClaseEstado(estado: string): string {
    switch (estado) {
      case 'CREADA': return 'estado-creada';
      case 'ASIGNADA': return 'estado-asignada';
      default: return '';
    }
  }

  formatearMotivo(motivo: string): string {
    return motivo
      .split('_')
      .map(word => word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
  }
}
