import { Component } from '@angular/core';
import {HomeComponent} from "../home/home.component";
import {Router, RouterModule} from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    HomeComponent,
    RouterModule,
    CommonModule,
  ],
  template: `
    <header *ngIf="!isLoginPage">
      <a [routerLink]="['/']">
        <img class="ufund-logo" src="assets/ufund-name.png" width="200" height="70" alt="logo" aria-hidden="true" />
      </a>
    </header>

    <main>
      <section class="content">
        <router-outlet></router-outlet>
      </section>
    </main>
  `,
  styleUrl: './app.component.css'
})
export class AppComponent {
  isLoginPage: boolean = false;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {

      this.isLoginPage = this.router.url === '/login';
    });
  }
}
