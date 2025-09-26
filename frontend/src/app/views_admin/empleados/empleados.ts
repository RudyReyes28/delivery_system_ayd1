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

// Importando Servicios
import { UsuarioService } from '../../../services/gestion-usuario-service/Usuario.service';
import { UsuarioModel } from '../../../models/usuario-model/UsuarioModel';
import { Rol } from '../../../models/usuario-model/RolModel';
import { RolesService } from '../../../services/gestion-usuario-service/Roles.service';
import {
  EmpleadoRequestDto,
  RegisterEmpleadoModel,
  UsuarioRequestDto,
} from '../../../models/usuario-model/RegisterEmpleadoModel';
import { UsuarioItem } from '../../../models/usuario-model/UsuarioItem';
import { EmpleadoService } from '../../../services/gestion-usuario-service/Empleado.service';
import { PersonaService } from '../../../services/gestion-usuario-service/Persona.service';
import { PersonaModel } from '../../../models/usuario-model/PersonaModel';
import {
  departamentosMunicipios,
  DireccionModel,
  TIPOS_DIRECCION,
} from '../../../models/usuario-model/DireccionModel';
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
import { ESTADOS_CIVILES, ESTADOS_EMPLEADO } from '../../../models/usuario-model/EstadosEmpleado';
import {
  DISPONIBILIDAD_REPARTIDOR,
  RepartidorDTO,
  TIPOS_LICENCIA,
} from '../../../models/usuario-model/RepartidorModel';
import { Vehiculo } from '../../../models/VehiculoModel';
import { VehiculoService } from '../../../services/vehiculo-service/Vehiculo.service';
import { RepartidorVehiculoService } from '../../../services/vehiculo-service/RepartidorVehiculo.service';
import { ContratoService } from '../../../services/gestion-contrato-service/Contrato.service';
import { ContratoComisionService } from '../../../services/gestion-contrato-service/ContratoComision.service';
import { RepartidorService } from '../../../services/gestion-usuario-service/Repartidor.service';
import { DireccionService } from '../../../services/gestion-usuario-service/Direccion.service';

// Importando modelos

@Component({
  selector: 'app-empleados',
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSnackBarModule,
    MatTabsModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
  ],
  templateUrl: './empleados.html',
  styleUrl: './empleados.scss',
})
export class Empleados implements OnInit {
  roles: Rol[] = [];
  empleados: UsuarioItem[] = [];
  users: UsuarioModel[] = [];
  loadingEmpleados: boolean = false;
  modoEdicion: boolean = false;

  empleadoForm: FormGroup = new FormGroup({});
  guardandoEmpleado: boolean = false;
  cambioProgmatico: boolean = false;
  selectedTabIndex: number = 0;

  empleadoEditando: EmpleadoRequestDto = {
    idEmpleado: 0,
    idUsuario: 0,
    codigoEmpleado: '',
    numeroIgss: '',
    numeroIrtra: '',
    tipoSangre: '',
    estadoCivil: '',
    numeroDependientes: 0,
    contactoEmergenciaNombre: '',
    contactoEmergenciaTelefono: '',
    estadoEmpleado: '',
    fechaIngreso: '',
    fechaSalida: null,
    motivoSalida: null,
  };
  usuarioEditando: UsuarioModel = {
    idUsuario: 0,
    idPersona: 0,
    idRol: 0,
    nombreRol: '',
    nombreUsuario: '',
    twoFactorEnabled: false,
    intentosFallidos: 0,
    ultimaFechaAcceso: '',
    estado: '',
    fechaCreacion: '',
    fechaUltimaActualizacion: '',
    nombre: '',
    apellido: '',
    fechaNacimiento: '',
    dpi: '',
    correo: '',
    telefono: '',
    direccion: '',
    estadoPersona: '',
  };
  direccionEditando: DireccionModel = {
    idDireccion: 0,
    tipoDireccion: '',
    municipio: '',
    departamento: '',
    pais: 'Guatemala',
    codigoPostal: '',
    referencias: '',
    activa: true,
  };

  //direccion
  direccion: DireccionModel = {
    idDireccion: 0,
    tipoDireccion: '',
    municipio: '',
    departamento: '',
    pais: 'Guatemala',
    codigoPostal: '',
    referencias: '',
    activa: true,
  };

  tiposDireccion: string[] = TIPOS_DIRECCION;

