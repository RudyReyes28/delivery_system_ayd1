import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { GuiaItem } from '../../../models/repartidor-models/SeguimientoModel';
import { SeguimientoService } from '../../../services/repartidor-service/Seguimiento.service';
import { Guia } from '../../../models/repartidor-models/AsignacionModel';
import { EvidenciaService } from '../../../services/repartidor-service/Evicencia.service';
import { IncidenciaService } from '../../../services/repartidor-service/Incidencia.service';
import {
  EvidenciaModel,
  TIPOS_EVIDENIA as TIPOS_EVIDENCIA,
} from '../../../models/repartidor-models/EvidenciaModel';
import { FilesService } from '../../../services/file-service/Files.service';
import {
  IncidenciaModel,
  SEVERIDADES,
  TIPOS_INCIDENCIAS,
} from '../../../models/repartidor-models/IncidenciaModel';
import { ESTADOS_GUIA } from '../../../models/repartidor-models/EstadosGuia';
import { MatDialog } from '@angular/material/dialog';
import { CancelacionClienteDialogComponent } from '../cancelacion-cliente-dialog/cancelacion-cliente-dialog.component';
import { RepartidorService } from '../../../services/repartidor-service/repartidor.service';

@Component({
  selector: 'app-seguimiento-pedidos',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTabsModule,
    MatListModule,
    MatChipsModule,
    MatDividerModule,
    MatSelectModule,
    MatSlideToggleModule,
  ],
  templateUrl: './seguimiento-pedidos.html',
  styleUrl: './seguimiento-pedidos.scss',
})
export class SeguimientoPedidos implements OnInit {
  tituloEntregada = '';
  selectedTabIndex = 0;
  cambioProgmatico: boolean = false;
  loadingGuias: boolean = false;
  guias: GuiaItem[] = [];

  //filtrar guias
  estados: string[] = ESTADOS_GUIA;
  //formulario evidencia
  formEvidencia: FormGroup = new FormGroup({});
  guardandoEvidencia: boolean = false;
  tiposEvidencia: string[] = TIPOS_EVIDENCIA;

  //cambio de estado
  formCambioEstado: FormGroup = new FormGroup({});
  guardandoCambioEstado: boolean = false;
  guiaSeleccionado!: GuiaItem;

  //formulario incidencia
  formIncidencia: FormGroup = new FormGroup({});
  file!: File;
  tiposIncidencia: string[] = TIPOS_INCIDENCIAS;
  serveridades: string[] = SEVERIDADES;
  guardandoIncidencia: boolean = false;

  tituloIncidencia = '';

  //devoluciones
  devolverGuia: boolean = false;

  private readonly ID_USUARIO_ACTUAL = Number(sessionStorage.getItem('idUsuario'));

  constructor(
    private dialog: MatDialog,
    private repartidorService: RepartidorService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private seguiminetoService: SeguimientoService,
    private evidenciaService: EvidenciaService,
    private incidenciaService: IncidenciaService,
    private filesService: FilesService
  ) {
    this.crearFormularioEvidencia();
    this.crearFormularioIncidencia();
    this.crearFormularioCambioEstado();
  }

  ngOnInit(): void {
    this.setGuias();
  }

  crearFormularioEvidencia(): void {
    this.formEvidencia = this.formBuilder.group({
      motivo: ['', [Validators.required]],
      comentarios: ['', [Validators.required]],
      tipoEvidencia: ['', [Validators.required]],
      receptor: [''],
      parentesco: [''],
      observaciones: [''],
    });
  }

  crearFormularioCambioEstado(): void {
    this.formCambioEstado = this.formBuilder.group({
      motivoEstado: ['', [Validators.required]],
      comentariosEstado: [''],
    });
  }

  crearFormularioIncidencia(): void {
    this.formIncidencia = this.formBuilder.group({
      tipoIncidencia: ['', [Validators.required]],
      severidad: ['', [Validators.required]],
      costoAdicional: [0, [Validators.required, Validators.min(0)]],
      descripcion: ['', [Validators.required]],
      requiereDevolucion: [false],
      ponerEnTransito: [false],
      motivo: [''],
      comentarios: [''],
    });
  }

  cancelarFormularios(): void {
    this.cambioProgmatico = false;
    this.selectedTabIndex = 0;
    this.formCambioEstado.reset();
    this.formCambioEstado.markAsUntouched();
    this.formCambioEstado.markAsPristine();
    this.formIncidencia.reset();
    this.formIncidencia.markAsUntouched();
    this.formIncidencia.markAsPristine();
    this.formEvidencia.reset();
    this.formEvidencia.markAsUntouched();
    this.formEvidencia.markAsPristine();
  }

  setGuias(): void {
    this.loadingGuias = true;
    this.seguiminetoService.getPedidosAsignados(this.ID_USUARIO_ACTUAL).subscribe({
      next: (data) => {
        this.guias = data;
        this.loadingGuias = false;
      },
      error: (err) => {
        this.loadingGuias = false;
        this.showSnackbar('Error al cargar las guías asignadas', 'error-snackbar');
      },
    });
  }

