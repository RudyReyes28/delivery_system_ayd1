import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';

// Importación de Servicios
import { AuthService } from '../../../services/auth.service';


@Component({
  selector: 'app-recuperar-cuenta',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    MatFormField,
    MatLabel,
    MatError,
    MatSpinner,
    MatIcon,
    MatCardActions,
    RouterModule,
  ],
  templateUrl: './recuperar-cuenta.html',
  styleUrl: './recuperar-cuenta.scss'
})
export class RecuperarCuenta implements OnInit{
  currentStep: number = 1;
  isLoading: boolean = false;
  hidePassword: boolean = true;
  hideConfirmPassword: boolean = true;
  
  // Forms
  emailForm!: FormGroup;
  verificationForm!: FormGroup;
  passwordForm!: FormGroup;
  
  // Data storage
  userEmail: string = '';
  userName: string = '';
  recoveryToken: string = '';
  userId: number = 0;
  
  private readonly API_BASE_URL = 'http://localhost:8081/login';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private router: Router,
    private authService: AuthService, 
  ) {

  }

  ngOnInit(): void {
    this.initializeForms();
  }

  private initializeForms(): void {
    // Formulario de email
    this.emailForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]]
    });

    // Formulario de verificación
    this.verificationForm = this.fb.group({
      codigoVerificacion: ['', [
        Validators.required, 
        Validators.pattern(/^\d{6}$/)
      ]]
    });

    // Formulario de contraseña con validador personalizado
    this.passwordForm = this.fb.group({
      nuevaContrasenia: ['', [
        Validators.required, 
        Validators.minLength(8)
      ]],
      confirmarContrasenia: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  // Validador personalizado para confirmar contraseñas
  private passwordMatchValidator(control: AbstractControl): {[key: string]: any} | null {
    const password = control.get('nuevaContrasenia');
    const confirmPassword = control.get('confirmarContrasenia');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { 'passwordMismatch': true };
    }
    return null;
  }

  // Paso 1: Solicitar recuperación de contraseña
  requestRecovery(): void {
    if (this.emailForm.valid) {
      this.isLoading = true;
      
      const requestData = {
        correo: this.emailForm.get('correo')?.value
      };

      this.authService.recuperarContrasenia(requestData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.userEmail = requestData.correo;
          this.userId = response.user;
          this.recoveryToken = response.token;
          
          sessionStorage.setItem('token', this.recoveryToken);
          
          this.showSuccessMessage(response.mensaje);
          this.currentStep = 2;
        },
        error: (error) => {
          // Manejar Error
        }
      });
    }
  }

  // Paso 2: Verificar código de seguridad
  verifyCode(): void {
    if (this.verificationForm.valid) {
      this.isLoading = true;
      
      const requestData = {
        token: this.recoveryToken,
        codigoVerificacion: this.verificationForm.get('codigoVerificacion')?.value
      };

      this.authService.solicitarCambioContrasenia(requestData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.userId = response.idUsuario;
          this.userName = response.nombreUsuario;
          this.recoveryToken = response.token;
          
          sessionStorage.setItem('idUsuario', this.userId.toString());
          sessionStorage.setItem('token', this.recoveryToken);
          
          this.showSuccessMessage(response.mensaje);
          this.currentStep = 3;
        },
        error: (error) => {
          // Manejar Errores
        }
      });
    }
  }

  // Paso 3: Actualizar contraseña
  resetPassword(): void {
    if (this.passwordForm.valid) {
      this.isLoading = true;
      
      const storedUserId = sessionStorage.getItem('idUsuario');
      const idUsuario = this.userId || (storedUserId ? parseInt(storedUserId) : 0);
      
      const requestData = {
        idUsuario: idUsuario,
        token: this.recoveryToken,
        nuevaContrasenia: this.passwordForm.get('nuevaContrasenia')?.value
      };

      this.authService.cambiarContrasenia(requestData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.showSuccessMessage(response.message);
          this.currentStep = 4;
        },
        error: (error) => {
          // Manejar Errores
        }
      });
    }
  }

  goBack(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  goToLogin(): void {
    // sessionStorage.removeItem('idUsuario');
    // sessionStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  private showSuccessMessage(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 4000,
      panelClass: ['success-snackbar']
    });
  }

  private showErrorMessage(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }

  resetProcess(): void {
    this.currentStep = 1;
    this.emailForm.reset();
    this.verificationForm.reset();
    this.passwordForm.reset();
    this.userEmail = '';
    this.userName = '';
    this.recoveryToken = '';
    this.userId = 0;
    this.isLoading = false;
  }
}
