import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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

//servicios y tipos
import { ContratoService } from '../../../services/gestion-contrato-service/Contrato.service';
import { ContratoComisionService } from '../../../services/gestion-contrato-service/ContratoComision.service';
import { UsuarioService } from '../../../services/gestion-usuario-service/Usuario.service';
import { PersonaService } from '../../../services/gestion-usuario-service/Persona.service';
import { EmpleadoService } from '../../../services/gestion-usuario-service/Empleado.service';
import { RepartidorService } from '../../../services/gestion-usuario-service/Repartidor.service';
import {
  ContratoComisionDTO,
  ContratoModel,
  ContratoRequestDTO,
  ESTADOS_CONTRATO,
  FRECUENCIA_PAGO,
  MODALIDADES_TRABAJO,
  RegisterContratoDTO,
  TIPOS_COMISION,
  TIPOS_CONTRATO,
} from '../../../models/contrato-model.ts/ContratoModel';
import { ContratoItem } from '../../../models/contrato-model.ts/ContratoItem';
import { EmpleadoRequestDto } from '../../../models/usuario-model/RegisterEmpleadoModel';

@Component({
  selector: 'app-contratos',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatChipsModule,
    MatListModule,
    MatSnackBarModule,
    MatTabsModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
  ],
  templateUrl: './contratos.html',
  styleUrl: './contratos.scss',
})
export class Contratos implements OnInit {
  contratoForm: FormGroup = new FormGroup({});
  guardandoContrato: boolean = false;
  cambioProgmatico: boolean = false;
  selectedTabIndex: number = 0;
  modoEdicion: boolean = false;
  loadingContratos: boolean = false;

  //contratos
  contratos: ContratoItem[] = [];

  //sobre los contratos
  tipos = TIPOS_CONTRATO;
  modalidades = MODALIDADES_TRABAJO;
  frecuencias = FRECUENCIA_PAGO;
  estadosContrato = ESTADOS_CONTRATO;
  //sobre comisiones
  tiposComision = TIPOS_COMISION;

  //otras opciones
  otrasOpciones = [
    { value: '1', viewValue: 'Ver todos' },
    { value: '2', viewValue: 'A punto de caducir' },
  ];

  //contrato seleccionado para edicion
  contratoSeleccionado!: ContratoModel;
  comisionSeleccionada!: ContratoComisionDTO;

  //empleados
  empleados: EmpleadoRequestDto[] = [];

  constructor(
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private personService: PersonaService,
    private empleadoService: EmpleadoService,
    private repartidorService: RepartidorService,
    private contratService: ContratoService,
    private comisionService: ContratoComisionService
  ) {
    this.contratoForm = this.crearFormulario();
    this.selectedTabIndex = 1;
  }

  ngOnInit(): void {
    this.contratoForm = this.crearFormulario();
    this.setContratos();
    this.setEmpleados();
  }

  private logInvalidControls(formGroup: FormGroup | FormArray): void {
    Object.keys(formGroup.controls).forEach((key) => {
      const control = formGroup.get(key);
      if (control instanceof FormGroup || control instanceof FormArray) {
        if (control.invalid) {
          console.log(`Grupo/Array inválido: ${key}`, control.errors);
          this.logInvalidControls(control); // Recursivamente para grupos anidados
        }
      } else if (control && control.invalid) {
        console.log(`Campo inválido: ${key}, Valor: ${control.value}, Errores: `, control.errors);
      }
    });
  }

