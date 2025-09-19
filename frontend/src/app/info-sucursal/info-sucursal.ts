import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// Importaciones de Angular Material
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';

// Importando Servicios y Modelos
import { SucursalService } from '../../services/sucursal.service';
import { SucursalItem, EmpresaItem, UsuarioSucursal, SucursalFormData, CambiarEstadoSucursalRequest } from '../../models/sucursal.model';

@Component({
  selector: 'app-info-sucursal',
  imports: [
    CommonModule,
    MatCardModule,
    MatTabsModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    ReactiveFormsModule
  ],
  templateUrl: './info-sucursal.html',
  styleUrl: './info-sucursal.scss'
})
export class InfoSucursal implements OnInit {
  sucursales: SucursalItem[] = [];
  empresas: EmpresaItem[] = [];
  usuariosSucursal: UsuarioSucursal[] = [];
  
  loadingSucursales = false;
  guardandoSucursal = false;
  selectedTabIndex = 0;
  
  sucursalForm: FormGroup;
  
  private cambioProgmatico = false;

  constructor(
    private sucursalService: SucursalService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) {
    this.sucursalForm = this.crearFormulario();
  }

  ngOnInit(): void {
    this.cargarEmpresas();
    this.cargarUsuariosSucursal();
    this.cargarSucursales();
  }

  private crearFormulario(): FormGroup {
    return this.formBuilder.group({
      // Información de la sucursal
      idEmpresa: ['', [Validators.required]],
      codigoSucursal: ['', [Validators.required]],
      nombreSucursal: ['', [Validators.required]],
      horarioApertura: ['', [Validators.required]],
      horarioCierre: ['', [Validators.required]],
      diasOperacion: ['', [Validators.required]],
      estado: ['ACTIVA'],
      
      // Información de dirección
      tipoDireccion: ['', [Validators.required]],
      municipio: ['', [Validators.required]],
      departamento: ['', [Validators.required]],
      pais: ['', [Validators.required]],
      codigoPostal: ['', [Validators.required]],
      referencias: ['', [Validators.required]],
      activa: [true],
      
      // Información del personal
      idUsuario: ['', [Validators.required]],
      cargo: ['', [Validators.required]],
      esEncargado: [true],
      fechaFin: [''],
      activo: [true]
    });
  }

  private resetearFormularioCompleto(): void {
    this.sucursalForm.reset();
    this.sucursalForm.markAsUntouched();
    this.sucursalForm.markAsPristine();
    this.sucursalForm.patchValue({ 
      estado: 'ACTIVA',
      activa: true,
      esEncargado: true,
      activo: true 
    });
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 0) {
      this.crearNuevaSucursal();
    }
  }

  cargarEmpresas(): void {
    this.sucursalService.listarEmpresas().subscribe({
      next: (data) => {
        this.empresas = data;
      },
      error: (error) => {
        console.error('Error al cargar empresas:', error);
        this.mostrarMensaje('Error al cargar las empresas', "error-snackbar");
      }
    });
  }

  cargarUsuariosSucursal(): void {
    this.sucursalService.obtenerUsuariosSucursal().subscribe({
      next: (data) => {
        this.usuariosSucursal = data;
      },
      error: (error) => {
        console.error('Error al cargar usuarios de sucursal:', error);
        this.mostrarMensaje('Error al cargar los usuarios de sucursal', "error-snackbar");
      }
    });
  }

  cargarSucursales(): void {
    this.loadingSucursales = true;
    
    this.sucursalService.listarSucursales().subscribe({
      next: (data) => {
        this.sucursales = data.map(item => ({
          ...item,
          expanded: false,
          actualizandoEstado: false
        }));
        this.loadingSucursales = false;
      },
      error: (error) => {
        console.error('Error al cargar sucursales:', error);
        this.mostrarMensaje('Error al cargar las sucursales', "error-snackbar");
        this.loadingSucursales = false;
      }
    });
  }

  crearNuevaSucursal(): void {
    this.resetearFormularioCompleto();
    this.cambioProgmatico = true;
    this.selectedTabIndex = 0;
  }

  guardarSucursal(): void {
    if (this.sucursalForm.invalid) {
      this.mostrarMensaje('Por favor complete todos los campos requeridos', "error-snackbar");
      return;
    }

    this.guardandoSucursal = true;
    const formData: SucursalFormData = this.sucursalForm.value;
    
    if (!formData.fechaFin) {
      formData.fechaFin = null as any;
    }

    this.sucursalService.crearSucursal(formData).subscribe({
      next: (response) => {
        this.mostrarMensaje('Sucursal creada exitosamente', "success-snackbar");
        this.cargarSucursales();
        this.cancelarFormulario();
      },
      error: (error) => {
        console.error('Error al crear sucursal:', error);
        this.mostrarMensaje(error.error?.message || 'Error al crear la sucursal', "error-snackbar");
        this.guardandoSucursal = false;
      }
    });
  }

  cancelarFormulario(): void {
    this.resetearFormularioCompleto();
    this.guardandoSucursal = false;
    this.cambioProgmatico = true;
    this.selectedTabIndex = 1;
  }

  toggleExpanded(id: number): void {
    const sucursal = this.sucursales.find(s => s.branch.idSucursal === id);
    if (sucursal) {
      sucursal.expanded = !sucursal.expanded;
    }
  }

  onEstadoChange(sucursalItem: SucursalItem, nuevoEstado: string): void {
    if (sucursalItem.branch.estado === nuevoEstado) {
      return;
    }

    const confirmacion = confirm(
      `¿Está seguro de que desea cambiar el estado de "${sucursalItem.branch.nombreSucursal}" a ${nuevoEstado}?`
    );

    if (!confirmacion) {
      return;
    }

    this.cambiarEstadoSucursal(sucursalItem, nuevoEstado);
  }

  private cambiarEstadoSucursal(sucursalItem: SucursalItem, nuevoEstado: string): void {
    sucursalItem.actualizandoEstado = true;

    const request: CambiarEstadoSucursalRequest = {
      idSucursal: sucursalItem.branch.idSucursal,
      estado: nuevoEstado
    };

    this.sucursalService.cambiarEstadoSucursal(request)
      .subscribe({
        next: (response) => {
          sucursalItem.branch.estado = nuevoEstado;
          sucursalItem.actualizandoEstado = false;
          
          this.mostrarMensaje(`Estado de la sucursal "${sucursalItem.branch.nombreSucursal}" actualizado a: ${nuevoEstado}`, "success-snackbar");
        },
        error: (error) => {
          console.error('Error al cambiar estado de la sucursal:', error);
          sucursalItem.actualizandoEstado = false;
          
          const mensajeError = error.error?.message || 'Error al cambiar el estado de la sucursal';
          this.mostrarMensaje(mensajeError, "error-snackbar");
        }
      });
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
  
  refrescarDatos(): void {
    this.cargarEmpresas();
    this.cargarUsuariosSucursal();
    this.cargarSucursales();
  }
}