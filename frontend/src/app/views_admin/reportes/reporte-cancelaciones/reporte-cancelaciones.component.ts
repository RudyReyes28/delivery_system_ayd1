import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ReportesService } from '../../../../services/admin-reportes/reportes.service';
import { ReporteCancelacionesEmpresa, GuiaCancelada } from '../../../../models/admin-reportes/reporte-cancelaciones.model';
import { PdfExportService } from '../../../../services/pdf-export/pdf-export.service';

@Component({
  selector: 'app-reporte-cancelaciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatExpansionModule,
    MatTabsModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatChipsModule,
    MatDividerModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTooltipModule
  ],
  templateUrl: './reporte-cancelaciones.component.html',
  styleUrls: ['./reporte-cancelaciones.component.css']
})
export class ReporteCancelacionesComponent implements OnInit {
  reporteCancelaciones: ReporteCancelacionesEmpresa[] = [];
  filteredCancelaciones: ReporteCancelacionesEmpresa[] = [];
  categoriasUnicas: string[] = [];
  empresasUnicas: string[] = [];
  cargando: boolean = false;
  error: string = '';

  // Filtros
  filtroEmpresa: string = '';
  filtroCategoria: string = '';
  filtroTexto: string = '';
  
  // Expandir/Colapsar
  expandedEmpresas: { [key: number]: boolean } = {};
  
  // Resumen
  totalEmpresas: number = 0;
  totalCancelaciones: number = 0;
  categoriasMasComunes: { categoria: string, cantidad: number }[] = [];

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
    
    this.reportesService.getReporteCancelaciones().subscribe({
      next: (data) => {
        this.reporteCancelaciones = data;
        this.filteredCancelaciones = [...data];
        
        // Inicializar estados expandidos
        this.reporteCancelaciones.forEach(empresa => {
          this.expandedEmpresas[empresa.idEmpresa] = false;
        });
        
        // Extraer categorías únicas
        this.extractUniqueCategories();
        
        // Extraer empresas únicas
        this.extractUniqueCompanies();
        
        // Calcular estadísticas
        this.calcularEstadisticas();
        
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el reporte de cancelaciones: ' + (err.message || 'Error desconocido');
        console.error('Error al cargar el reporte de cancelaciones:', err);
        this.cargando = false;
      }
    });
  }

  toggleExpanded(empresaId: number): void {
    this.expandedEmpresas[empresaId] = !this.expandedEmpresas[empresaId];
  }

  isExpanded(empresaId: number): boolean {
    return this.expandedEmpresas[empresaId] === true;
  }
  
  extractUniqueCategories(): void {
    const categorias = new Set<string>();
    
    this.reporteCancelaciones.forEach(empresa => {
      empresa.guiasCanceladas.forEach(guia => {
        if (guia.categoriaCancelacion) {
          categorias.add(guia.categoriaCancelacion);
        }
      });
    });
    
    this.categoriasUnicas = Array.from(categorias);
  }
  
  extractUniqueCompanies(): void {
    this.empresasUnicas = this.reporteCancelaciones.map(empresa => empresa.nombreEmpresa);
  }
  
  calcularEstadisticas(): void {
    this.totalEmpresas = this.reporteCancelaciones.length;
    this.totalCancelaciones = this.reporteCancelaciones.reduce(
      (total, empresa) => total + empresa.totalCancelaciones, 0
    );
    
    // Calcular categorías más comunes
    const contadorCategorias: { [key: string]: number } = {};
    
    this.reporteCancelaciones.forEach(empresa => {
      empresa.guiasCanceladas.forEach(guia => {
        if (guia.categoriaCancelacion) {
          contadorCategorias[guia.categoriaCancelacion] = (contadorCategorias[guia.categoriaCancelacion] || 0) + 1;
        }
      });
    });
    
    this.categoriasMasComunes = Object.entries(contadorCategorias)
      .map(([categoria, cantidad]) => ({ categoria, cantidad }))
      .sort((a, b) => b.cantidad - a.cantidad)
      .slice(0, 3); // Top 3 categorías
  }
  
  aplicarFiltros(): void {
    this.filteredCancelaciones = this.reporteCancelaciones.filter(empresa => {
      // Filtrar por empresa si hay un filtro seleccionado
      if (this.filtroEmpresa && empresa.nombreEmpresa !== this.filtroEmpresa) {
        return false;
      }
      
      // Filtrar por texto en el nombre de la empresa
      if (this.filtroTexto && !empresa.nombreEmpresa.toLowerCase().includes(this.filtroTexto.toLowerCase())) {
        return false;
      }
      
      // Si hay filtro de categoría, verificar que la empresa tenga guías con esa categoría
      if (this.filtroCategoria) {
        const guiasFiltradas = empresa.guiasCanceladas.filter(
          guia => guia.categoriaCancelacion === this.filtroCategoria
        );
        
        // Si no hay guías que coincidan con la categoría, excluir la empresa
        if (guiasFiltradas.length === 0) {
          return false;
        }
        
        // Crear una copia de la empresa solo con las guías que coinciden con el filtro de categoría
        return true;
      }
      
      return true;
    });
    
    // Si hay filtro de categoría, ajustar las guías mostradas en cada empresa
    if (this.filtroCategoria) {
      this.filteredCancelaciones = this.filteredCancelaciones.map(empresa => {
        const empresaClone = { ...empresa };
        empresaClone.guiasCanceladas = empresa.guiasCanceladas.filter(
          guia => guia.categoriaCancelacion === this.filtroCategoria
        );
        empresaClone.totalCancelaciones = empresaClone.guiasCanceladas.length;
        return empresaClone;
      });
    }
  }
  
  resetFiltros(): void {
    this.filtroEmpresa = '';
    this.filtroCategoria = '';
    this.filtroTexto = '';
    this.filteredCancelaciones = [...this.reporteCancelaciones];
  }

  formatDate(dateString: string): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('es-GT', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getCategoriaClass(categoria: string): string {
    switch (categoria) {
      case 'ERROR_INFORMACION':
        return 'categoria-error';
      case 'CAMBIO_CLIENTE':
        return 'categoria-cambio';
      case 'FALTA_STOCK':
        return 'categoria-falta';
      case 'CLIENTE_RECHAZA':
        return 'categoria-rechaza';
      default:
        return 'categoria-default';
    }
  }

  exportarPDF(): void {
    // Expandir todas las empresas para que aparezcan en el PDF
    this.reporteCancelaciones.forEach(empresa => {
      this.expandedEmpresas[empresa.idEmpresa] = true;
    });

    // Dar tiempo a Angular para actualizar la vista
    setTimeout(() => {
      this.pdfExportService.exportToPDF(
        'cancelaciones-pdf-export', 
        `reporte-cancelaciones-${new Date().toISOString().slice(0, 10)}`,
        'Reporte de Cancelaciones por Empresa'
      );

      // Restaurar el estado de expansión original después de un tiempo
      setTimeout(() => {
        this.reporteCancelaciones.forEach(empresa => {
          this.expandedEmpresas[empresa.idEmpresa] = false;
        });
      }, 1000);
    }, 500);
  }
}
