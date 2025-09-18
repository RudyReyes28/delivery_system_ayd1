import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Importaciones de Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';

// Importación de Servicios
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './iniciar-sesion.html',
  styleUrls: ['./iniciar-sesion.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ]
})
export class IniciarSesion implements OnInit {
  loginForm: FormGroup;
  tokenForm: FormGroup;
  hidePassword = true;
  isLoading = false;
  mensajeError = '';
  mostrarDialogToken = false;
  isVerificandoToken = false;
  isReenviandoCodigo = false;
  usuarioActual = '';

  constructor(
    private formBuilder: FormBuilder, 
    private router: Router, 
    private authService: AuthService, 
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.createLoginForm();
    this.tokenForm = this.createTokenForm();
  }

  ngOnInit(): void {
    this.mensajeError = '';
  }

  // Especificaciones de los formularios
  private createLoginForm(): FormGroup {
    return this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  private createTokenForm(): FormGroup {
    return this.formBuilder.group({
      token: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.mensajeError = '';

      const credencial = {
        nombreUsuario: this.loginForm.value.username,
        contrasenia: this.loginForm.value.password,
      };

      this.authService.iniciarSesion(credencial).subscribe({
        next: (response) => {
          if (!response.autenticacion.autenticacion) {
            this.guardarInformacionUsuario(response,true);
            this.router.navigate(['/login']);
          } else {
            this.guardarInformacionUsuario(response,false);
            this.mostrarDialogToken = true;
          }
        },
        error: (error: any) => {
          this.manejoDeErrores(error);
        }
      });
      this.isLoading = false;
    } else {
      this.marcarToqueDelFormulario();
      this.mostrarMensaje("Por favor, completa todos los campos correctamente", "warning-snackbar");
    }
  }

  verificarToken(): void {
    if (this.tokenForm.valid) {
      this.isVerificandoToken = true;

      const token = sessionStorage.getItem('token');

      const verificacion = {
        token: token || "",
        codigoVerificacion: parseInt(this.tokenForm.value.token, 10)
      }

      this.authService.verificacionDosPasos(verificacion).subscribe({
        next: (response) => {
          this.isVerificandoToken = false;
          this.cerrarDialogToken();
          this.guardarInformacionUsuario(response,false);
        },
        error: (error) => {
          this.isVerificandoToken = false;
          this.mostrarMensaje("Código incorrecto o expirado", "error-snackbar")
          this.tokenForm.get('token')?.setErrors({ 'incorrect': true });
        }
      });
    }
  }

  cerrarDialogToken(): void {
    this.mostrarDialogToken = false;
    this.tokenForm.reset();
    this.isVerificandoToken = false;
  }

  private guardarInformacionUsuario(response: any, guardar: boolean): void {
    this.mostrarMensaje("¡Login exitoso! Bienvenido", "success-snackbar" );
    sessionStorage.setItem('idUsuario', response.credenciales.idUsuario.toString());  
    
    if (guardar) {
      sessionStorage.setItem('rol', response.credenciales.rol?.toString() ?? ""); 
      sessionStorage.setItem('nombreRol', response.credenciales.nombreRol || "");
      sessionStorage.setItem('nombreUsuario', response.credenciales.nombreUsuario || "");
    } else {
      sessionStorage.setItem('token', response.credenciales.token || "");
    } 
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  private manejoDeErrores(error: any): void {
    let mensaje = '';
    
    if (error.status === 401) {
      mensaje = 'Credenciales incorrectas. Verifica tu usuario y contraseña.';
    } else if (error.status === 404) {
      mensaje = 'Usuario no encontrado.';
    } else if (error.status === 0) {
      mensaje = 'No se pudo conectar con el servidor. Verifica tu conexión.';
    } else {
      mensaje = 'Ha ocurrido un error inesperado. Inténtalo de nuevo.';
    }
    this.mostrarMensaje(mensaje, "error-snackbar");
  }

  private marcarToqueDelFormulario(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get token() {
    return this.tokenForm.get('token');
  }

  clearError(): void {
    this.mensajeError = '';
  }
}