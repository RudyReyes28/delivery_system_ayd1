import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

import { ReportesService } from '../../../../services/admin-reportes/reportes.service';
import { DescuentoNivel } from '../../../../models/admin-reportes/reporte-descuentos.model';
import { PdfExportService } from '../../../../services/pdf-export/pdf-export.service';

@Component({
  selector: 'app-reporte-descuentos',
  standalone: true,
  imports: [
    CommonModule,
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
    MatChipsModule
  ],
  templateUrl: './reporte-descuentos.component.html',
  styleUrls: ['./reporte-descuentos.component.css']
})
export class ReporteDescuentosComponent implements OnInit {
  descuentos: DescuentoNivel[] = [];
  dataSource = new MatTableDataSource<DescuentoNivel>();
  columnas: string[] = [
    'nombreNivel', 
    'codigoNivel', 
    'porcentajeDescuento', 
    'totalDescuentosAplicados',
    'totalEmpresasBeneficiadas'
  ];
  
  cargando = false;
  error = '';
  
  totalDescuentosAplicados = 0;
  totalEmpresasBeneficiadas = 0;
  
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private reportesService: ReportesService,
    private snackBar: MatSnackBar,
    private pdfExportService: PdfExportService
  ) { }

  ngOnInit(): void {
    this.cargarReporte();
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  cargarReporte(): void {
    this.cargando = true;
    this.error = '';
    
    this.reportesService.getReporteDescuentos().subscribe({
      next: (data) => {
        this.descuentos = data;
        this.dataSource.data = data;
        this.calcularTotales();
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el reporte de descuentos';
        console.error('Error al cargar el reporte de descuentos:', err);
        this.showSnackbar(this.error);
        this.cargando = false;
      }
    });
  }
  
  calcularTotales(): void {
    this.totalDescuentosAplicados = this.descuentos.reduce((total, nivel) => total + nivel.totalDescuentosAplicados, 0);
    this.totalEmpresasBeneficiadas = this.descuentos.reduce((total, nivel) => total + nivel.totalEmpresasBeneficiadas, 0);
  }
  
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getCodigoNivelClass(codigo: string): string {
    switch (codigo) {
      case 'PLATA':
        return 'nivel-plata';
      case 'ORO':
        return 'nivel-oro';
      case 'DIAMANTE':
        return 'nivel-diamante';
      default:
        return '';
    }
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
      minimumFractionDigits: 1 
    }).format(valor / 100);
  }

  exportarPDF(): void {
    this.pdfExportService.exportToPDF(
      'descuentos-pdf-export',
      `reporte-descuentos-${new Date().toISOString().slice(0, 10)}`,
      'Reporte de Descuentos por Nivel de Tarjeta'
    );
  }

  private showSnackbar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }
}
