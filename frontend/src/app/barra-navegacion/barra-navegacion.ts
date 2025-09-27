import { Component, OnInit } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { NavigationEnd, Router } from '@angular/router';

// Importaciones de Angular Material
import { MatDivider, MatListItem, MatNavList } from '@angular/material/list';
import { MatDrawer, MatDrawerContainer } from '@angular/material/sidenav';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

// Importando Servicios
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { filter } from 'rxjs';

interface MenuItem {
  icon: string;
  label: string;
  route: string;
  roles?: string[];
  roleIds?: number[];
}

@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  templateUrl: './barra-navegacion.html',
  styleUrl: './barra-navegacion.scss',
  imports: [
    CommonModule,
    MatDrawerContainer,
    MatDrawer,
    MatNavList,
    MatIcon,
    MatDivider,
    MatListItem,
    MatToolbar,
    RouterModule,
  ],
})
export class BarraNavegacion implements OnInit {
  private allMenuItems: MenuItem[] = [
    {
      icon: 'local_shipping',
      label: 'General',
      route: '/',
    },
    {
      icon: 'account_circle',
      label: 'Iniciar Sesión',
      route: '/login',
    },
    {
      icon: 'person',
      label: 'Crear Usuario',
      route: '/crear-usuario',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'group',
      label: 'Gestión Empleados',
      route: '/gestion-empleados',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'density_small',
      label: 'Gestión de Contratos',
      route: '/gestion-contratos',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'business_center',
      label: 'Gestión Empresas',
      route: '/empresa',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'store',
      label: 'Gestión Sucursales',
      route: '/sucursal',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'grade',
      label: 'Fidelización',
      route: '/fidelizacion',
      roles: ['ADMINISTRADOR'],
    },
    {
      icon: 'settings',
      label: 'General',
      route: 'general-sucursal',
      roles: ['SUCURSAL'],
    },
    {
      icon: 'local_shipping',
      label: 'Gestión Guías',
      route: '/guia-sucursal',
      roles: ['SUCURSAL'],
    },
    {
      icon: 'local_shipping',
      label: 'Gestión Guías Coordinador',
      route: '/guia-repartidor',
      roles: ['COORDINADOR_OPERACIONES'],
    },
    {
      icon: 'logout',
      label: 'Cerrar Sesión',
      route: '/logout',
      roles: ['ADMINISTRADOR', 'SUCURSAL'],
    },
  ];

  menuItems: MenuItem[] = [];
  idUsuarioActual: number | null = null;
  nombreRolActual: string | null = null;
  private routerSubscription: any;

  constructor(
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    this.routerSubscription = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.cargarDatosUsuarioYMenu();
      });
  }

  ngOnInit() {
    this.cargarDatosUsuarioYMenu();
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  private cargarDatosUsuarioYMenu() {
    const idUsuario = sessionStorage.getItem('idUsuario');
    const nombreRol = sessionStorage.getItem('nombreRol');

    if (idUsuario) {
      this.idUsuarioActual = Number(idUsuario);
      this.nombreRolActual = nombreRol;
      this.filtrarMenu();
    } else {
      this.menuItems = this.allMenuItems.filter((item) => !item.roles && !item.roleIds);
    }
  }

  private filtrarMenu() {
    this.menuItems = this.allMenuItems.filter((item) => {
      if (!item.roles && !item.roleIds) {
        return true;
      }

      if (item.roles && this.nombreRolActual) {
        if (item.roles.includes(this.nombreRolActual)) {
          return true;
        }
      }
      return false;
    });
  }

  elementoMenuSeleccionado(item: MenuItem) {
    if (item.route === '/logout') {
      this.cerrarSesion();
    } else {
      this.router.navigate([item.route]);
    }
  }

  quitarVerificacionDosPasos() {
    const idUsuario = sessionStorage.getItem('idUsuario');

    if (idUsuario) {
      this.authService.desactivarAutetificacion(Number(idUsuario)).subscribe({
        next: (response) => {
          this.mostrarMensaje(response.message, 'success-snackbar');
        },
        error: (error) => {
          this.mostrarMensaje(
            error.error?.message || 'Error al cambiar Autentificación',
            'error-snackbar'
          );
        },
      });
    } else {
      this.mostrarMensaje('No existe una Sesión Activa', 'error-snackbar');
    }
  }

  private cerrarSesion() {
    if (this.idUsuarioActual !== null && this.nombreRolActual !== null) {
      try {
        sessionStorage.clear();

        this.idUsuarioActual = null;
        this.nombreRolActual = null;

        this.menuItems = this.allMenuItems.filter((item) => !item.roles && !item.roleIds);

        this.mostrarMensaje('Sesión cerrada correctamente', 'success-snackbar');
        this.router.navigate(['/']);
      } catch (error) {
        this.mostrarMensaje('Error al cerrar sesión', 'error-snackbar');
      }
    } else {
      this.mostrarMensaje('No hay usuario registrado', 'info-snackbar');
    }
  }

  private mostrarMensaje(mensaje: string, tipo: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: [tipo],
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

  // Método para refrescar el menú (útil si el rol cambia durante la sesión)
  refreshMenu() {
    this.cargarDatosUsuarioYMenu();
  }

  // Método helper para verificar si el usuario tiene un rol específico
  hasRole(roleName: string): boolean {
    return this.nombreRolActual === roleName;
  }

  // Método helper para verificar si el usuario tiene uno de varios roles
  hasAnyRole(roleNames: string[]): boolean {
    return this.nombreRolActual ? roleNames.includes(this.nombreRolActual) : false;
  }
}
