import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ReporteEntregasComponent } from './reporte-entregas/reporte-entregas.component';
import { ReporteComisionesComponent } from './reporte-comisiones/reporte-comisiones.component';
import { ReporteDescuentosComponent } from './reporte-descuentos/reporte-descuentos.component';
import { ReporteCancelacionesComponent } from './reporte-cancelaciones/reporte-cancelaciones.component';
import { PdfExportService } from '../../../services/pdf-export/pdf-export.service';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    ReporteEntregasComponent,
    ReporteComisionesComponent,
    ReporteDescuentosComponent,
    ReporteCancelacionesComponent
  ],
  template: `
    <div class="reportes-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>
            <mat-icon>assessment</mat-icon>
            Panel de Reportes
          </mat-card-title>
          <div class="spacer"></div>
          <button mat-raised-button color="primary" (click)="exportarReporteActual()">
            <mat-icon>picture_as_pdf</mat-icon> Exportar a PDF
          </button>
        </mat-card-header>

        <mat-card-content>
          <mat-tab-group animationDuration="300ms" #tabGroup (selectedTabChange)="onTabChange($event)">
            <mat-tab label="Entregas">
              <div id="reporte-entregas">
                <app-reporte-entregas></app-reporte-entregas>
              </div>
            </mat-tab>
            <mat-tab label="Comisiones">
              <div id="reporte-comisiones">
                <app-reporte-comisiones></app-reporte-comisiones>
              </div>
            </mat-tab>
            <mat-tab label="Descuentos por Nivel">
              <div id="reporte-descuentos">
                <app-reporte-descuentos></app-reporte-descuentos>
              </div>
            </mat-tab>
            <mat-tab label="Cancelaciones">
              <div id="reporte-cancelaciones">
                <app-reporte-cancelaciones></app-reporte-cancelaciones>
              </div>
            </mat-tab>
          </mat-tab-group>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .reportes-container {
      padding: 20px;
    }
    
    mat-card-title {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    
    .spacer {
      flex: 1 1 auto;
    }
    
    ::ng-deep .mat-tab-body-content {
      padding: 20px 0 !important;
    }
  `]
})
export class ReportesComponent {
  selectedTabIndex = 0;
  private reporteTitles = ['Reporte de Entregas', 'Reporte de Comisiones', 'Reporte de Descuentos', 'Reporte de Cancelaciones'];
  private reporteIds = ['reporte-entregas', 'reporte-comisiones', 'reporte-descuentos', 'reporte-cancelaciones'];

  constructor(private pdfExportService: PdfExportService) {}

  onTabChange(event: any): void {
    this.selectedTabIndex = event.index;
  }

  exportarReporteActual(): void {
    const elementId = this.reporteIds[this.selectedTabIndex];
    const title = this.reporteTitles[this.selectedTabIndex];
    const fileName = `${title.toLowerCase().replace(/\s+/g, '-')}-${new Date().toISOString().slice(0, 10)}`;
    
    this.pdfExportService.exportToPDF(elementId, fileName, title);
  }
}
