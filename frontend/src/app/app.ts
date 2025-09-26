import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BarraNavegacion } from "./barra-navegacion/barra-navegacion";

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    BarraNavegacion 
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
}
