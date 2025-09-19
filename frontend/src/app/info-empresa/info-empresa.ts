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
import { EmpresaService } from '../../services/empresa.service';
import { UsuarioSucursal, Empresa, EmpresaItem, CambiarEstadoRequest,EmpresaFormData } from '../../models/empresa.model';

@Component({
  selector: 'app-info-empresa',
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
  templateUrl: './info-empresa.html',
  styleUrl: './info-empresa.scss'
})
export class InfoEmpresa implements OnInit {
  usuariosSucursal: UsuarioSucursal[] = [];
  empresas: EmpresaItem[] = [];
  loadingEmpresas = false;
  guardandoEmpresa = false;
  selectedTabIndex = 0;
  modoEdicion = false;
  empresaForm: FormGroup;
  empresaEditando: Empresa | null = null;
  
  private cambioProgmatico = false;

  constructor(
    private empresaService: EmpresaService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) {
    this.empresaForm = this.crearFormulario();
  }

  ngOnInit(): void {
    this.cargarUsuariosSucursal();
    this.cargarEmpresas();
  }

  private crearFormulario(): FormGroup {
    return this.formBuilder.group({
      tipoEmpresa: ['', [Validators.required]],
      nombreComercial: ['', [Validators.required]],
      razonSocial: ['', [Validators.required]],
      nit: ['', [Validators.required]],
      registroMercantil: ['', [Validators.required]],
      fechaConstitucion: ['', [Validators.required]],
      fechaAfiliacion: ['', [Validators.required]],
      fechaVencimientoAfiliacion: ['', [Validators.required]],
      idPersona: ['', [Validators.required]],
      cargo: ['', [Validators.required]],
      fechaInicio: ['', [Validators.required]],
      fechaFin: [''],
      activo: [true]
    });
  }

