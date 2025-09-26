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
          this.mostrarMensaje("Error al cargar las empresas", "error-snackbar");
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
      this.mostrarMensaje("Esta empresa ya tiene un programa de fidelización asignado", "info-snackbar");
      return;
    }

    this.empresaSeleccionada = empresa;
    this.accionActual = 'asignar';
    this.nivelSeleccionado = '';
    this.mostrarSelectorNivel = true;
  }

  cambiarFidelizacion(empresa: Empresa): void {
    if (!this.fidelizacionService.puedeCambiar(empresa)) {
      this.mostrarMensaje("Esta empresa no tiene un programa de fidelización para cambiar", "info-snackbar");
      return;
    }

    this.empresaSeleccionada = empresa;
    this.accionActual = 'cambiar';
    this.nivelSeleccionado = '';
    this.mostrarSelectorNivel = true;
  }

  confirmarAccion(): void {
    if (!this.empresaSeleccionada || !this.nivelSeleccionado) {
      this.mostrarMensaje("Por favor selecciona un nivel de fidelización", "info-snackbar");
      return;
    }

    if (!this.fidelizacionService.validarDatosAccion(
      this.empresaSeleccionada.idEmpresa, 
      this.nivelSeleccionado
    )) {
      this.mostrarMensaje("Datos inválidos para la operación", "warning-snackbar");
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
          this.mostrarMensaje(mensaje, "success-snackbar");
          this.cargarEmpresas();
          this.cancelarSeleccion();
          this.procesandoAccion = false;
          this.procesando = null;
        },
        error: (error) => {
          const accionTexto = this.accionActual === 'asignar' ? 'asignar' : 'cambiar';
          this.mostrarMensaje("Error al realizar la Fidelización", "error-snackbar");
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

  puedeAsignar(empresa: Empresa): boolean {
    return this.fidelizacionService.puedeAsignar(empresa);
  }

  puedeCambiar(empresa: Empresa): boolean {
    return this.fidelizacionService.puedeCambiar(empresa);
  }

  obtenerEstadoPrograma(empresa: Empresa): string {
    return this.fidelizacionService.obtenerEstadoPrograma(empresa);
  }

  formatearMoneda(cantidad: number): string {
    return this.fidelizacionService.formatearMoneda(cantidad);
  }

  formatearFecha(fecha: string): string {
    return this.fidelizacionService.formatearFecha(fecha);
  }

  obtenerNombreMes(mes: number): string {
    return this.fidelizacionService.obtenerNombreMes(mes);
  }

  reintentar(): void {
    this.cargarEmpresas();
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
}