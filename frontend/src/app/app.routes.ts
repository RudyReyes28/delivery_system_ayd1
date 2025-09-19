import { Routes } from '@angular/router';
import { IniciarSesion } from './login/iniciar-sesion/iniciar-sesion';
import { CrearUsuario } from './login/crear-usuario/crear-usuario';
import { RecuperarCuenta } from './login/recuperar-cuenta/recuperar-cuenta';
import { InfoEmpresa } from './info-empresa/info-empresa';
import { InfoSucursal } from './info-sucursal/info-sucursal';

export const routes: Routes = [
    { 
        path: 'login', component: IniciarSesion 
    },
    { 
        path: '', redirectTo: '/login', pathMatch: 'full' 
    },
    {
        path: "crear-usuario", component: CrearUsuario
    },
    {
        path: "recuperar-cuenta", component: RecuperarCuenta
    },
    {
        path: "info-empresa", component: InfoEmpresa
    },
    {
        path: "info-sucursal", component: InfoSucursal
    }
];