  //sobre datos de usuario
  usuarioRequest: UsuarioRequestDto = {
    idUsuario: 0,
    idPersona: 0,
    idRol: 0,
    idDireccion: 0,
    nombreUsuario: '',
    contrasena: '',
    nombre: '',
    apellido: '',
    fechaNacimiento: '',
    dpi: '',
    correo: '',
    telefono: '',
    estado: '',
  };
  //sobre datos de empleado
  empleadoRequest: EmpleadoRequestDto = {
    idEmpleado: 0,
    idUsuario: 0,
    codigoEmpleado: '',
    numeroIgss: '',
    numeroIrtra: '',
    tipoSangre: '',
    estadoCivil: '',
    numeroDependientes: 0,
    contactoEmergenciaNombre: '',
    contactoEmergenciaTelefono: '',
    estadoEmpleado: '',
    fechaIngreso: '',
    fechaSalida: '',
    motivoSalida: '',
  };

  registerEmpleado: RegisterEmpleadoModel = {
    usuarioRequestDdto: this.usuarioRequest,
    empleadoRequestDdto: this.empleadoRequest,
  };

  estadosEmpleado: string[] = ESTADOS_EMPLEADO;
  estadosCivil: string[] = ESTADOS_CIVILES;

  //datos del repartidor
  repartidor: RepartidorDTO = {
    idRepartidor: 0,
    idEmpleado: 0,
    numeroLicencia: '',
    tipoLicencia: '',
    fechaVencimientoLicencia: '',
    zonaAsignada: '',
    radioCoberturaKm: 0,
    disponibilidad: '',
    calificacionPromedio: 0,
    totalEntregasCompletadas: 0,
    totalEntregasFallidas: 0,
  };

  isRepartidor: boolean = false;

  vehiculos: Vehiculo[] = [];
  vehiculosAsignados: number[] = [];

  disponibilidadRepartidor: string[] = DISPONIBILIDAD_REPARTIDOR;
  tiposLicencia: string[] = TIPOS_LICENCIA;

  //sobre datos de direccion
  datosDireccion = departamentosMunicipios;

  departamentos = Object.keys(departamentosMunicipios);

  municipios: string[] = [];

  //sobre los contratos
  tipos = TIPOS_CONTRATO;
  modalidades = MODALIDADES_TRABAJO;
  frecuencias = FRECUENCIA_PAGO;
  estadosContrato = ESTADOS_CONTRATO;

  contratoRequest: ContratoRequestDTO = {
    idContrato: 0,
    idEmpleado: 0,
    numeroContrato: '',
    tipoContrato: '',
    modalidadTrabajo: '',
    fechaInicio: '',
    fechaFin: '',
    renovacionAutomatica: false,
    salarioBase: 0,
    moneda: '',
    frecuenciaPago: '',
    incluyeAguinaldo: false,
    incluyeBono14: false,
    incluyeVacaciones: false,
    incluyeIgss: false,
    estadoContrato: '',
  };

  contratoComisionRequest: ContratoComisionDTO = {
    idContratoComision: 0,
    idContrato: 0,
    tipoComision: '',
    porcentaje: 0,
    montoFijo: 0,
    aplicaDesde: '',
    aplicaHasta: '',
    activo: false,
    minimoEntregasMes: 0,
    maximoEntregasMes: 0,
    factorMultiplicador: 0,
    createdAt: '',
  };
  registroContrato: RegisterContratoDTO = {
    contrato: this.contratoRequest,
    comision: this.contratoComisionRequest,
  };
  //sobre comisiones
  tiposComision = TIPOS_COMISION;
  constructor(
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private rolService: RolesService,
    private personaService: PersonaService,
    private usuarioService: UsuarioService,
    private empleadoService: EmpleadoService,
    private contratoService: ContratoService,
    private comisionService: ContratoComisionService,
    private vehiculoService: VehiculoService,
    private vehiculoRepartidor: RepartidorVehiculoService,
    private repartidorService: RepartidorService,
    private direccionService: DireccionService
  ) {
    this.empleadoForm = this.crearFormulario();
    this.isRepartidor = false;
  }

