import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { RepartidorService } from '../../../services/repartidor-service/repartidor.service';
import { CancelacionClienteRequest } from '../../../models/repartidor/cancelacion-cliente.model';

@Component({
  selector: 'app-cancelacion-cliente-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  template: `
    <h2 mat-dialog-title>Registrar Cancelación de Cliente</h2>
    <div mat-dialog-content>
      <p>Registre el motivo por el cual el cliente ha cancelado la entrega del pedido.</p>
      
      <form [formGroup]="cancelacionForm">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Descripción de la Cancelación</mat-label>
          <textarea matInput formControlName="descripcion" rows="5" 
            placeholder="Describa el motivo por el cual el cliente canceló el pedido..."></textarea>
          <mat-hint align="end">{{ cancelacionForm.get('descripcion')?.value?.length || 0 }}/500</mat-hint>
          <mat-error *ngIf="cancelacionForm.get('descripcion')?.hasError('required')">
            La descripción es requerida
          </mat-error>
          <mat-error *ngIf="cancelacionForm.get('descripcion')?.hasError('minlength')">
            La descripción debe tener al menos 10 caracteres
          </mat-error>
          <mat-error *ngIf="cancelacionForm.get('descripcion')?.hasError('maxlength')">
            La descripción no debe exceder los 500 caracteres
          </mat-error>
        </mat-form-field>
      </form>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button color="warn" [disabled]="submitting" (click)="onNoClick()">
        Cancelar
      </button>
      <button mat-raised-button color="primary" 
              [disabled]="cancelacionForm.invalid || submitting" 
              (click)="onSubmit()">
        <mat-icon *ngIf="!submitting">save</mat-icon>
        <mat-spinner *ngIf="submitting" diameter="20" class="spinner-button"></mat-spinner>
        {{ submitting ? 'Enviando...' : 'Registrar Cancelación' }}
      </button>
    </div>
  `,
  styles: [`
    .full-width {
      width: 100%;
      min-width: 300px;
    }
    
    .spinner-button {
      display: inline-block;
      vertical-align: middle;
      margin-right: 8px;
    }
    
    mat-dialog-content {
      padding-top: 10px;
    }
    
    mat-dialog-actions {
      padding: 16px 0;
    }
  `]
})
export class CancelacionClienteDialogComponent implements OnInit {
  cancelacionForm: FormGroup;
  submitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CancelacionClienteDialogComponent>,
    private repartidorService: RepartidorService,
    @Inject(MAT_DIALOG_DATA) public data: { idGuia: number, idUsuario: number }
  ) {
    this.cancelacionForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void { }

  onNoClick(): void {
    this.dialogRef.close(false);
  }

  onSubmit(): void {
    if (this.cancelacionForm.invalid) {
      return;
    }

    this.submitting = true;

    const request: CancelacionClienteRequest = {
      idGuia: this.data.idGuia,
      idUsuario: this.data.idUsuario,
      descripcion: this.cancelacionForm.get('descripcion')?.value
    };

    this.repartidorService.registrarCancelacionCliente(request).subscribe({
      next: (response) => {
        this.submitting = false;
        this.dialogRef.close({ success: true, message: response.message });
      },
      error: (error) => {
        console.error('Error al registrar cancelación:', error);
        this.submitting = false;
        this.dialogRef.close({ 
          success: false, 
          message: error.error?.message || 'Error al registrar la cancelación' 
        });
      }
    });
  }
}