  /**
   *
   public enum EstadoActual {
        CREADA,
        ASIGNADA,
        RECOLECTADA,
        EN_TRANSITO,
        EN_REPARTO,
        ENTREGADA,
        DEVUELTA,
        CANCELADA
    }
   */

  marcarComoEntregada(guia: GuiaItem): void {
    this.guiaSeleccionado = guia;
    this.tituloEntregada = 'Agregar evidencia';
    this.selectedTabIndex = 1;
    this.cambioProgmatico = true;
  }

  cambiarEstadoEntregada(guia: GuiaItem): void {
    console.log(guia);
    this.guiaSeleccionado = guia;
    this.tituloEntregada = 'Cambiar estado';
    this.selectedTabIndex = 2;
    this.cambioProgmatico = true;
  }

  cancelarEntrega(guia: GuiaItem): void {
    this.cambioProgmatico = false;
    this.selectedTabIndex = 3;
    this.guiaSeleccionado = guia;
    this.tituloIncidencia = 'Registro de incidencia por cancelación';
  }

  devolver(guia: GuiaItem): void {
    console.log(guia.guia);
    this.devolverGuia = true;
    this.guiaSeleccionado = guia;
    this.tituloIncidencia = 'Registro de incidencia por devolución';
    this.selectedTabIndex = 3;
    this.cambioProgmatico = true;
  }

  guardarIncidencia(): void {
    if (!this.devolverGuia) {
      // es decir fue cancelada
      this.guardandoIncidencia = true;
      let incidencia: IncidenciaModel = {
        idIncidencia: 0,
        idGuia: this.guiaSeleccionado.guia.idGuia,
        idRepartidor: this.ID_USUARIO_ACTUAL,
        codigoIncidencia: '',
        tipoIncidencia: this.formIncidencia.get('tipoIncidencia')?.value,
        severidad: this.formIncidencia.get('severidad')?.value,
        descripcion: this.formIncidencia.get('descripcion')?.value,
        solucionAplicada: '',
        requiereDevolucion: false,
        costoAdicional: this.formIncidencia.get('costoAdicional')?.value,
        fechaReporte: '',
        fechaAtencion: '',
        fechaResolucion: '',
        estado: 'ABIERTA',
      };
      this.incidenciaService.crearIncidencia(incidencia).subscribe({
        next: (data) => {
          this.cambiarEstado(
            this.guiaSeleccionado.guia.idGuia,
            'CANCELADA',
            this.formIncidencia.get('motivo')?.value,
            this.formIncidencia.get('comentarios')?.value
          );
          this.guardandoIncidencia = false;
          this.showSnackbar('Incidencia guardada correctamente', 'success-snackbar');
        },
        error: (error) => {
          console.log(error);
          this.showSnackbar('Error al crear la incidencia', 'error-snackbar');
        },
      });
    } else {
      this.guardandoIncidencia = true;
      let incidencia: IncidenciaModel = {
        idIncidencia: 0,
        idGuia: this.guiaSeleccionado.guia.idGuia,
        idRepartidor: this.ID_USUARIO_ACTUAL,
        codigoIncidencia: '',
        tipoIncidencia: this.formIncidencia.get('tipoIncidencia')?.value,
        severidad: this.formIncidencia.get('severidad')?.value,
        descripcion: this.formIncidencia.get('descripcion')?.value,
        solucionAplicada: '',
        requiereDevolucion: this.formIncidencia.get('requiereDevolucion')?.value ? true : false,
        costoAdicional: this.formIncidencia.get('costoAdicional')?.value,
        fechaReporte: '',
        fechaAtencion: '',
        fechaResolucion: '',
        estado: 'ABIERTA',
      };
      this.incidenciaService.crearIncidencia(incidencia).subscribe({
        next: (data) => {
          const estado = this.formIncidencia.get('ponerEnTransito')?.value
            ? 'EN_TRANSITO'
            : 'DEVUELTA';
          this.cambiarEstado(
            this.guiaSeleccionado.guia.idGuia,
            estado,
            this.formIncidencia.get('motivo')?.value,
            this.formIncidencia.get('comentarios')?.value
          );
          this.guardandoIncidencia = false;
          this.showSnackbar('Incidencia guardada correctamente', 'success-snackbar');
        },
        error: (error) => {
          console.log(error);
          this.showSnackbar('Error al crear la incidencia', 'error-snackbar');
        },
      });
    }
  }

  private cambiarEstado(idGuia: number, estado: string, motivo: string, comentarios: string) {
    this.seguiminetoService
      .cambiarEstadoPedido(idGuia, this.ID_USUARIO_ACTUAL, estado, motivo, comentarios)
      .subscribe({
        next: (data) => {
          this.guias = [];
          this.setGuias();
          this.showSnackbar(`${data.message}`, 'success-snackbar');
          this.guardandoCambioEstado = false;
          this.guardandoEvidencia = false;
          this.cancelarFormularios();
        },
        error: (error) => {
          console.log(error);
          this.showSnackbar('Error al actualizar el estado de la entrega', 'error-snackbar');
        },
      });
  }

