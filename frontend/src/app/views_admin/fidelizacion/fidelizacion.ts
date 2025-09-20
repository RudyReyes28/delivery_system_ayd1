import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';

// Importando Servicios y Modelos
import { FidelizacionService } from '../../../services/fidelizacion.service';
import { EmpresaData, Empresa, AccionFidelizacion, AsignarCambiarRequest } from '../../../models/fidelizacion.model';

@Component({
  selector: 'app-admin-fidelizacion',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatExpansionModule,
    MatSelectModule,
    MatFormFieldModule,
    MatSnackBarModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    FormsModule
  ],
  templateUrl: './fidelizacion.html',
  styleUrl: './fidelizacion.scss'
})
export class Fidelizacion implements OnInit, OnDestroy {
  empresas: EmpresaData[] = [];
  loading = false;
  procesando: number | null = null;
  mostrarSelectorNivel = false;
  mostrarDetalleEmpresa = false;
  empresaSeleccionada: Empresa | null = null;
  empresaDetalleSeleccionada: Empresa | null = null;
  nivelSeleccionado = '';
  accionActual: AccionFidelizacion = 'asignar';
  procesandoAccion = false;

  private destroy$ = new Subject<void>();

  constructor(
    private fidelizacionService: FidelizacionService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.cargarEmpresas();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  cargarEmpresas(): void {
    this.loading = true;
    
    this.fidelizacionService.obtenerEmpresas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.empresas = data;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al cargar empresas:', error);
          this.mostrarMensaje('Error al cargar las empresas');
          this.loading = false;
        }
      });
  }

  verMasInfo(empresa: Empresa): void {
    this.empresaDetalleSeleccionada = empresa;
    this.mostrarDetalleEmpresa = true;
  }

  asignarFidelizacion(empresa: Empresa): void {
    if (!this.fidelizacionService.puedeAsignar(empresa)) {
      this.mostrarMensaje('Esta empresa ya tiene un programa de fidelización asignado');
      return;
    }

    this.empresaSeleccionada = empresa;
    this.accionActual = 'asignar';
    this.nivelSeleccionado = '';
    this.mostrarSelectorNivel = true;
  }

  cambiarFidelizacion(empresa: Empresa): void {
    if (!this.fidelizacionService.puedeCambiar(empresa)) {
      this.mostrarMensaje('Esta empresa no tiene un programa de fidelización para cambiar');
      return;
    }

    this.empresaSeleccionada = empresa;
    this.accionActual = 'cambiar';
    this.nivelSeleccionado = '';
    this.mostrarSelectorNivel = true;
  }

  confirmarAccion(): void {
    if (!this.empresaSeleccionada || !this.nivelSeleccionado) {
      this.mostrarMensaje('Por favor selecciona un nivel de fidelización');
      return;
    }

    if (!this.fidelizacionService.validarDatosAccion(
      this.empresaSeleccionada.idEmpresa, 
      this.nivelSeleccionado
    )) {
      this.mostrarMensaje('Datos inválidos para la operación');
      return;
    }

    this.procesandoAccion = true;
    this.procesando = this.empresaSeleccionada.idEmpresa;

    const request: AsignarCambiarRequest = {
      idEmpresa: this.empresaSeleccionada.idEmpresa,
      codigoNivel: this.nivelSeleccionado
    };

    this.fidelizacionService.ejecutarAccionFidelizacion(this.accionActual, request)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          const mensaje = this.fidelizacionService.generarMensajeResultado(response, this.accionActual);
          this.mostrarMensaje(mensaje, 8000);
          this.cargarEmpresas(); // Recargar lista
          this.cancelarSeleccion();
          this.procesandoAccion = false;
          this.procesando = null;
        },
        error: (error) => {
          console.error('Error en la operación:', error);
          const accionTexto = this.accionActual === 'asignar' ? 'asignar' : 'cambiar';
          this.mostrarMensaje(`Error al ${accionTexto} fidelización`);
          this.procesandoAccion = false;
          this.procesando = null;
        }
      });
  }

  cancelarSeleccion(): void {
    this.mostrarSelectorNivel = false;
    this.empresaSeleccionada = null;
    this.nivelSeleccionado = '';
    this.procesandoAccion = false;
  }

  cerrarDetalleEmpresa(): void {
    this.mostrarDetalleEmpresa = false;
    this.empresaDetalleSeleccionada = null;
  }

  /**
   * Verifica si una empresa puede asignar programa usando el servicio
   */
  puedeAsignar(empresa: Empresa): boolean {
    return this.fidelizacionService.puedeAsignar(empresa);
  }

  /**
   * Verifica si una empresa puede cambiar nivel usando el servicio
   */
  puedeCambiar(empresa: Empresa): boolean {
    return this.fidelizacionService.puedeCambiar(empresa);
  }

  /**
   * Obtiene el estado del programa usando el servicio
   */
  obtenerEstadoPrograma(empresa: Empresa): string {
    return this.fidelizacionService.obtenerEstadoPrograma(empresa);
  }

  /**
   * Formatea moneda usando el servicio
   */
  formatearMoneda(cantidad: number): string {
    return this.fidelizacionService.formatearMoneda(cantidad);
  }

  /**
   * Formatea fecha usando el servicio
   */
  formatearFecha(fecha: string): string {
    return this.fidelizacionService.formatearFecha(fecha);
  }

  /**
   * Obtiene nombre del mes usando el servicio
   */
  obtenerNombreMes(mes: number): string {
    return this.fidelizacionService.obtenerNombreMes(mes);
  }

  /**
   * Reintenta la carga de datos
   */
  reintentar(): void {
    this.cargarEmpresas();
  }

  private mostrarMensaje(mensaje: string, duration = 4000): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['custom-snackbar']
    });
  }
}