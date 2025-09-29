import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';

import { ReportesService } from '../../../../services/admin-reportes/reportes.service';
import { PdfExportService } from '../../../../services/pdf-export/pdf-export.service';
import { ReporteComisiones, Periodo, RepartidorComisiones, DetalleComision } from '../../../../models/admin-reportes/reporte-comisiones.model';

@Component({
  selector: 'app-reporte-comisiones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatExpansionModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatChipsModule,
    MatBadgeModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDividerModule
  ],
  templateUrl: './reporte-comisiones.component.html',
  styleUrls: ['./reporte-comisiones.component.css']
})
export class ReporteComisionesComponent implements OnInit {
  comisiones: ReporteComisiones[] = [];
  cargando = false;
  error = '';
  
  // Control de expansión
  expandedPeriodo: { [key: number]: boolean } = {};
  expandedRepartidor: { [key: number]: boolean } = {};

  constructor(
    private reportesService: ReportesService,
    private snackBar: MatSnackBar,
    private pdfExportService: PdfExportService
  ) { }

  ngOnInit(): void {
    this.cargarReporte();
  }

  cargarReporte(): void {
    this.cargando = true;
    this.error = '';
    
    this.reportesService.getReporteComisiones().subscribe({
      next: (data) => {
        this.comisiones = data;
        
        // Inicializar estados de expansión
        this.comisiones.forEach(comision => {
          this.expandedPeriodo[comision.periodo.idPeriodo] = false;
          comision.periodo.repartidores.forEach(repartidor => {
            this.expandedRepartidor[repartidor.idLiquidacion] = false;
          });
        });
        
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el reporte de comisiones';
        console.error('Error al cargar el reporte de comisiones:', err);
        this.showSnackbar(this.error);
        this.cargando = false;
      }
    });
  }

  togglePeriodo(idPeriodo: number): void {
    this.expandedPeriodo[idPeriodo] = !this.expandedPeriodo[idPeriodo];
  }

  toggleRepartidor(idLiquidacion: number): void {
    this.expandedRepartidor[idLiquidacion] = !this.expandedRepartidor[idLiquidacion];
  }

  isPeriodoExpanded(idPeriodo: number): boolean {
    return this.expandedPeriodo[idPeriodo] === true;
  }

  isRepartidorExpanded(idLiquidacion: number): boolean {
    return this.expandedRepartidor[idLiquidacion] === true;
  }

  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'ABIERTO':
        return 'estado-abierto';
      case 'CERRADO':
        return 'estado-cerrado';
      default:
        return '';
    }
  }

  calcularTotalComisionesPeriodo(periodo: Periodo): number {
    return periodo.repartidores.reduce((total, repartidor) => total + repartidor.totalNeto, 0);
  }

  calcularTotalRepartidoresPeriodo(periodo: Periodo): number {
    return periodo.repartidores.length;
  }

  formatFecha(fecha: string): string {
    if (!fecha) return 'N/A';
    return new Date(fecha).toLocaleDateString('es-GT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatFechaHora(fecha: string): string {
    if (!fecha) return 'N/A';
    return new Date(fecha).toLocaleDateString('es-GT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatMoneda(valor: number): string {
    return new Intl.NumberFormat('es-GT', { 
      style: 'currency', 
      currency: 'GTQ', 
      minimumFractionDigits: 2 
    }).format(valor);
  }

  formatPorcentaje(valor: number): string {
    return new Intl.NumberFormat('es-GT', { 
      style: 'percent', 
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(valor / 100);
  }

  exportarPDF(): void {
    // Expandir todos los periodos y repartidores para el PDF
    this.comisiones.forEach(comision => {
      this.expandedPeriodo[comision.periodo.idPeriodo] = true;
      comision.periodo.repartidores.forEach(repartidor => {
        this.expandedRepartidor[repartidor.idLiquidacion] = true;
      });
    });

    // Dar tiempo a Angular para actualizar la vista
    setTimeout(() => {
      this.pdfExportService.exportToPDF(
        'comisiones-pdf-export',
        `reporte-comisiones-${new Date().toISOString().slice(0, 10)}`,
        'Reporte de Comisiones por Repartidor'
      );

      // Restaurar estado original
      setTimeout(() => {
        this.comisiones.forEach(comision => {
          this.expandedPeriodo[comision.periodo.idPeriodo] = false;
          comision.periodo.repartidores.forEach(repartidor => {
            this.expandedRepartidor[repartidor.idLiquidacion] = false;
          });
        });
      }, 1000);
    }, 500);
  }

  private showSnackbar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }
}
