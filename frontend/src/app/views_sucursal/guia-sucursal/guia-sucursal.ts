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
import { GuiaService } from '../../../services/guia-sucursal.service';
import { GuiaResponse, TipoServicioResponse, ClienteRegistradoResponse, NuevaGuiaClienteNuevoRequest, NuevaGuiaClienteExistenteRequest} from '../../../models/guia-sucursal.model';

interface GuiaItem extends GuiaResponse {
  expanded?: boolean;
}

@Component({
  selector: 'app-guia',
  standalone: true,
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
  templateUrl: './guia-sucursal.html',
  styleUrl: './guia-sucursal.scss'
})
export class GuiaSucursal implements OnInit {
  guias: GuiaItem[] = [];
  tiposServicio: TipoServicioResponse[] = [];
  clientes: ClienteRegistradoResponse[] = [];
  
  loadingGuias = false;
  guardandoGuia = false;
  selectedTabIndex = 0;
  tipoClienteSeleccionado: 'nuevo' | 'existente' | null = null;
  
  guiaClienteNuevoForm: FormGroup;
  guiaClienteExistenteForm: FormGroup;
  
  tipoServicioSeleccionado: TipoServicioResponse | null = null;
  tipoServicioSeleccionadoExistente: TipoServicioResponse | null = null;
  
  private cambioProgmatico = false;
  private readonly ID_USUARIO_ACTUAL = Number(sessionStorage.getItem('idUsuario'));

  constructor(private guiaSucursalService: GuiaService, private snackBar: MatSnackBar, private formBuilder: FormBuilder) {
    this.guiaClienteNuevoForm = this.crearFormularioClienteNuevo();
    this.guiaClienteExistenteForm = this.crearFormularioClienteExistente();
  }

  ngOnInit(): void {
    this.cargarTiposServicio();
    this.cargarGuias();
  }

  private crearFormularioClienteNuevo(): FormGroup {
    return this.formBuilder.group({
      nombreCompleto: ['', [Validators.required]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      email: ['', [Validators.required, Validators.email]],
      horarioPreferidoInicio: ['', [Validators.required]],
      horarioPreferidoFin: ['', [Validators.required]],
      instruccionesEntrega: [''],
      aceptaEntregasVecinos: [false],
      requiereIdentificacion: [false],
      personaRecibe: ['', [Validators.required]],
      municipio: ['', [Validators.required]],
      departamento: ['', [Validators.required]],
      pais: ['', [Validators.required]],
      codigoPostal: ['', [Validators.required]],
      referencias: ['', [Validators.required]],
      aliasDireccion: ['', [Validators.required]],
      instruccionesEspecificas: [''],
      puntoReferencia: ['', [Validators.required]],
      idTipoServicio: ['', [Validators.required]],
      descripcionContenido: ['', [Validators.required]],
      valorDeclarado: ['', [Validators.required, Validators.min(0)]],
      pesoKG: ['', [Validators.required, Validators.min(0.01)]],
      dimensiones: ['', [Validators.required, Validators.pattern(/^\d+(\.\d+)?x\d+(\.\d+)?x\d+(\.\d+)?$/)]],
      esFragil: [false],
      observaciones: [''],
      fechaProgramadaRecoleccion: ['', [Validators.required]],
      prioridad: ['NORMAL', [Validators.required]],
      subtotal: ['', [Validators.required, Validators.min(0)]],
      descuentos: [0, [Validators.min(0)]],
      recargos: [0, [Validators.min(0)]],
      formaPago: ['CONTADO', [Validators.required]]
    });
  }

  private crearFormularioClienteExistente(): FormGroup {
    return this.formBuilder.group({
      idCliente: ['', [Validators.required]],
      idTipoServicio: ['', [Validators.required]],
      descripcionContenido: ['', [Validators.required]],
      valorDeclarado: ['', [Validators.required, Validators.min(0)]],
      pesoKG: ['', [Validators.required, Validators.min(0.01)]],
      dimensiones: ['', [Validators.required, Validators.pattern(/^\d+(\.\d+)?x\d+(\.\d+)?x\d+(\.\d+)?$/)]],
      esFragil: [false],
      observaciones: [''],
      fechaProgramadaRecoleccion: ['', [Validators.required]],
      prioridad: ['NORMAL', [Validators.required]],
      subtotal: ['', [Validators.required, Validators.min(0)]],
      descuentos: [0, [Validators.min(0)]],
      recargos: [0, [Validators.min(0)]],
      formaPago: ['CONTADO', [Validators.required]]
    });
  }

  private resetearFormularioClienteNuevo(): void {
    this.guiaClienteNuevoForm.reset();
    this.guiaClienteNuevoForm.markAsUntouched();
    this.guiaClienteNuevoForm.markAsPristine();
    this.guiaClienteNuevoForm.patchValue({ 
      aceptaEntregasVecinos: false,
      requiereIdentificacion: false,
      esFragil: false,
      prioridad: 'NORMAL',
      descuentos: 0,
      recargos: 0,
      formaPago: 'CONTADO'
    });
    this.tipoServicioSeleccionado = null;
  }

  private resetearFormularioClienteExistente(): void {
    this.guiaClienteExistenteForm.reset();
    this.guiaClienteExistenteForm.markAsUntouched();
    this.guiaClienteExistenteForm.markAsPristine();
    this.guiaClienteExistenteForm.patchValue({ 
      esFragil: false,
      prioridad: 'NORMAL',
      descuentos: 0,
      recargos: 0,
      formaPago: 'CONTADO'
    });
    this.tipoServicioSeleccionadoExistente = null;
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 0) {
      this.crearNuevaGuia();
    }
  }

