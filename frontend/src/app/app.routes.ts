import { Routes } from '@angular/router';
import { IniciarSesion } from './auth/iniciar-sesion/iniciar-sesion';
import { CrearUsuario } from './auth/crear-usuario/crear-usuario';
import { RecuperarCuenta } from './auth/recuperar-cuenta/recuperar-cuenta';
import { Empresa } from './views_admin/empresa/empresa';
import { Sucursal } from './views_admin/sucursal/sucursal';
import { Fidelizacion } from './views_admin/fidelizacion/fidelizacion';
import { GeneralSucursal } from './views_sucursal/general-sucursal/general-sucursal';
import { Empleados } from './views_admin/empleados/empleados';
import { Contratos } from './views_admin/contratos/contratos';
import { GuiaSucursal } from './views_sucursal/guia-sucursal/guia-sucursal';
import { GuiaCliente } from './views_cliente/guia-cliente/guia-cliente';
import { GuiaCoordenador } from './guia-coordenador/guia-coordenador';
import { Liquidacion } from './liquidacion/liquidacion';
import { CancelacionGuiasComponent } from './views_sucursal/cancelacion-guias/cancelacion-guias.component';
import { SolicitudesCancelacionComponent } from './views_coordinador/solicitudes-cancelacion/solicitudes-cancelacion.component';
import { CancelacionesClienteComponent } from './views_coordinador/cancelaciones-cliente/cancelaciones-cliente.component';

//repartidor
import { GestionPedidos } from './views_repartidor/gestion-pedidos/gestion-pedidos';
import { SeguimientoPedidos } from './views_repartidor/seguimiento-pedidos/seguimiento-pedidos';
import { CancelacionClienteComponent } from './views_repartidor/cancelacion-cliente/cancelacion-cliente.component';

export const routes: Routes = [
    {
        path: 'login', component: IniciarSesion, title: "Iniciar Sesión"
    },
    {
        path: "crear-usuario", component: CrearUsuario, title: "Crear Usuario"
    },
    {
        path: "recuperar-cuenta", component: RecuperarCuenta, title: "Recuperar Cuenta"
    },
    // Rutas del Administrador
    {
        path: "empresa", component: Empresa, title: "Gestión Empresas"
    },
    {
        path: "sucursal", component: Sucursal, title: "Gestión Surcusales"
    },
    {
        path: "fidelizacion", component: Fidelizacion, title: "Fidelización"
    },
    {
        path: "gestion-empleados", component: Empleados, title: "Empleados"
    },
    {
        path: "gestion-contratos", component: Contratos, title: "Contratos"
    },
    {
        path: "periodos-liquidacion", component: Liquidacion, title: "Periodos de Liquidación"
    },
    {
        path: "general-sucursal", component: GeneralSucursal, title: "Información General"
    },
    {
        path: "gestion-empleados", component: Empleados, title: "Empleados"
    },
    {
        path: "gestion-contratos", component: Contratos, title: "Contratos"
    },
    {
        path: "guia-sucursal", component: GuiaSucursal, title: "Guia"
    },
    {
        path: "cancelacion-guias", component: CancelacionGuiasComponent, title: "Cancelación de Guías"
    },
    // Rutas del Coordinador
    {
        path: "solicitudes-cancelacion", component: SolicitudesCancelacionComponent, title: "Solicitudes de Cancelación"
    },
    {
        path: "cancelaciones-cliente", component: CancelacionesClienteComponent, title: "Cancelaciones de Cliente"
    },
    //rutas repartidor
    {
      path: "gestion-pedidos", component: GestionPedidos, title: "Gestión Pedidos"
    },
    {
      path: "seguimiento-pedidos", component: SeguimientoPedidos, title: "Seguimiento Pedidos"
    },
    {
      path: "cancelacion-cliente", component: CancelacionClienteComponent, title: "Cancelación Cliente"
    },
    {
      path: "cancelacion-cliente/:id", component: CancelacionClienteComponent, title: "Cancelación Cliente"
    },
    {
        path: "", component: GuiaCliente, title: "Guía"
    },
    {
        path: "guia-repartidor", component: GuiaCoordenador, title: "Gestión Guías Coordenador"
    },
    {
        path: "admin-liquidacion", component: Liquidacion, title: "Liquidación"
    }
];
