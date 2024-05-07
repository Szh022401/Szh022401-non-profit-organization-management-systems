import { Component } from '@angular/core';
import {HomeComponent} from "../home/home.component";
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    HomeComponent,
    RouterModule,
  ],
  template: `
    <main>
      <a [routerLink]="['/']">
        <header class="ufund-name">
          <img class="ufund-logo" src="assets/ufund-name.png" width="200" height="70" alt="logo" aria-hidden="true" />
        </header>
      </a>

      <section class="content">
        <router-outlet></router-outlet>
      </section>
    </main>
  `,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'xixi';
}
