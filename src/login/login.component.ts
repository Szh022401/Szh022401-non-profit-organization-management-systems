import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router  } from '@angular/router';
import {AuthService} from "../service/auth.service";
import {response} from "express";
import { RouterModule } from '@angular/router';
import {FundingBasketService} from "../service/funding-basket.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,RouterModule],
  template: `
    <body>

    <div class="login-box">
      <form (ngSubmit)="onSubmit()">
        <input type="checkbox" class="input-check" id="input-check" [(ngModel)]="isChecked" name="input-check">
        <label for="input-check" class="toggle">
          <span class="text off">off</span>
          <span class="text on">on</span>
        </label>
        <div class="light"></div>

        <h2>Login</h2>
        <div class="input-box">
        <span class="icon">
          <ion-icon name="mail"></ion-icon>
        </span>
          <input type="userName" required [(ngModel)]="userName" name="userName">
          <label>User Id</label>
          <div class="input-line"></div>
        </div>
        <div class="input-box">
        <span class="icon">
          <ion-icon name="lock-closed"></ion-icon>
        </span>
          <input type="password" required [(ngModel)]="password" name="password">
          <label>Password</label>
          <div class="input-line"></div>
        </div>
        <div class="remember-forgot">
          <label><input type="checkbox" [(ngModel)]="rememberMe" name="rememberMe"> Remember me</label>
          <a href="#">Forgot Password?</a>
        </div>
        <button type="submit">Login</button>
        <div class="register-link">
          <p>Don't have an account? <a [routerLink]="['/register']">Register</a></p>
        </div>
      </form>
    </div>
    </body>

  `,
  styleUrls: ['./login.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginComponent {
  isChecked = false;
  userName = '';
  password = '';
  rememberMe = false;
  constructor(private router: Router,private authService:AuthService,private fundingBasketService :FundingBasketService) {}
  onSubmit() {
    this.authService.login(this.userName).subscribe({
      next:(user)=>{
        console.log('User login successfully:', user);

        const userId = user.id;
        this.authService.setId(userId);
        this.router.navigate(['/']);
      },error:(error)=>{
        console.error('login failed:', error);
        alert(' Please try again.');
      }
    })
  }

}
