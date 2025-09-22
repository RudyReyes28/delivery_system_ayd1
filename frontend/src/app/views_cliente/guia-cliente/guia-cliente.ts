import { Component } from '@angular/core';
import { GuiaClienteDetalleResponse, GuiaDetalle } from '../../../models/guia.cliente.model';
import { GuiaClienteService } from '../../../services/guia-cliente.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MatChip, MatChipListbox, MatChipOption, MatChipSet } from '@angular/material/chips';
import { MatStep, MatStepperModule } from '@angular/material/stepper';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-guia-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatCard,
    MatCardContent,
    MatFormField,
    MatLabel,
    MatIcon,
    MatSpinner,
    MatCardHeader,
    MatCardTitle,
    MatChipSet,
    MatCardSubtitle,
    MatChip,
    MatStepperModule,
    MatStep
],
  templateUrl: './guia-cliente.html',
  styleUrl: './guia-cliente.scss'
})
export class GuiaCliente {
  
  numeroGuia: number = 0;
  guiaDetalle: GuiaDetalle | null = null;
  buscandoGuia: boolean = false;
  mostrarInfoEmpresa: boolean = true;
  cargando: boolean = false;

  // Información de la empresa
  infoEmpresa = {
    nombre: 'Sistema Integral de Entregas',
    mision: 'Diseñar e implementar soluciones tecnológicas que optimicen la gestión logística de entregas, garantizando eficiencia operativa, transparencia en procesos y satisfacción de todos los actores involucrados: comercios, repartidores y clientes finales.',
    vision: 'Ser el sistema líder en transformación digital para empresas de mensajería y logística, ofreciendo herramientas inteligentes que impulsen la fidelización, el control operativo y la trazabilidad de cada entrega en tiempo real.',
    valores: [
      'Compromiso con la calidad',
      'Responsabilidad y puntualidad',
      'Innovación constante',
      'Respeto y honestidad',
      'Servicio al cliente excepcional'
    ],
    contacto: {
      telefono: '+502 7765-1915',
      email: 'sie@gmail.com',
      direccion: 'Zona 11, Quetzaltenango',
      horarios: 'Lunes a Sabado: 8:00 AM - 6:00 PM'
    }
  };

  constructor(private guiaService: GuiaClienteService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {}

  buscarGuia(): void {
    if (!this.numeroGuia || this.numeroGuia <= 0) {
      this.mostrarMensaje("Por favor ingrese un número de guía válido", "info-snackbar");
      return;
    }

    this.cargando = true;
    this.guiaService.obtenerDetalleGuiaCliente(this.numeroGuia).subscribe({
      next: (response: GuiaClienteDetalleResponse) => {
        this.guiaDetalle = response.guia;
        this.mostrarInfoEmpresa = false;
        this.buscandoGuia = true;
        this.cargando = false;
      },
      error: (error) => {
        this.mostrarMensaje(error.error?.message || "No se encontró la guía o ocurrió un error", "error-snackbar");
        this.cargando = false;
      }
    });
  }

  limpiarBusqueda(): void {
    this.numeroGuia = 0;
    this.guiaDetalle = null;
    this.buscandoGuia = false;
    this.mostrarInfoEmpresa = true;
  }

  obtenerColorEstado(estado: string): string {
    if (!estado) return 'primary';
    
    const estadosColores: { [key: string]: string } = {
      'CREADA': 'primary',
      'EN_TRANSITO': 'accent',
      'ENTREGADO': 'primary',
      'PENDIENTE': 'warn',
      'CANCELADO': 'warn'
    };
    return estadosColores[estado.toUpperCase()] || 'primary';
  }

  obtenerColorPrioridad(prioridad: string): string {
    if (!prioridad) return 'primary';
    
    const prioridadesColores: { [key: string]: string } = {
      'NORMAL': 'primary',
      'ALTA': 'accent',
      'URGENTE': 'warn'
    };
    return prioridadesColores[prioridad.toUpperCase()] || 'primary';
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
}
