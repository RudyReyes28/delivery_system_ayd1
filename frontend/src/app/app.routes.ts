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


export const routes: Routes = [
    {
        path: 'login', component: IniciarSesion, title: "Iniciar Sesión"
    },
    {
        path: '', redirectTo: '/login', pathMatch: 'full'
    },
    {
        path: "crear-usuario", component: CrearUsuario, title: "Crear Usuario"
    },
    {
        path: "recuperar-cuenta", component: RecuperarCuenta, title: "Recuperar Cuenta"
    },
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
        path: "general-sucursal", component: GeneralSucursal, title: "Información General"
    },
    {
        path: "gestion-empleados", component: Empleados, title: "Empleados"
    },
    {
        path: "gestion-contratos", component: Contratos, title: "Contratos"
    },
    {
        path: "guia-sucursal",  component: GuiaSucursal, title: "Guia"
    },
    {
        path: "", component: GuiaCliente, title: "Guía"
    },
    {
        path: "guia-repartidor", component: GuiaCoordenador, title: "Gestión Guías Coordenador"
    }
];
