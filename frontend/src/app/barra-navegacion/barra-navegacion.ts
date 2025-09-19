import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';

// Importaciones de Angular Material
import { MatDivider, MatListItem, MatNavList } from '@angular/material/list';
import { MatDrawer, MatDrawerContainer } from '@angular/material/sidenav';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

// Importando Servicios
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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
export class BarraNavegacion {
  menuItems = [
    { icon: 'home', label: 'Inicio', route: '/login' },
    { icon: 'person', label: 'Crear Usuario', route: '/crear-usuario' },
    { icon: 'business_center', label: 'Gestión Empresas', route: '/info-empresa' },
    { icon: 'store', label: 'Gestión Surcusales', route: '/info-sucursal' },
    //{ icon: 'help', label: 'Ayuda', route: '/help' },
    { icon: 'logout', label: 'Cerrar Sesión', route: '/logout' }
  ];

  constructor(private router: Router, private authService: AuthService, private snackBar: MatSnackBar) { }

  onMenuItemClick(item: any) {
    console.log('Navegando a:', item.route);
    this.router.navigate([item.route]);
  }

  quitarVerificacionDosPasos() {
    const idUsuario = sessionStorage.getItem('idUsuario');
    
    if (idUsuario) {
      this.authService.desactivarAutetificacion(Number(idUsuario)).subscribe({
        next: (response) => {
          this.mostrarMensaje(response.message, "success-snackbar");
        },
        error: (error) => {
          this.mostrarMensaje("Error al cambiar Autetificación", "error-snackbar");
        }
      });
    } else {
      console.error('No se encontró ID de usuario en sessionStorage');
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
}
