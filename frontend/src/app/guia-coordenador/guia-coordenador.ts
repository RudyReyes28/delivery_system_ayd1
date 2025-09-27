import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Angular Material Imports
import { MatCard, MatCardHeader, MatCardTitle, MatCardSubtitle, MatCardContent } from '@angular/material/card';
import { MatTabGroup, MatTab } from '@angular/material/tabs';
import { MatTable, MatHeaderCell, MatHeaderCellDef, MatCell, MatCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatColumnDef } from '@angular/material/table';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatSelect, MatOption } from '@angular/material/select';
import { MatInput } from '@angular/material/input';
import { MatChip } from '@angular/material/chips';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltip } from '@angular/material/tooltip';

// Services and Models
import { GuiaRepartidorService } from '../../services/guia-repartidor.service';
import { 
  GuiaResponse, 
  Repartidor, 
  RepartidorCompleto,
  Guia,
  AsignarRepartidorRequest 
} from '../../models/guia-repartidor.model';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-guia-coordenador',
  templateUrl: './guia-coordenador.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatColumnDef,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatTabGroup,
    MatTab,
    MatTable,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatButton,
    MatIconButton,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    MatInput,
    MatIcon,
    MatChip,
    MatSpinner,
    MatTooltip
  ],
  styleUrls: ['./guia-coordenador.scss']
})
export class GuiaCoordenador implements OnInit {
  todasLasGuias: GuiaResponse[] = [];
  guiasPendientes: GuiaResponse[] = [];
  repartidoresActivos: Repartidor[] = [];
  todosLosRepartidores: RepartidorCompleto[] = [];
  
  columnasTodasGuias: string[] = [
    'numeroGuia', 
    'cliente', 
    'descripcion', 
    'estadoActual', 
    'repartidor', 
    'fechaCreacion',
    'acciones'
  ];
  
  columnasPendientes: string[] = [
    'numeroGuia', 
    'cliente', 
    'descripcion', 
    'estadoActual', 
    'acciones'
  ];

  columnasRepartidores: string[] = [
    'nombre',
    'contacto',
    'zona',
    'estadisticas',
    'guiasAsignadas',
    'accionesRepartidor'
  ];

  loading = false;
  selectedGuiaId: number | null = null;
  selectedRepartidorId: number | null = null;
  fechaEntrega = '';

  mostrarDetalleGuia = false;
  mostrarDetalleRepartidor = false;
  guiaSeleccionada: Guia | null = null;
  repartidorSeleccionado: Repartidor | null = null;

  constructor(
    private guiaService: GuiaRepartidorService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.loading = true;
    
    // Cargar todas las guías
    this.guiaService.obtenerTodasLasGuias().subscribe({
      next: (guias) => {
        this.todasLasGuias = guias;
        console.log('Todas las guías cargadas:', guias);
      },
      error: (error) => {
        console.error('Error al cargar todas las guías:', error);
        this.mostrarMensaje('Error al cargar las guías');
      }
    });

    // Cargar guías sin repartidor
    this.guiaService.obtenerGuiasSinRepartidor().subscribe({
      next: (guias) => {
        this.guiasPendientes = guias;
        console.log('Guías pendientes cargadas:', guias);
      },
      error: (error) => {
        console.error('Error al cargar guías pendientes:', error);
        this.mostrarMensaje('Error al cargar las guías pendientes');
      }
    });

    // Cargar repartidores activos
    this.guiaService.obtenerRepartidoresActivos().subscribe({
      next: (repartidores) => {
        this.repartidoresActivos = repartidores;
        console.log('Repartidores activos cargados:', repartidores);
      },
      error: (error) => {
        console.error('Error al cargar repartidores:', error);
        this.mostrarMensaje('Error al cargar los repartidores');
      }
    });

    // Cargar todos los repartidores
    this.guiaService.obtenerTodosLosRepartidores().subscribe({
      next: (repartidores) => {
        this.todosLosRepartidores = repartidores;
        console.log('Todos los repartidores cargados:', repartidores);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar todos los repartidores:', error);
        this.mostrarMensaje('Error al cargar todos los repartidores');
        this.loading = false;
      }
    });
  }

  seleccionarGuia(guiaId: number): void {
    this.selectedGuiaId = guiaId;
  }

  asignarRepartidor(): void {
    if (!this.selectedGuiaId || !this.selectedRepartidorId || !this.fechaEntrega) {
      this.mostrarMensaje('Por favor, seleccione una guía, un repartidor y una fecha de entrega');
      return;
    }

    const request: AsignarRepartidorRequest = {
      idGuia: this.selectedGuiaId,
      idRepartidor: this.selectedRepartidorId,
      fechaEntrega: this.fechaEntrega
    };

    this.loading = true;
    this.guiaService.asignarRepartidor(request).subscribe({
      next: (response) => {
        this.mostrarMensaje(response.message);
        this.cargarDatos();
        this.limpiarSeleccion();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al asignar repartidor:', error);
        this.mostrarMensaje('Error al asignar el repartidor');
        this.loading = false;
      }
    });
  }

  limpiarSeleccion(): void {
    this.selectedGuiaId = null;
    this.selectedRepartidorId = null;
    this.fechaEntrega = '';
  }

  // Métodos para modales
  verDetalleGuia(guia: Guia): void {
    this.guiaSeleccionada = guia;
    this.mostrarDetalleGuia = true;
  }

  cerrarDetalleGuia(): void {
    this.mostrarDetalleGuia = false;
    this.guiaSeleccionada = null;
  }

  verDetalleRepartidor(repartidor: Repartidor): void {
    this.repartidorSeleccionado = repartidor;
    this.mostrarDetalleRepartidor = true;
  }

  cerrarDetalleRepartidor(): void {
    this.mostrarDetalleRepartidor = false;
    this.repartidorSeleccionado = null;
  }

  // Métodos de utilidad
  obtenerNombreRepartidor(repartidor: any): string {
    return repartidor ? repartidor.nombreCompleto : 'Sin asignar';
  }

  obtenerEstadoRepartidor(disponibilidad: string): string {
    const estados: { [key: string]: string } = {
      'DISPONIBLE': 'Disponible',
      'OCUPADO': 'Ocupado',
      'INACTIVO': 'Inactivo'
    };
    return estados[disponibilidad] || disponibilidad;
  }

  calcularTasaExito(repartidor: Repartidor): number {
    const total = (repartidor.totalEntregasCompletadas || 0) + (repartidor.totalEntregasFallidas || 0);
    if (total === 0) return 0;
    return Math.round(((repartidor.totalEntregasCompletadas || 0) / total) * 100);
  }

  private mostrarMensaje(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  // Método para formatear fecha
  formatearFecha(fecha: string): string {
    if (!fecha) return 'No disponible';
    return new Date(fecha).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Método para obtener la fecha mínima (hoy)
  getFechaMinima(): string {
    return new Date().toISOString().split('T')[0];
  }
}