  cargarTiposServicio(): void {
    this.guiaSucursalService.obtenerTiposServicios().subscribe({
      next: (data) => {
        this.tiposServicio = data;
      },
      error: (error) => {
        this.mostrarMensaje('Error al cargar los tipos de servicio', "error-snackbar");
      }
    });
  }

  cargarClientes(): void {
    if (this.clientes.length === 0) {
      this.guiaSucursalService.obtenerClientes().subscribe({
        next: (data) => {
          this.clientes = data;
        },
        error: (error) => {
          this.mostrarMensaje('Error al cargar los clientes', "error-snackbar");
        }
      });
    }
  }

  cargarGuias(): void {
    this.loadingGuias = true;
    
    this.guiaSucursalService.obtenerGuiaUsuario(this.ID_USUARIO_ACTUAL).subscribe({
      next: (data) => {
        this.guias = data.map(item => ({
          ...item,
          expanded: false
        }));
        this.loadingGuias = false;
      },
      error: (error) => {
        this.mostrarMensaje('Error al cargar las guías', "error-snackbar");
        this.loadingGuias = false;
      }
    });
  }

  crearNuevaGuia(): void {
    this.resetearFormularioClienteNuevo();
    this.resetearFormularioClienteExistente();
    this.tipoClienteSeleccionado = null;
    this.cambioProgmatico = true;
    this.selectedTabIndex = 0;
  }

  seleccionarTipoCliente(tipo: 'nuevo' | 'existente'): void {
    this.tipoClienteSeleccionado = tipo;
    
    if (tipo === 'existente') {
      this.cargarClientes();
    }

    this.resetearFormularioClienteNuevo();
    this.resetearFormularioClienteExistente();
  }

  onTipoServicioChange(idTipoServicio: number): void {
    this.tipoServicioSeleccionado = this.tiposServicio.find(ts => ts.idTipoServicio === idTipoServicio) || null;
    
    if (this.tipoServicioSeleccionado) {
      this.guiaClienteNuevoForm.patchValue({
        subtotal: this.tipoServicioSeleccionado.precioBase
      });
    }
  }

