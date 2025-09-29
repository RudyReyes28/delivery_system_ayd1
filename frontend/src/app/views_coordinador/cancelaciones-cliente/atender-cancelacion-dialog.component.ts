import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { IncidenciaCliente } from '../../../models/coordinador/cancelacion-cliente.model';

@Component({
  selector: 'app-atender-cancelacion-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <h2 mat-dialog-title>Atender Cancelación</h2>
    <div mat-dialog-content>
      <p class="info-text">
        Está atendiendo la cancelación para la guía <strong>{{ data.incidencia.guia.numeroGuia }}</strong>
      </p>
      
      <div class="incidencia-info">
        <p><span class="label">Motivo:</span> {{ data.incidencia.descripcion }}</p>
        <p><span class="label">Tipo:</span> {{ data.incidencia.tipoIncidencia }}</p>
        <p><span class="label">Severidad:</span> {{ data.incidencia.severidad }}</p>
        <p><span class="label">Reportado:</span> {{ data.incidencia.fechaReporte | date:'dd/MM/yyyy HH:mm' }}</p>
      </div>
      
      <form [formGroup]="form">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Solución Aplicada</mat-label>
          <textarea matInput formControlName="solucion" rows="4" placeholder="Describa la solución a aplicar..."></textarea>
          <mat-error *ngIf="form.get('solucion')?.hasError('required')">
            Este campo es requerido
          </mat-error>
          <mat-error *ngIf="form.get('solucion')?.hasError('minlength')">
            La solución debe tener al menos 10 caracteres
          </mat-error>
        </mat-form-field>
      </form>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button (click)="onNoClick()">Cancelar</button>
      <button mat-raised-button color="primary" [disabled]="form.invalid" (click)="onYesClick()">
        Atender y Devolver a Sucursal
      </button>
    </div>
  `,
  styles: [`
    .info-text {
      margin-bottom: 16px;
      font-size: 16px;
    }
    
    .incidencia-info {
      background-color: #f5f5f5;
      border-left: 4px solid #2196f3;
      padding: 12px;
      margin-bottom: 20px;
      border-radius: 4px;
    }
    
    .incidencia-info p {
      margin: 6px 0;
    }
    
    .label {
      font-weight: 500;
      margin-right: 8px;
    }
    
    .full-width {
      width: 100%;
    }
  `]
})
export class AtenderCancelacionDialogComponent {
  form: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<AtenderCancelacionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { incidencia: IncidenciaCliente },
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      solucion: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
    if (this.form.valid) {
      this.dialogRef.close({
        solucion: this.form.get('solucion')?.value
      });
    }
  }
}
