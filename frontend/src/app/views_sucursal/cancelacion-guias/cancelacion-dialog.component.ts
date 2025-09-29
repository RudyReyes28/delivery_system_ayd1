import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { MotivoCancelacion } from '../../../models/cancelacion-guia.model';

@Component({
  selector: 'app-cancelacion-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  template: `
    <h2 mat-dialog-title>Cancelar Guía</h2>
    <div mat-dialog-content>
      <p>Está a punto de cancelar la guía <strong>{{ data.guia.numeroGuia }}</strong></p>
      
      <form [formGroup]="cancelForm">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Motivo de Cancelación</mat-label>
          <mat-select formControlName="motivoCategoria" required>
            <mat-option *ngFor="let motivo of data.motivosCancelacion" [value]="motivo">
              {{ formatMotivo(motivo) }}
            </mat-option>
          </mat-select>
          <mat-error *ngIf="cancelForm.get('motivoCategoria')?.hasError('required')">
            Este campo es requerido
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Detalles del Motivo</mat-label>
          <textarea matInput formControlName="motivoDetalle" rows="3" required></textarea>
          <mat-error *ngIf="cancelForm.get('motivoDetalle')?.hasError('required')">
            Este campo es requerido
          </mat-error>
          <mat-error *ngIf="cancelForm.get('motivoDetalle')?.hasError('minlength')">
            El detalle debe tener al menos 10 caracteres
          </mat-error>
        </mat-form-field>
      </form>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button (click)="onNoClick()">Cancelar</button>
      <button mat-raised-button color="warn" [disabled]="cancelForm.invalid" (click)="onYesClick()">
        Confirmar Cancelación
      </button>
    </div>
  `,
  styles: [`
    .full-width {
      width: 100%;
      margin-bottom: 15px;
    }
    mat-dialog-actions {
      margin-top: 10px;
    }
  `]
})
export class CancelacionDialogComponent {
  cancelForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CancelacionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      guia: any,
      motivosCancelacion: MotivoCancelacion[]
    }
  ) {
    this.cancelForm = this.fb.group({
      motivoCategoria: ['', Validators.required],
      motivoDetalle: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
    if (this.cancelForm.valid) {
      this.dialogRef.close({
        idGuia: this.data.guia.idGuia,
        motivoCategoria: this.cancelForm.value.motivoCategoria,
        motivoDetalle: this.cancelForm.value.motivoDetalle
      });
    }
  }

  formatMotivo(motivo: string): string {
    return motivo.replace('_', ' ').split('_')
      .map(word => word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
  }
}
