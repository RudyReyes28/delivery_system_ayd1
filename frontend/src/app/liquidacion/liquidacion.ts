import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmacionDialogComponent } from './confirmacion-dialog.component';
import { ApiResponse } from '../../models/admin-liquidacion.model'; // Asegúrate de que este tipo exista
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminLiquidacionService } from '../../services/admin-liquidacion.service';
import { PeriodoLiquidacion, RepartidorResponse, Repartidor,NuevoPeriodoRequest,ProcesarLiquidacionRequest } from '../../models/admin-liquidacion.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle } from '@angular/material/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatChip, MatChipsModule } from '@angular/material/chips';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatProgressSpinnerModule, MatSpinner } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-liquidacion',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatIcon,
    MatCardContent,
    MatChip,
    MatFormField,
    MatLabel,
    MatSpinner,
    MatCardModule,
    MatIconModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],
  templateUrl: './liquidacion.html',
  styleUrls: ['./liquidacion.scss']
})
export class Liquidacion implements OnInit {
  periodoActivo: PeriodoLiquidacion | null = null;
  repartidores: RepartidorResponse[] = [];
  repartidorSeleccionado: Repartidor | null = null; // Una sola declaración
  cargando = false;
  
  // Variables para el modal
  mostrarDetalleRepartidor: boolean = false;
  
  // Columnas para la tabla
  columnasTabla: string[] = [
    'nombreCompleto', 
    'email', 
    'telefono', 
    'zonaAsignada', 
    'estadoLiquidacion',
    'acciones'
  ];

  // Formulario para nuevo periodo
  nuevoPeriodo: NuevoPeriodoRequest = {
    descripcion: '',
    fechaInicio: '',
    fechaFin: ''
  };

  // Definir las constantes para los estados
  readonly ESTADO_ABIERTO = 'ABIERTO';
  readonly ESTADO_CERRADO = 'CERRADO';
  readonly ESTADO_LIQUIDADO = 'LIQUIDADO';

  constructor(
    private liquidacionService: AdminLiquidacionService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    
    // Cargar periodo activo
    this.liquidacionService.obtenerPeriodoActivo().subscribe({
      next: (periodo) => {
        this.periodoActivo = periodo;
      },
      error: (error) => {
        console.error('Error al obtener periodo activo:', error);
        this.mostrarMensaje("Error al obtener el periodo activo", "error-snackbar");
      }
    });

    // Cargar repartidores
    this.liquidacionService.obtenerRepartidoresLiquidacion().subscribe({
      next: (repartidores) => {
        this.repartidores = repartidores;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al obtener repartidores:', error);
        this.mostrarMensaje("Error al obtener los repartidores", "error-snackbar");
        this.cargando = false;
      }
    });
  }

  verDetalleRepartidor(repartidor: Repartidor): void {
    this.repartidorSeleccionado = repartidor;
    this.mostrarDetalleRepartidor = true;
  }

  cerrarDetalleRepartidor(): void {
    this.mostrarDetalleRepartidor = false;
    this.repartidorSeleccionado = null;
  }

  crearNuevoPeriodo(): void {
    if (!this.validarFormularioPeriodo()) {
      return;
    }

    this.cargando = true;
    this.liquidacionService.crearNuevoPeriodo(this.nuevoPeriodo).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message, "success-snackbar");
        this.limpiarFormularioPeriodo();
        this.cargarDatos();
      },
      error: (error) => {
        console.error('Error al crear periodo:', error);
        this.mostrarMensaje("Error al crear el nuevo periodo", "error-snackbar");
        this.cargando = false;
      }
    });
  }

  procesarLiquidacionRepartidor(idRepartidor: number): void {
    if (!this.periodoActivo) {
      this.mostrarMensaje("No hay periodo activo disponible", "info-snackbar");
      return;
    }

    const request: ProcesarLiquidacionRequest = {
      idRepartidor: idRepartidor,
      idPeriodoLiquidacion: this.periodoActivo.idPeriodo
    };

    this.cargando = true;
    this.liquidacionService.procesarLiquidacion(request).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message, "success-snackbar");
        this.cargarDatos();
      },
      error: (error) => {
        console.error('Error al procesar liquidación:', error);
        this.mostrarMensaje("Error al procesar la liquidación", "error-snackbar");
        this.cargando = false;
      }
    });
  }

  cerrarPeriodoActivo(): void {
    if (!this.periodoActivo) {
      this.showSnackbar('No hay un periodo activo para cerrar');
      return;
    }

    // Abrir diálogo de confirmación
    const dialogRef = this.dialog.open(ConfirmacionDialogComponent, {
      width: '400px',
      data: {
        titulo: 'Cerrar Periodo de Liquidación',
        mensaje: `¿Está seguro que desea cerrar el periodo "${this.periodoActivo.descripcion}"? Esta acción no se puede deshacer.`,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.periodoActivo) { // Agregamos verificación adicional para evitar null
        this.cargando = true;
        this.liquidacionService.cerrarPeriodo(this.periodoActivo.idPeriodo).subscribe({
          next: (response: ApiResponse) => {
            this.showSnackbar(response.message || 'Periodo cerrado correctamente');
            this.cargarDatos(); // Recargar datos
          },
          error: (error: any) => { // Añadimos tipo explícito
            console.error('Error al cerrar periodo:', error);
            this.showSnackbar('Error al cerrar el periodo');
            this.cargando = false;
          }
        });
      }
    });
  }

  tieneLiquidacionActiva(repartidor: Repartidor): boolean {
    return repartidor.liquidacionRepartidor.idLiquidacion !== null;
  }

  obtenerEstadoLiquidacion(repartidor: Repartidor): string {
    if (this.tieneLiquidacionActiva(repartidor)) {
      return repartidor.liquidacionRepartidor.estado || 'PENDIENTE';
    }
    return 'SIN LIQUIDACIÓN';
  }

  private validarFormularioPeriodo(): boolean {
    if (!this.nuevoPeriodo.descripcion || 
        !this.nuevoPeriodo.fechaInicio || 
        !this.nuevoPeriodo.fechaFin) {
      this.mostrarMensaje("Todos los campos son obligatorios", "info-snackbar");
      return false;
    }

    if (new Date(this.nuevoPeriodo.fechaInicio) >= new Date(this.nuevoPeriodo.fechaFin)) {
      this.mostrarMensaje("La fecha de inicio debe ser anterior a la fecha de fin", "info-snackbar");
      return false;
    }

    return true;
  }

  private limpiarFormularioPeriodo(): void {
    this.nuevoPeriodo = {
      descripcion: '',
      fechaInicio: '',
      fechaFin: ''
    };
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

  private showSnackbar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }

  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    return new Date(fecha).toLocaleDateString('es-GT');
  }

  formatearMoneda(valor: number | null): string {
    if (valor === null || valor === undefined) return 'Q 0.00';
    return `Q ${valor.toFixed(2)}`;
  }
}