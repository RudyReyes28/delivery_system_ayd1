import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatChip } from '@angular/material/chips';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';

// Importando Servicios y Modelos
import { EmpresaService } from '../../../services/general-sucursal.service';
import { Empresa } from '../../../models/general-sucursal.model';

@Component({
  selector: 'app-info-usuario-sucursal',
  templateUrl: './general-sucursal.html',
  styleUrls: ['./general-sucursal.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MatSpinner,
    MatCard,
    MatCardContent,
    MatIcon,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    MatChip
  ]
})
export class GeneralSucursal implements OnInit, OnDestroy {
  empresa: Empresa | null = null;
  empresaFidelizacion: Empresa | null = null;
  loading = true;
  error = '';
  idUsuario = Number(sessionStorage.getItem('idUsuario'));

  private destroy$ = new Subject<void>();

  constructor(private empresaService: EmpresaService) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  cargarDatos(): void {
    this.loading = true;
    this.error = '';
    
    this.empresaService.obtenerInformacionCompleta(this.idUsuario)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.empresa = response.empresa;
          this.empresaFidelizacion = response.empresaFidelizacion;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Error al cargar la información de la empresa';
          this.loading = false;
          console.error('Error:', error);
        }
      });
  }

  formatearDiasOperacion(dias: string): string {
    return this.empresaService.formatearDiasOperacion(dias);
  }

  obtenerColorEstado(estado: string): string {
    return this.empresaService.obtenerColorEstado(estado);
  }

  formatearMoneda(cantidad: number): string {
    return this.empresaService.formatearMoneda(cantidad);
  }

  formatearFecha(fecha: string): string {
    return this.empresaService.formatearFecha(fecha);
  }

  obtenerNombreMes(mes: number): string {
    return this.empresaService.obtenerNombreMes(mes);
  }

  reintentar(): void {
    this.cargarDatos();
  }
}