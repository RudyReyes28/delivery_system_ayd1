import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ActivatedRoute, Router } from '@angular/router';

import { RepartidorService } from '../../../services/repartidor-service/repartidor.service';
import { CancelacionClienteRequest } from '../../../models/repartidor/cancelacion-cliente.model';

@Component({
  selector: 'app-cancelacion-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDividerModule
  ],
  templateUrl: './cancelacion-cliente.component.html',
  styleUrls: ['./cancelacion-cliente.component.css']
})
export class CancelacionClienteComponent implements OnInit {
  cancelacionForm!: FormGroup;
  submitting: boolean = false;
  idGuia: number | null = null;
  idUsuario: number | null = null;

  constructor(
    private fb: FormBuilder,
    private repartidorService: RepartidorService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // Obtener el ID de guía de los parámetros de la ruta si está disponible
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.idGuia = +params['id'];
      }
    });

    // Obtener el ID del usuario de la sesión
    const usuarioId = sessionStorage.getItem('idUsuario');
    if (usuarioId) {
      this.idUsuario = +usuarioId;
    }

    this.initForm();
  }

  private initForm(): void {
    this.cancelacionForm = this.fb.group({
      idGuia: [this.idGuia, [Validators.required, Validators.min(1)]],
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]]
    });
  }

  onSubmit(): void {
    if (this.cancelacionForm.invalid || !this.idUsuario) {
      this.cancelacionForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    const request: CancelacionClienteRequest = {
      idGuia: this.cancelacionForm.get('idGuia')?.value,
      idUsuario: this.idUsuario,
      descripcion: this.cancelacionForm.get('descripcion')?.value
    };

    this.repartidorService.registrarCancelacionCliente(request).subscribe({
      next: (response) => {
        this.showSnackBar(response.message || 'Cancelación registrada correctamente');
        this.submitting = false;
        // Redirigir después de un breve retraso
        setTimeout(() => {
          this.router.navigate(['/seguimiento-pedidos']);
        }, 2000);
      },
      error: (error) => {
        console.error('Error al registrar cancelación:', error);
        this.showSnackBar(error.error?.message || 'Error al registrar la cancelación');
        this.submitting = false;
      }
    });
  }

  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
    });
  }
}