  ngOnInit(): void {
    this.setEmpleados();
    this.setRoles();
    this.setVehiculos();
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

  crearFormulario(): FormGroup {
    return this.formBuilder.group({
      // Información del Usuario
      nombreUsuario: ['', Validators.required],
      password: ['', Validators.required],
      idRol: ['', Validators.required],
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      dpi: ['', [Validators.required, Validators.minLength(13), Validators.maxLength(13)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],

      // Dirección
      departamento: ['', Validators.required],
      municipio: ['', Validators.required],
      codigoPostal: ['', Validators.required],
      tipoDireccion: ['', Validators.required],
      referencias: ['', Validators.required],

      // Información de Empleado
      codigoEmpleado: ['', Validators.required],
      numeroIgss: ['', Validators.required],
      numeroIrtra: ['', Validators.required],
      tipoSangre: ['', Validators.required],
      estadoCivil: ['', Validators.required],
      numeroDependientes: [0, [Validators.required, Validators.min(0)]],
      contactoEmergenciaNombre: ['', Validators.required],
      contactoEmergenciaTelefono: [
        '',
        [Validators.required, Validators.minLength(8), Validators.maxLength(8)],
      ],
      estadoEmpleado: ['', Validators.required],
      fechaIngreso: ['', Validators.required],
      fechaSalida: [''],
      motivoSalida: [''],

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

      // Campos adicionales de Repartidor (se activan dinámicamente en onChancheRol)
      disponibilidad: [{ value: '', disabled: true }],
      numeroLicencia: [{ value: '', disabled: true }],
      tipoLicencia: [{ value: '', disabled: true }],
      fechaVenLicencia: [{ value: '', disabled: true }],
      zonaAsignada: [{ value: '', disabled: true }],
      radioCobertura: [{ value: '', disabled: true }],
      vehiculosAsignados: [{ value: [], disabled: true }],
    });
  }

  onTabChange(event: any): void {
    if (this.cambioProgmatico) {
      this.cambioProgmatico = false;
      return;
    }
    if (event.index === 0) {
      if (!this.modoEdicion) {
        this.crearNuevoEmpleado();
      }
    } else {
      this.modoEdicion = false;
    }
  }

  onChangeDepartamento(departamento: string): void {
    if (this.departamentos.includes(departamento)) {
      this.municipios = this.datosDireccion[departamento];
    } else {
      this.municipios = [];
      this.empleadoForm.get('municipio')?.setValue('');
    }
  }

  onChancheRol(rolID: number): void {
    const rol = this.roles.find((rol) => rol.idRol === rolID);
    this.isRepartidor = rol?.nombre === 'REPARTIDOR';

    const repartidorControls = [
      'disponibilidad',
      'numeroLicencia',
      'tipoLicencia',
      'fechaVenLicencia',
      'zonaAsignada',
      'radioCobertura',
      'vehiculosAsignados',
    ];

    if (this.isRepartidor) {
      repartidorControls.forEach((campo) => {
        this.empleadoForm.get(campo)?.enable();
        this.empleadoForm.get(campo)?.setValidators(Validators.required);
        this.empleadoForm.get(campo)?.updateValueAndValidity();
      });
    } else {
      repartidorControls.forEach((campo) => {
        const control = this.empleadoForm.get(campo);
        control?.clearValidators();
        control?.reset(); // limpia el valor
        control?.disable(); // opcional: evita que se envíe al backend
        control?.updateValueAndValidity();
      });
    }

    this.empleadoForm.updateValueAndValidity();
  }

  setEmpleados(): void {
    this.usuarioService.obtenerUsuarios().subscribe((response: UsuarioModel[]) => {
      const filteredUsers = response.filter((user) => user.nombreRol !== 'CLIENTE');

      filteredUsers.forEach((user: UsuarioModel) => {
        this.empleadoService
          .getEmpleadoPorIdsuario(user.idUsuario)
          .subscribe((empleado: EmpleadoRequestDto) => {
            this.empleados.push({
              usuario: user,
              empleado: empleado,
              expanded: false,
            });
          });
      });
      this.loadingEmpleados = false;
    });
  }

  setRoles(): void {
    this.rolService.getRoles().subscribe((response: Rol[]) => {
      this.roles = response.filter((rol) => rol.nombre !== 'CLIENTE');
    });
  }

  setVehiculos(): void {
    this.vehiculoService.obtenerVehiculos().subscribe((data: Vehiculo[]) => {
      this.vehiculos = data;
    });
  }

  crearNuevoEmpleado(): void {
    this.resetearFormularioCompleto();
    this.modoEdicion = false;
    this.guardandoEmpleado = false;
    this.selectedTabIndex = 0;
  }

  guardarEmpleado(): void {
    if (this.empleadoForm.invalid) {
      this.showSnackbar('Por favor, complete todos los campos requeridos.', 'error-snackbar');
      return;
    }

    this.direccion = {
      idDireccion: 0,
      tipoDireccion: this.empleadoForm.get('tipoDireccion')?.value,
      municipio: this.empleadoForm.get('municipio')?.value,
      departamento: this.empleadoForm.get('departamento')?.value,
      pais: 'Guatemala',
      codigoPostal: this.empleadoForm.get('codigoPostal')?.value,
      referencias: this.empleadoForm.get('referencias')?.value,
      activa: true,
    };

    this.usuarioRequest = {
      idUsuario: 0,
      idPersona: 0,
      idRol: this.empleadoForm.get('idRol')?.value,
      idDireccion: 0,
      nombreUsuario: this.empleadoForm.get('nombreUsuario')?.value,
      contrasena: this.empleadoForm.get('password')?.value,
      nombre: this.empleadoForm.get('nombre')?.value,
      apellido: this.empleadoForm.get('apellido')?.value,
      fechaNacimiento: this.empleadoForm.get('fechaNacimiento')?.value,
      dpi: this.empleadoForm.get('dpi')?.value,
      correo: this.empleadoForm.get('correo')?.value,
      telefono: this.empleadoForm.get('telefono')?.value,
      estado: 'ACTIVO',
    };

    this.empleadoRequest.codigoEmpleado = this.empleadoForm.get('codigoEmpleado')?.value;
    this.empleadoRequest.numeroIgss = this.empleadoForm.get('numeroIgss')?.value;
    this.empleadoRequest.numeroIrtra = this.empleadoForm.get('numeroIrtra')?.value;
    this.empleadoRequest.tipoSangre = this.empleadoForm.get('tipoSangre')?.value;
    this.empleadoRequest.estadoCivil = this.empleadoForm.get('estadoCivil')?.value;
    this.empleadoRequest.numeroDependientes = this.empleadoForm.get('numeroDependientes')?.value;
    this.empleadoRequest.contactoEmergenciaNombre = this.empleadoForm.get(
      'contactoEmergenciaNombre'
    )?.value;
    this.empleadoRequest.contactoEmergenciaTelefono = this.empleadoForm.get(
      'contactoEmergenciaTelefono'
    )?.value;
    this.empleadoRequest.estadoEmpleado = this.empleadoForm.get('estadoEmpleado')?.value;
    this.empleadoRequest.fechaIngreso = this.empleadoForm.get('fechaIngreso')?.value;
    this.empleadoRequest.fechaSalida = this.empleadoForm.get('fechaSalida')?.value;
    if (!this.empleadoForm.get('motivoSalida')?.value) {
      this.empleadoRequest.motivoSalida = null;
    } else {
      this.empleadoRequest.motivoSalida = this.empleadoForm.get('motivoSalida')?.value;
    }

    //contrato
    this.contratoRequest.numeroContrato = this.empleadoForm.get('numeroContrato')?.value;
    this.contratoRequest.tipoContrato = this.empleadoForm.get('tipoContrato')?.value;
    this.contratoRequest.modalidadTrabajo = this.empleadoForm.get('modalidadTrabajo')?.value;
    this.contratoRequest.fechaInicio = this.empleadoForm.get('fechaInicio')?.value;
    this.contratoRequest.fechaFin = this.empleadoForm.get('fechaFin')?.value;
    this.contratoRequest.estadoContrato = this.empleadoForm.get('estadoContrato')?.value;
    this.contratoRequest.salarioBase = this.empleadoForm.get('salarioBase')?.value;
    this.contratoRequest.frecuenciaPago = this.empleadoForm.get('frecuenciaPago')?.value;
    this.contratoRequest.renovacionAutomatica =
      this.empleadoForm.get('renovacionAutomatica')?.value;
    this.contratoRequest.incluyeAguinaldo = this.empleadoForm.get('incluyeAguinaldo')?.value;
    this.contratoRequest.incluyeBono14 = this.empleadoForm.get('incluyeBono14')?.value;
    this.contratoRequest.incluyeVacaciones = this.empleadoForm.get('incluyeVacaciones')?.value;
    this.contratoRequest.incluyeIgss = this.empleadoForm.get('incluyeIgss')?.value;
    //comision
    this.contratoComisionRequest.tipoComision = this.empleadoForm.get('tipoComision')?.value;
    this.contratoComisionRequest.porcentaje = this.empleadoForm.get('porcentaje')?.value;
    this.contratoComisionRequest.montoFijo = this.empleadoForm.get('montoFijo')?.value;
    this.contratoComisionRequest.aplicaDesde = this.empleadoForm.get('fechaDesde')?.value;
    this.contratoComisionRequest.aplicaHasta = this.empleadoForm.get('fechaHasta')?.value;
    this.contratoComisionRequest.activo = this.empleadoForm.get('comisionActiva')?.value;
    this.contratoComisionRequest.minimoEntregasMes =
      this.empleadoForm.get('minimoEntregasMes')?.value;
    this.contratoComisionRequest.maximoEntregasMes =
      this.empleadoForm.get('maximoEntregasMes')?.value;
    this.contratoComisionRequest.factorMultiplicador =
      this.empleadoForm.get('factorMultiplicador')?.value;

    if (this.modoEdicion) {
      this.direccionEditando.codigoPostal = this.empleadoForm.get('codigoPostal')?.value;
      this.direccionEditando.departamento = this.empleadoForm.get('departamento')?.value;
      this.direccionEditando.municipio = this.empleadoForm.get('municipio')?.value;
      this.direccionEditando.tipoDireccion = this.empleadoForm.get('tipoDireccion')?.value;
      this.direccionEditando.referencias = this.empleadoForm.get('referencias')?.value;

      this.direccionService.actualizarDireccion(this.direccionEditando).subscribe({
        next: (direccionActualizada: DireccionModel) => {},
        error: (error) => {
          console.log(error);
        },
      });

      this.usuarioRequest.idUsuario = this.usuarioEditando.idUsuario;
      this.usuarioRequest.idPersona = this.usuarioEditando.idPersona;
      this.usuarioRequest.idDireccion = this.direccionEditando.idDireccion;

      this.usuarioService.actualizarUsuario(this.usuarioRequest).subscribe({
        next: (usuarioActualizado: UsuarioModel) => {
          this.empleadoRequest.idEmpleado = this.empleadoEditando.idEmpleado;
          this.empleadoRequest.idUsuario = this.usuarioEditando.idUsuario;

          this.empleadoService.actualizarEmpleado(this.empleadoRequest).subscribe({
            next: (empleadoActualizado: EmpleadoRequestDto) => {
              this.showSnackbar(
                `Empleado ${this.empleadoRequest.codigoEmpleado} actualizado exitosamente`,
                'success-snackbar'
              );
              this.guardandoEmpleado = false;
              this.cambioProgmatico = true;
              this.selectedTabIndex = 1;
              this.resetearFormularioCompleto();
              this.setEmpleados();
            },
            error: (error) => {
              this.showSnackbar(`Error al actualizar el empleado`, 'error-snackbar');
              console.log(error);
            },
          });
        },
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      this.direccionService.crearDireccion(this.direccion).subscribe({
        next: (direccionCreada: DireccionModel) => {
          this.direccion = direccionCreada;
          this.usuarioRequest.idDireccion = direccionCreada.idDireccion;

          this.registerEmpleado.usuarioRequestDdto = this.usuarioRequest;
          this.registerEmpleado.empleadoRequestDdto = this.empleadoRequest;

          this.usuarioService.crearUsuario(this.registerEmpleado).subscribe({
            next: (empleado: EmpleadoRequestDto) => {
              this.contratoRequest.idEmpleado = empleado.idEmpleado;
              this.contratoService.crearContrato(this.registroContrato).subscribe({
                next: (contratoNuevo: ContratoModel) => {
                  this.showSnackbar(
                    `Empleado ${empleado.codigoEmpleado} creado exitosamente`,
                    'success-snackbar'
                  );
                  this.guardandoEmpleado = false;
                  this.cambioProgmatico = true;
                  this.selectedTabIndex = 1;
                  this.resetearFormularioCompleto();
                  this.setEmpleados();
                },
                error: (error) => {
                  console.log(error);
                },
              });
            },
            error: (error) => {
              console.log(error);
            },
          });
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
  }

  cancelarEdicion(): void {}

  cancelarFormulario(): void {
    this.resetearFormularioCompleto();
    this.guardandoEmpleado = false;
    this.cambioProgmatico = true;
    this.selectedTabIndex = 1;
  }

  resetearFormularioCompleto(): void {
    this.empleadoForm.reset();
    this.empleadoForm.markAsUntouched();
    this.empleadoForm.markAsPristine();
  }

  editarEmpleado(item: UsuarioItem): void {
    console.log(item);
    this.modoEdicion = true;
    this.empleadoEditando = item.empleado;
    this.usuarioEditando = item.usuario;

    this.personaService.getPersonaPorId(item.usuario.idPersona).subscribe({
      next: (persona: PersonaModel) => {
        this.direccionService.obtenerDireccionPorId(persona.idDireccion).subscribe({
          next: (direccion: DireccionModel) => {
            this.direccionEditando = direccion;
            this.onChangeDepartamento(this.direccionEditando.departamento);
            this.empleadoForm.patchValue({
              nombreUsuario: item.usuario.nombreUsuario,
              password: '000000',
              idRol: item.usuario.idRol,
              nombre: item.usuario.nombre,
              apellido: item.usuario.apellido,
              fechaNacimiento: item.usuario.fechaNacimiento,
              dpi: item.usuario.dpi,
              correo: item.usuario.correo,
              telefono: item.usuario.telefono,

              // Dirección
              departamento: this.direccionEditando?.departamento,
              municipio: this.direccionEditando?.municipio,
              codigoPostal: this.direccionEditando?.codigoPostal,
              tipoDireccion: this.direccionEditando?.tipoDireccion,
              referencias: this.direccionEditando?.referencias,

              // Información de Empleado
              codigoEmpleado: item.empleado?.codigoEmpleado,
              numeroIgss: item.empleado?.numeroIgss,
              numeroIrtra: item.empleado?.numeroIrtra,
              tipoSangre: item.empleado?.tipoSangre,
              estadoCivil: item.empleado?.estadoCivil,
              numeroDependientes: item.empleado?.numeroDependientes,
              contactoEmergenciaNombre: item.empleado?.contactoEmergenciaNombre,
              contactoEmergenciaTelefono: item.empleado?.contactoEmergenciaTelefono,
              estadoEmpleado: item.empleado?.estadoEmpleado,
              fechaIngreso: item.empleado?.fechaIngreso,
              fechaSalida: item.empleado?.fechaSalida,
              motivoSalida: item.empleado?.motivoSalida,

              // Contrato inicial con datos falsos
              numeroContrato: '000',
              tipoContrato: 'N/A',
              modalidadTrabajo: 'N/A',
              fechaInicio: new Date().toISOString().substring(0, 10),
              fechaFin: '',
              estadoContrato: 'N/A',
              salarioBase: 1,
              frecuenciaPago: 'N/A',
              renovacionAutomatica: false,
              incluyeAguinaldo: false,
              incluyeBono14: false,
              incluyeVacaciones: false,
              incluyeIgss: false,

              // Comisiones con datos falsos
              tipoComision: 'N/A',
              porcentaje: 0,
              montoFijo: 0,
              fechaDesde: new Date().toISOString().substring(0, 10),
              fechaHasta: new Date().toISOString().substring(0, 10),
              comisionActiva: false,
              minimoEntregasMes: 1,
              maximoEntregasMes: 1,
              factorMultiplicador: 0.1,
            });
            this.logInvalidControls(this.empleadoForm);
          },
          error: (error) => {
            console.log(error);
          },
        });
      },
      error: (error) => {
        console.log(error);
      },
    });

    setTimeout(() => {
      this.selectedTabIndex = 0;
    }, 0);
  }


  suspenderEmpleado(item: UsuarioItem): void {
    this.actualizarEstado(item, 'SUSPENDIDO');
  }

  darDeAltaEmpleado(item: UsuarioItem): void {
    this.actualizarEstado(item, 'ACTIVO');
  }

  private actualizarEstado(item: UsuarioItem, estado: string): void {
    item.usuario.estado = estado;

    const usuarioRequest: UsuarioRequestDto = {
      idUsuario: item.usuario.idUsuario,
      idPersona: item.usuario.idPersona,
      idRol: item.usuario.idRol,
      idDireccion: 0,
      nombreUsuario: item.usuario.nombreUsuario,
      contrasena: '000000',
      nombre: item.usuario.nombre,
      apellido: item.usuario.apellido,
      fechaNacimiento: item.usuario.fechaNacimiento,
      dpi: item.usuario.dpi,
      correo: item.usuario.correo,
      telefono: item.usuario.telefono,
      estado: item.usuario.estado,
    };
    this.usuarioService.actualizarUsuario(usuarioRequest).subscribe({
      next: (usuarioActualizado: UsuarioModel) => {
        this.showSnackbar(
          `Empleado ${item.empleado.codigoEmpleado} suspendido exitosamente`,
          'success-snackbar'
        );
        this.cambioProgmatico = true;
        this.selectedTabIndex = 1;
        this.resetearFormularioCompleto();
        this.setEmpleados();
      },
      error: (error) => {
        this.showSnackbar(`Error al suspender el empleado`, 'error-snackbar');
        console.log(error);
      },
    });
  }

  toggleExpanded(item: UsuarioItem): void {
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