  guardarCambioEstado(): void {
    if (!this.formCambioEstado.valid) {
      this.showSnackbar('Todos los campos son obligatorios', 'error-snackbar');
      return;
    }
    this.guardandoCambioEstado = true;

    const idGuia = this.guiaSeleccionado.guia.idGuia;
    const motivo = this.formCambioEstado.get('motivo')?.value;
    const comentarios = this.formCambioEstado.get('comentarios')?.value;

    let estadoActual = this.guiaSeleccionado.guia.estadoActual;

    switch (estadoActual) {
      case 'ASIGNADA':
        this.cambiarEstado(idGuia, 'RECOLECTADA', motivo, comentarios);
        break;
      case 'RECOLECTADA':
        this.cambiarEstado(idGuia, 'EN_TRANSITO', motivo, comentarios);
        break;
      case 'EN_TRANSITO':
        this.cambiarEstado(idGuia, 'EN_REPARTO', motivo, comentarios);
        break;
    }
  }

  guardarEvidencia(): void {
    if (!this.formEvidencia.valid) {
      this.showSnackbar('Todos los campos son obligatorios', 'error-snackbar');
      return;
    }
    if (!this.file) {
      this.showSnackbar('Debe seleccionar un archivo', 'error-snackbar');
      return;
    }
    if (this.file.size > 1000000) {
      this.showSnackbar('El archivo debe ser menor a 1MB', 'error-snackbar');
      return;
    }

    if (confirm('¿Desea guardar la evidencia?')) {
      this.guardandoEvidencia = true;
      const idGuia = this.guiaSeleccionado.guia.idGuia;
      const motivo = this.formEvidencia.get('motivo')?.value;
      const comentarios = this.formEvidencia.get('comentarios')?.value;
      const tipoEvidencia = this.formEvidencia.get('tipoEvidencia')?.value;
      this.filesService.upload(this.file).subscribe({
        next: (data) => {
          const evidencia: EvidenciaModel = {
            idEvidenciaEntrega: 0,
            idGuia: idGuia,
            tipoEvidencia: tipoEvidencia,
            nombreArchivo: this.file.name,
            urlArchivo: data.url,
            nombreReceptor: this.formEvidencia.get('receptor')?.value,
            documentoReceptor: '',
            parentescoReceptor: this.formEvidencia.get('parentesco')?.value,
            observaciones: this.formEvidencia.get('observaciones')?.value,
          };
          this.evidenciaService.crearEvidencia(evidencia).subscribe({
            next: (data) => {
              this.cambiarEstado(idGuia, 'ENTREGADA', motivo, comentarios);
              this.showSnackbar('Evidencia guardada correctamente', 'success-snackbar');
            },
            error: (error) => {
              this.showSnackbar('Error al guardar la evidencia', 'error-snackbar');
            },
          });
        },
        error: (error) => {
          console.log(error);
          this.showSnackbar('Error al subir el archivo', 'error-snackbar');
        },
      });
    }
  }

  filtrarPorEstado(estado: string): void {
    if (estado === 'VER TODOS') {
      this.setGuias();
    } else {
      this.seguiminetoService.getPedidosAsignados(this.ID_USUARIO_ACTUAL).subscribe({
        next: (data) => {
          this.guias = data.filter((item) => item.guia.estadoActual === estado);
        },
        error: (error) => {
          console.error('Error al filtrar las guías:', error);
          this.showSnackbar('Error al filtrar las guías', 'error-snackbar');
        },
      });
    }
  }

  onFileSelected(event: any): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.file = input.files[0];
    }
  }

  toggleExpanded(item: GuiaItem): void {
    item.expanded = !item.expanded;
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 1 || event.index === 2) {
      this.selectedTabIndex = 0;
    }
  }

  // Método para abrir el diálogo de cancelación del cliente
  registrarCancelacionCliente(idGuia: number): void {
    // Obtener ID de usuario de la sesión
    const idUsuario = Number(sessionStorage.getItem('idUsuario')) || 0;
    
    if (idUsuario === 0) {
      this.showSnackbar('No se ha podido identificar al usuario. Inicie sesión nuevamente.' , 'error-snackbar');
      return;
    }
    
    const dialogRef = this.dialog.open(CancelacionClienteDialogComponent, {
      width: '500px',
      data: { idGuia, idUsuario }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result && result.success) {
        this.showSnackbar(result.message || 'Cancelación registrada correctamente', 'success-snackbar');
        // Recargar los datos
        this.setGuias();
      } else if (result && !result.success) {
        this.showSnackbar(result.message || 'Error al registrar la cancelación', 'error-snackbar');
      }
    });
  }
  
  showSnackbar(message: string, tipo: string): void {
    const mensaje = message;
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }
}
