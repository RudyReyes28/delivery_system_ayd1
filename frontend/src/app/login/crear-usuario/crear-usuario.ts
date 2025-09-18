import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

// Importaciones de Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { getTipoDireccionOptions, TipoDireccion } from '../../../models/enums';

// Importando Servicios
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-crear-usuario',
  standalone: true,
  templateUrl: './crear-usuario.html',
  styleUrl: './crear-usuario.scss',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule
  ]
})
export class CrearUsuario implements OnInit {
  usuarioForm: FormGroup;
  hidePassword = true;
  
  // Opciones para el select
  tipoDireccionOptions = getTipoDireccionOptions();

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.usuarioForm = this.crearUsuarioForm();
  }

  crearUsuarioForm() {
    return this.formBuilder.group({
      tipoDireccion: ['', [Validators.required]],
      municipio: ['', [Validators.required]],
      departamento: ['', [Validators.required]],
      pais: ['Guatemala', [Validators.required]], // Valor por defecto
      codigoPostal: ['', [Validators.required]],
      referencias: ['', [Validators.required]],
      nombre: ['', [Validators.required]],
      apellido: ['', [Validators.required]],
      fechaNacimiento: ['', [Validators.required]],
      dpi: ['', [Validators.required, Validators.minLength(13), Validators.maxLength(13)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9+\-\s\(\)]+$/)]],
      nombreUsuario: ['', [Validators.required, Validators.minLength(3)]],
      contraseniaHash: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  enviarFormulario() {
    if (this.usuarioForm.valid) {
      const formValue = this.prepararDatos(this.usuarioForm);

      const usuarioData = {
        ...formValue,
        tipoDireccion: formValue.tipoDireccion as TipoDireccion
      };

      this.authService.registrarUsuario(usuarioData).subscribe({
        next: (response) => {
          this.mostrarMensaje(response.message, "success-snackbar");
          this.limpiarFormulario();
        },
        error: (error: any) => {
          this.mostrarMensaje(error, "error-snackbar");
        }
      });
    } else {
      this.mostrarMensaje("Por favor complete todos los campos requeridos", "error-snackbar");
    }
  }

  limpiarFormulario() {
    this.usuarioForm.reset();

    Object.keys(this.usuarioForm.controls).forEach(key => {
      const control = this.usuarioForm.get(key);
      control?.markAsPristine();
      control?.markAsUntouched();
      control?.updateValueAndValidity();
    });
  }

  private prepararDatos(form: FormGroup): any {
    const valoresLimpios: any = {};

    Object.keys(form.controls).forEach(key => {
      const valor = form.get(key)?.value;

      if (valor instanceof Date) {
        valoresLimpios[key] = this.formatearFecha(valor);
      } else if (typeof valor === 'string' || typeof valor === 'number') {
        valoresLimpios[key] = valor?.toString().trim() || '';
      } else {
        valoresLimpios[key] = valor;
      }
    });
    return valoresLimpios;
  }


  private formatearFecha(fecha: any): string {
    if (!fecha) return '';
    
    if (fecha instanceof Date) {
      return fecha.toISOString().split('T')[0];
    }
    
    if (typeof fecha === 'string') {
      const dateObj = new Date(fecha);
      if (!isNaN(dateObj.getTime())) {
        return dateObj.toISOString().split('T')[0];
      }
    }
    
    return fecha.toString();
  }

  cancelar() {

  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  ngOnInit(): void {}
}