  onTipoServicioChangeExistente(idTipoServicio: number): void {
    this.tipoServicioSeleccionadoExistente = this.tiposServicio.find(ts => ts.idTipoServicio === idTipoServicio) || null;
    
    if (this.tipoServicioSeleccionadoExistente) {
      // Actualizar el subtotal con el precio base del servicio
      this.guiaClienteExistenteForm.patchValue({
        subtotal: this.tipoServicioSeleccionadoExistente.precioBase
      });
    }
  }

  onClienteTabChange(event: any): void {
    if (event.index === 1) {
      this.cargarClientes();
    }
  }

  guardarGuiaClienteNuevo(): void {
    if (this.guiaClienteNuevoForm.invalid) {
      this.mostrarMensaje("Por favor complete todos los campos requeridos", "info-snackbar");
      return;
    }

    this.guardandoGuia = true;
    const formData = this.guiaClienteNuevoForm.value;

    const request: NuevaGuiaClienteNuevoRequest = {
      ...formData,
      idUsuario: this.ID_USUARIO_ACTUAL,
    };

    this.guiaSucursalService.crearGuiaClienteNuevo(request).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message, "success-snackbar");
        this.cargarGuias();
        this.cancelarFormulario();
        this.guardandoGuia = false;
      },
      error: (error) => {
        console.error('Error al crear guía para cliente nuevo:', error);
        this.mostrarMensaje(error.error?.message || 'Error al crear la guía', "error-snackbar");
        this.guardandoGuia = false;
      }
    });
  }

  guardarGuiaClienteExistente(): void {
    if (this.guiaClienteExistenteForm.invalid) {
      this.mostrarMensaje("Por favor complete todos los campos requeridos", "info-snackbar");
      return;
    }

    this.guardandoGuia = true;
    const formData = this.guiaClienteExistenteForm.value;
    
    // Agregar el ID del usuario actual
    const request: NuevaGuiaClienteExistenteRequest = {
      ...formData,
      idUsuario: this.ID_USUARIO_ACTUAL
    };

    this.guiaSucursalService.crearGuiaClienteExistente(request).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message, "success-snackbar");
        this.cargarGuias();
        this.cancelarFormulario();
      },
      error: (error) => {
        console.error('Error al crear guía para cliente existente:', error);
        this.mostrarMensaje(error.error?.message || "Error al crear la guía", "error-snackbar");
        this.guardandoGuia = false;
      }
    });
  }

  cancelarFormulario(): void {
    this.resetearFormularioClienteNuevo();
    this.resetearFormularioClienteExistente();
    this.guardandoGuia = false;
    this.cambioProgmatico = true;
    this.selectedTabIndex = 1;
  }

  toggleExpanded(id: number): void {
    const guia = this.guias.find(g => g.guia.idGuia === id);
    if (guia) {
      guia.expanded = !guia.expanded;
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
  
  refrescarDatos(): void {
    this.cargarTiposServicio();
    this.cargarClientes();
    this.cargarGuias();
  }

  calcularTotal(form: FormGroup): number {
    const subtotal = form.get('subtotal')?.value || 0;
    const descuentos = form.get('descuentos')?.value || 0;
    const recargos = form.get('recargos')?.value || 0;
    
    return subtotal - descuentos + recargos;
  }

  private validarFechaFutura(control: any): any {
    const fecha = new Date(control.value);
    const ahora = new Date();
    
    if (fecha <= ahora) {
      return { fechaPasada: true };
    }
    return null;
  }

  obtenerColorPrioridad(prioridad: string): string {
    switch (prioridad) {
      case 'NORMAL': return 'primary';
      case 'ALTA': return 'accent';
      case 'URGENTE': return 'warn';
      default: return 'primary';
    }
  }

  obtenerIconoEstado(estado: string): string {
    switch (estado.toLowerCase()) {
      case 'pendiente': return 'hourglass_empty';
      case 'en_transito': return 'local_shipping';
      case 'entregado': return 'check_circle';
      case 'cancelado': return 'cancel';
      case 'devuelto': return 'keyboard_return';
      default: return 'help';
    }
  }
}