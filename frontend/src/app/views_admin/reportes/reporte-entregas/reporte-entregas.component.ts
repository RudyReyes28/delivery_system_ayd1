import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
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
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDividerModule } from '@angular/material/divider';

import { ReportesService } from '../../../../services/admin-reportes/reportes.service';
import { 
  ReporteEntregas,
  EntregaCompletada,
  EntregaCancelada,
  EntregaRechazada 
} from '../../../../models/admin-reportes/reporte-entregas.model';

@Component({
  selector: 'app-reporte-entregas',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    MatCardModule,
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
    MatExpansionModule,
    MatDividerModule
  ],
  templateUrl: './reporte-entregas.component.html',
  styleUrls: ['./reporte-entregas.component.css']
})
export class ReporteEntregasComponent implements OnInit {
  reporte: ReporteEntregas | null = null;
  cargando = false;
  error = '';

  // Propiedades para expansión de detalles
  expandedCompletadas: { [key: number]: boolean } = {};
  expandedCanceladas: { [key: number]: boolean } = {};
  expandedRechazadas: { [key: number]: boolean } = {};

  constructor(
    private reportesService: ReportesService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.cargarReporte();
  }

  cargarReporte(): void {
    this.cargando = true;
    this.error = '';
    
    this.reportesService.getReporteEntregas().subscribe({
      next: (data) => {
        this.reporte = data;
        
        // Inicializar estados de expansión
        if (data.entregasCompletadas) {
          data.entregasCompletadas.forEach(item => {
            this.expandedCompletadas[item.entregasCompletadas.guia.idGuia] = false;
          });
        }
        
        if (data.entregasCanceladas) {
          data.entregasCanceladas.forEach(item => {
            this.expandedCanceladas[item.entregasCanceladas.guia.idGuia] = false;
          });
        }
        
        if (data.entregasRechazadas) {
          data.entregasRechazadas.forEach(item => {
            this.expandedRechazadas[item.entregasRechazadas.guia.idGuia] = false;
          });
        }
        
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el reporte de entregas';
        console.error('Error al cargar el reporte de entregas:', err);
        this.showSnackbar(this.error);
        this.cargando = false;
      }
    });
  }

  toggleExpansion(type: 'completadas' | 'canceladas' | 'rechazadas', id: number): void {
    switch (type) {
      case 'completadas':
        this.expandedCompletadas[id] = !this.expandedCompletadas[id];
        break;
      case 'canceladas':
        this.expandedCanceladas[id] = !this.expandedCanceladas[id];
        break;
      case 'rechazadas':
        this.expandedRechazadas[id] = !this.expandedRechazadas[id];
        break;
    }
  }

  isExpanded(type: 'completadas' | 'canceladas' | 'rechazadas', id: number): boolean {
    switch (type) {
      case 'completadas':
        return this.expandedCompletadas[id] === true;
      case 'canceladas':
        return this.expandedCanceladas[id] === true;
      case 'rechazadas':
        return this.expandedRechazadas[id] === true;
      default:
        return false;
    }
  }

  formatDate(date: string | null): string {
    if (!date) return 'N/A';
    
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric', 
      month: 'long', 
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    };
    
    return new Date(date).toLocaleDateString('es-ES', options);
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('es-GT', { 
      style: 'currency', 
      currency: 'GTQ',
      minimumFractionDigits: 2
    }).format(amount);
  }

  getTotalCompletadas(): number {
    return this.reporte?.entregasCompletadas?.[0]?.totalEntregasCompletadas || 0;
  }

  getTotalCanceladas(): number {
    return this.reporte?.entregasCanceladas?.[0]?.totalEntregasCanceladas || 0;
  }

  getTotalRechazadas(): number {
    return this.reporte?.entregasRechazadas?.[0]?.totalEntregasRechazadas || 0;
  }

  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'ENTREGADA':
        return 'estado-entregada';
      case 'CANCELADA':
        return 'estado-cancelada';
      case 'DEVUELTA':
        return 'estado-devuelta';
      default:
        return '';
    }
  }

  getPrioridadClass(prioridad: string): string {
    switch (prioridad) {
      case 'ALTA':
        return 'prioridad-alta';
      case 'NORMAL':
        return 'prioridad-normal';
      case 'URGENTE':
        return 'prioridad-urgente';
      default:
        return '';
    }
  }

  private showSnackbar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }
}