  private resetearFormularioCompleto(): void {
    this.empresaForm.reset();
    this.empresaForm.markAsUntouched();
    this.empresaForm.markAsPristine();
    this.empresaForm.patchValue({ activo: true });
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 0) {
      this.crearNuevaEmpresa();
    }
  }

  cargarUsuariosSucursal(): void {
    this.empresaService.obtenerUsuariosSucursal().subscribe({
      next: (data) => {
        this.usuariosSucursal = data;
      },
      error: (error) => {
        console.error('Error al cargar usuarios de sucursal:', error);
        this.mostrarMensaje('Error al cargar los usuarios de sucursal', "error-snackbar");
      }
    });
  }

  cargarEmpresas(): void {
    this.loadingEmpresas = true;
    
    this.empresaService.listarEmpresas().subscribe({
      next: (data) => {
        this.empresas = data.map(item => ({
          ...item,
          expanded: false,
          actualizandoEstado: false
        }));
        this.loadingEmpresas = false;
      },
      error: (error) => {
        console.error('Error al cargar empresas:', error);
        this.mostrarMensaje('Error al cargar las empresas', "error-snackbar");
        this.loadingEmpresas = false;
      }
    });
  }

  crearNuevaEmpresa(): void {
    this.modoEdicion = false;
    this.empresaEditando = null;
    this.resetearFormularioCompleto();
    
    this.cambioProgmatico = true;
    this.selectedTabIndex = 0;
  }

  editarEmpresa(empresa: Empresa): void {
    this.modoEdicion = true;
    this.empresaEditando = empresa;
    
    this.empresaForm.patchValue({
      tipoEmpresa: empresa.tipoEmpresa,
      nombreComercial: empresa.nombreComercial,
      razonSocial: empresa.razonSocial,
      nit: empresa.nit,
      registroMercantil: empresa.registroMercantil,
      fechaConstitucion: empresa.fechaConstitucion,
      fechaAfiliacion: empresa.fechaAfiliacion,
      fechaVencimientoAfiliacion: empresa.fechaVencimientoAfiliacion,
      idPersona: empresa.contacto?.idPersona || '',
      cargo: empresa.contacto?.cargo || '',
      fechaInicio: empresa.contacto?.fechaInicio || '',
      fechaFin: empresa.contacto?.fechaFin || '',
      activo: empresa.contacto?.activo || true
    });
    
    this.cambioProgmatico = true;
    this.selectedTabIndex = 0;
  }

  guardarEmpresa(): void {
    if (this.empresaForm.invalid) {
      this.mostrarMensaje('Por favor complete todos los campos requeridos', "error-snackbar");
      return;
    }

    this.guardandoEmpresa = true;
    const formData: EmpresaFormData = this.empresaForm.value;
    
    // Convertir fechaFin a null si está vacía
    if (!formData.fechaFin) {
      formData.fechaFin = null;
    }

    if (this.modoEdicion && this.empresaEditando) {
      // Modo edición
      this.empresaService.editarEmpresa(this.empresaEditando.idEmpresa, formData).subscribe({
        next: (response) => {
          this.mostrarMensaje('Empresa actualizada exitosamente', "success-snackbar");
          this.cargarEmpresas();
          this.cancelarFormulario();
        },
        error: (error) => {
          console.error('Error al actualizar empresa:', error);
          this.mostrarMensaje(error.error?.message || 'Error al actualizar la empresa', "error-snackbar");
          this.guardandoEmpresa = false;
        }
      });
    } else {
      // Modo creación
      this.empresaService.crearEmpresa(formData)
        .subscribe({
          next: (response) => {
            this.mostrarMensaje('Empresa creada exitosamente', "success-snackbar");
            this.cargarEmpresas();
            this.cancelarFormulario();
          },
          error: (error) => {
            console.error('Error al crear empresa:', error);
            this.mostrarMensaje(error.error?.message || 'Error al crear la empresa', "error-snackbar");
            this.guardandoEmpresa = false;
          }
        });
    }
  }

  cancelarFormulario(): void {
    this.resetearFormularioCompleto();
    this.modoEdicion = false;
    this.empresaEditando = null;
    this.guardandoEmpresa = false;
    
    this.cambioProgmatico = true;
    this.selectedTabIndex = 1;
  }

  toggleExpanded(tipo: 'empresa', id: number): void {
    const empresa = this.empresas.find(e => e.empresa.idEmpresa === id);
    if (empresa) {
      empresa.expanded = !empresa.expanded;
    }
  }

  onEstadoChange(empresaItem: EmpresaItem, nuevoEstado: string): void {
    if (empresaItem.empresa.estado === nuevoEstado) {
      return;
    }

    const confirmacion = confirm(
      `¿Está seguro de que desea cambiar el estado de "${empresaItem.empresa.nombreComercial}" a ${nuevoEstado}?`
    );

    if (!confirmacion) {
      return;
    }

    this.cambiarEstadoEmpresa(empresaItem, nuevoEstado);
  }

  private cambiarEstadoEmpresa(empresaItem: EmpresaItem, nuevoEstado: string): void {
    empresaItem.actualizandoEstado = true;

    const request: CambiarEstadoRequest = {
      idEmpresa: empresaItem.empresa.idEmpresa,
      nuevoEstado: nuevoEstado
    };

    this.empresaService.cambiarEstadoEmpresa(request)
      .subscribe({
        next: (response) => {
          empresaItem.empresa.estado = response.estado;
          empresaItem.empresa.tipoEmpresa = response.tipoEmpresa;
          empresaItem.empresa.nombreComercial = response.nombreComercial;
          empresaItem.empresa.razonSocial = response.razonSocial;
          empresaItem.empresa.nit = response.nit;
          empresaItem.empresa.registroMercantil = response.registroMercantil;
          empresaItem.empresa.fechaConstitucion = response.fechaConstitucion;
          empresaItem.empresa.fechaAfiliacion = response.fechaAfiliacion;
          empresaItem.empresa.fechaVencimientoAfiliacion = response.fechaVencimientoAfiliacion;

          empresaItem.actualizandoEstado = false;
          
          this.mostrarMensaje(`Estado de la empresa "${empresaItem.empresa.nombreComercial}" actualizado a: ${response.estado}`, "success-snackbar");

        },
        error: (error) => {
          console.error('Error al cambiar estado de la empresa:', error);
          empresaItem.actualizandoEstado = false;
          
          const mensajeError = error.error?.message || 'Error al cambiar el estado de la empresa';
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
    this.cargarUsuariosSucursal();
    this.cargarEmpresas();
  }
}