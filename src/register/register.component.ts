import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import {Helper} from "../models/Helper";
import {FundingBasketService} from "../service/funding-basket.service";

@Component({
  selector: 'app-register',
  standalone: true, // 保持组件独立
  template: `
    <div class="register-box">
      <form (ngSubmit)="onSubmit()">
        <h2>Register</h2>
        <div class="input-box">
          <span class="icon">
            <span class="material-icons">Username</span>
          </span>
          <input type="text" required [(ngModel)]="username" name="username">
          <div class="input-line"></div>
        </div>
        <div class="input-box">
        </div>
        <button type="submit">Register</button>
        <div class="login-link">
          <p>Already have an account? <a [routerLink]="['/login']">Login</a></p>
        </div>
      </form>
    </div>
  `,
  styleUrls: ['register.component.css'],
  imports: [FormsModule, RouterModule]
})
export class RegisterComponent {
  username = '';

  constructor(private authService: AuthService, private router: Router,private  fundingBasketService:FundingBasketService) {}

  onSubmit() {
    this.authService.register(this.username).subscribe({
      next: (newUser) => {
        console.log('User registered successfully:', newUser);
        const id = newUser.id;
        console.log('User ID:', id);
        const helper: Helper = {
          username: newUser.username,
          fundingBasket: []
        };
        this.fundingBasketService.createFundingBasket(helper).subscribe({
          next:(createdNeeds)=>{
            console.log('New funding basket created for registered user:', createdNeeds);
          },
          error: (error) => console.error('Error creating funding basket:', error)
        });

        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Registration failed:', error);
        alert('Registration failed. Please try again.');
      }
    });

  }
}