  filtrarPorEmpleado(idEmpleado: number): void {
    console.log('Empleado seleccionado ID:', idEmpleado);
    if (idEmpleado) {
      this.contratService.contratosPorEmpleado(idEmpleado).subscribe({
        next: (data: ContratoModel[]) => {
          this.contratos = [];
          data.forEach((contrato) => {
            this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
              next: (comision: ContratoComisionDTO) => {
                this.contratos.push({ contrato, comision, expanded: false });
                this.loadingContratos = true;
              },
              error: (err) => {
                console.error('Error al obtener la comisión del contrato:', err);
              },
            });
          });
        },
        error: (err) => {
          console.error('Error al filtrar contratos por empleado:', err);
        },
      });
    } else {
      this.contratos = [];
      this.setContratos();
    }
  }

  filtrarPorTipo(tipo: string): void {
    if (tipo) {
      this.contratService.contratosPorTipoContrato(tipo).subscribe({
        next: (data: ContratoModel[]) => {
          this.contratos = [];
          data.forEach((contrato) => {
            this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
              next: (comision: ContratoComisionDTO) => {
                this.contratos.push({ contrato, comision, expanded: false });
                this.loadingContratos = true;
              },
              error: (err) => {
                console.error('Error al obtener la comisión del contrato:', err);
              },
            });
          });
        },
        error: (err) => {
          console.error('Error al filtrar contratos por estado:', err);
        },
      });
    } else {
      this.contratos = [];
      this.setContratos();
    }
  }

  filtrarPorModalidad(modalidad: string): void {
    if (modalidad) {
     this.contratService.contratosPorModalidad(modalidad).subscribe({
       next: (data: ContratoModel[]) => {
         this.contratos = [];
         data.forEach((contrato) => {
           this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
             next: (comision: ContratoComisionDTO) => {
               this.contratos.push({ contrato, comision, expanded: false });
               this.loadingContratos = true;
             },
             error: (err) => {
               console.error('Error al obtener la comisión del contrato:', err);
             },
           });
         });
       },
       error: (err) => {
         console.error('Error al filtrar contratos por estado:', err);
       },
     });
    } else {
      this.contratos = [];
      this.setContratos();
    }
  }

  filtrarPorEstado(estado: string): void {
    if (estado) {
      this.contratService.contratosPorEstado(estado).subscribe({
        next: (data: ContratoModel[]) => {
          this.contratos = [];
          data.forEach((contrato) => {
            this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
              next: (comision: ContratoComisionDTO) => {
                this.contratos.push({ contrato, comision, expanded: false });
                this.loadingContratos = true;
              },
              error: (err) => {
                console.error('Error al obtener la comisión del contrato:', err);
              },
            });
          });
        },
        error: (err) => {
          console.error('Error al filtrar contratos por estado:', err);
        },
      });
    } else {
      this.contratos = [];
      this.setContratos();
    }
  }

  filtrarContratos(opcion: string): void {
    if (opcion === '1') {
      this.contratos = [];
      this.setContratos();
    } else if (opcion === '2') {
      this.contratService.contratosApuntoDeCaducir().subscribe({
        next: (data: ContratoModel[]) => {
          this.contratos = [];
          data.forEach((contrato) => {
            this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
              next: (comision: ContratoComisionDTO) => {
                this.contratos.push({ contrato, comision, expanded: false });
                this.loadingContratos = true;
              },
              error: (err) => {
                console.error('Error al obtener la comisión del contrato:', err);
              },
            });
          });
        },
        error: (err) => {
          console.error('Error al filtrar contratos a punto de caducir:', err);
        },
      });
    }
  }

  setEmpleados(): void {
    this.empleadoService.obtenerTodosLosEmpleados().subscribe({
      next: (data: EmpleadoRequestDto[]) => {
        this.empleados = data;
      },
      error: (err) => {
        console.error('Error al obtener los empleados:', err);
      },
    });
  }


  setContratos(): void {
    this.contratService.obtenerTodosLosContratos().subscribe({
      next: (data: ContratoModel[]) => {
        data.forEach((contrato) => {
          this.comisionService.obtenerContratoComisionPorContrato(contrato.idContrato).subscribe({
            next: (comision: ContratoComisionDTO) => {
              this.contratos.push({ contrato, comision, expanded: false });
              this.loadingContratos = true;
            },
            error: (err) => {
              console.error('Error al obtener la comisión del contrato:', err);
            },
          });
        });
      },
    });
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 0) {
      this.selectedTabIndex = 1;
    }
  }

  crearFormulario(): FormGroup {
    return this.fb.group({
      // Contrato inicial
      numeroContrato: ['', Validators.required],
      tipoContrato: ['', Validators.required],
      modalidadTrabajo: ['', Validators.required],
      fechaInicio: ['', Validators.required],
      fechaFin: [''],
      estadoContrato: ['', Validators.required],
      salarioBase: ['', [Validators.required, Validators.min(1)]],
      frecuenciaPago: ['', Validators.required],
      renovacionAutomatica: [false],
      incluyeAguinaldo: [false],
      incluyeBono14: [false],
      incluyeVacaciones: [false],
      incluyeIgss: [false],

      // Comisiones
      tipoComision: ['', Validators.required],
      porcentaje: ['', [Validators.required, Validators.min(0), Validators.max(1)]],
      montoFijo: ['', Validators.required],
      fechaDesde: ['', Validators.required],
      fechaHasta: ['', Validators.required],
      comisionActiva: [true],
      minimoEntregasMes: ['', [Validators.required, Validators.min(1)]],
      maximoEntregasMes: ['', [Validators.required]],
      factorMultiplicador: ['', [Validators.required, Validators.min(0)]],
    });
  }

  cancelarFormulario(): void {
    this.resetearFormularioCompleto();
    this.guardandoContrato = false;
    this.cambioProgmatico = true;
    this.selectedTabIndex = 1;
  }

  suspenderContrato(contrato: ContratoModel): void {
    this.actualizarContrato('SUSPENDIDO', contrato);
  }

  rescindirContrato(contrato: ContratoModel): void {
    this.actualizarContrato('RESCINDIDO', contrato);
  }

  private actualizarContrato(estado: string, contrato: ContratoModel): void {
    const contratoActualizado: ContratoModel = {
      ...contrato,
      estadoContrato: estado,
    };
    this.contratService.actualizarContrato(contratoActualizado).subscribe({
      next: (data: ContratoModel) => {
        this.showSnackbar(`Contrato ${estado.toLowerCase()} con éxito.`, 'success-snackbar');
        this.contratos = [];
        this.setContratos();
      },
      error: (err) => {
        console.error(`Error al actualizar el contrato a ${estado}:`, err);
        this.showSnackbar(`Error al actualizar el contrato a ${estado}.`, 'error-snackbar');
      },
    });
  }

  editarContrato(contrato: ContratoItem): void {
    console.log(contrato);

    this.modoEdicion = true;
    this.contratoSeleccionado = contrato.contrato;
    this.comisionSeleccionada = contrato.comision;
    this.contratoForm.patchValue({
      // Contrato inicial
      numeroContrato: contrato.contrato.numeroContrato,
      tipoContrato: contrato.contrato.tipoContrato,
      modalidadTrabajo: contrato.contrato.modalidadTrabajo,
      fechaInicio: contrato.contrato.fechaInicio,
      fechaFin: contrato.contrato.fechaFin,
      estadoContrato: contrato.contrato.estadoContrato,
      salarioBase: contrato.contrato.salarioBase,
      frecuenciaPago: contrato.contrato.frecuenciaPago,
      renovacionAutomatica: contrato.contrato.renovacionAutomatica,
      incluyeAguinaldo: contrato.contrato.incluyeAguinaldo,
      incluyeBono14: contrato.contrato.incluyeBono14,
      incluyeVacaciones: contrato.contrato.incluyeVacaciones,
      incluyeIgss: contrato.contrato.incluyeIgss,

      // Comisiones
      tipoComision: contrato.comision.tipoComision,
      porcentaje: contrato.comision.porcentaje,
      montoFijo: contrato.comision.montoFijo,
      fechaDesde: contrato.comision.aplicaDesde,
      fechaHasta: contrato.comision.aplicaHasta,
      comisionActiva: contrato.comision.activo,
      minimoEntregasMes: contrato.comision.minimoEntregasMes,
      maximoEntregasMes: contrato.comision.maximoEntregasMes,
      factorMultiplicador: contrato.comision.factorMultiplicador,
    });
    this.logInvalidControls(this.contratoForm);
    this.cambioProgmatico = true;
    this.selectedTabIndex = 0;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  guardarContrato(): void {
    if (this.contratoForm.invalid) {
      this.logInvalidControls(this.contratoForm);
      this.showSnackbar('Por favor, complete todos los campos obligatorios.', 'error-snackbar');
      return;
    }

    this.guardandoContrato = true;
    const contrato: ContratoModel = {
      idContrato: this.contratoSeleccionado.idContrato,
      idEmpleado: this.contratoSeleccionado.idEmpleado,
      numeroContrato: this.contratoForm.value.numeroContrato,
      tipoContrato: this.contratoForm.value.tipoContrato,
      modalidadTrabajo: this.contratoForm.value.modalidadTrabajo,
      fechaInicio: this.contratoForm.value.fechaInicio,
      fechaFin: this.contratoForm.value.fechaFin,
      salarioBase: this.contratoForm.value.salarioBase,
      moneda: 'GTQ',
      frecuenciaPago: this.contratoForm.value.frecuenciaPago,
      renovacionAutomatica: this.contratoForm.value.renovacionAutomatica,
      incluyeAguinaldo: this.contratoForm.value.incluyeAguinaldo,
      incluyeBono14: this.contratoForm.value.incluyeBono14,
      incluyeVacaciones: this.contratoForm.value.incluyeVacaciones,
      incluyeIgss: this.contratoForm.value.incluyeIgss,
      estadoContrato: this.contratoForm.value.estadoContrato,
    };

    const comision: ContratoComisionDTO = {
      idContratoComision: this.comisionSeleccionada.idContratoComision,
      idContrato: this.contratoSeleccionado.idContrato,
      tipoComision: this.contratoForm.value.tipoComision,
      porcentaje: this.contratoForm.value.porcentaje,
      montoFijo: this.contratoForm.value.montoFijo,
      aplicaDesde: this.contratoForm.value.fechaDesde,
      aplicaHasta: this.contratoForm.value.fechaHasta,
      activo: this.contratoForm.value.comisionActiva,
      minimoEntregasMes: this.contratoForm.value.minimoEntregasMes,
      maximoEntregasMes: this.contratoForm.value.maximoEntregasMes,
      factorMultiplicador: this.contratoForm.value.factorMultiplicador,
      createdAt: this.comisionSeleccionada.createdAt,
    };

    console.log('contrato guardando...', contrato);
    console.log('comision guardando...', comision);

    this.contratService.actualizarContrato(contrato).subscribe({
      next: (data: ContratoModel) => {
        //voy a quitar esto despues de corregir lo de la comision
        this.showSnackbar('Contrato actualizado con éxito.', 'success-snackbar');
        this.resetearFormularioCompleto();
        this.guardandoContrato = false;
        this.modoEdicion = false;
        this.cambioProgmatico = true;
        this.selectedTabIndex = 1;
        this.contratos = [];
        this.setContratos();

        this.comisionService.actualizarContratoComision(comision).subscribe({
          next: (dataComision: ContratoComisionDTO) => {
            this.showSnackbar('Contrato actualizado con éxito.', 'success-snackbar');
            this.resetearFormularioCompleto();
            this.guardandoContrato = false;
            this.modoEdicion = false;
            this.cambioProgmatico = true;
            this.selectedTabIndex = 1;
            this.contratos = [];
            this.setContratos();
          },
          error: (err) => {
            console.error('Error al actualizar la comisión del contrato:', err);
            this.showSnackbar('Error al actualizar la comisión del contrato.', 'error-snackbar');
            this.guardandoContrato = false;
          },
        });
      },
      error: (err) => {
        console.error('Error al actualizar el contrato:', err);
        this.showSnackbar('Error al actualizar el contrato.', 'error-snackbar');
        this.guardandoContrato = false;
      },
    });
  }

  resetearFormularioCompleto(): void {
    this.contratoForm.reset();
    this.contratoForm.markAsUntouched();
    this.contratoForm.markAsPristine();
  }

  toggleExpanded(item: any): void {
    item.expanded = !item.expanded;
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
