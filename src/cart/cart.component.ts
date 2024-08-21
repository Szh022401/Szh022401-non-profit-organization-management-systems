import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CurrencyPipe, NgForOf, NgIf, NgOptimizedImage } from '@angular/common';
import { Need } from '../models/Need';
import { CupboardService } from '../service/cupboard.service';
import { FundingBasketService } from '../service/funding-basket.service';
import {Observable} from "rxjs";
import {AuthService } from '../service/auth.service'

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    NgOptimizedImage,
    CurrencyPipe,
  ],
  styleUrls: ['./cart.component.css'],
  template: `
    <section class="shopping-cart">
      <h2>Cart</h2>
      <div *ngIf="basketNeeds && basketNeeds.length > 0; else emptyCart">
        <h3>Your Current Basket</h3>
        <ul>
          <li *ngFor="let item of basketNeeds; let i = index">
            {{ item.name }} - {{ item.quantity }} units
            <button (click)="increaseQuantity(i)">+</button>
            <button (click)="decreaseQuantity(i)">-</button>
            <button (click)="removeItem(i)">Remove</button>
          </li>
        </ul>
      </div>
      <div class="total-price">
        <h3>Total Price: {{ calculateTotalPrice() | currency }}</h3>
      </div>
      <button class="checkout-button" (click)="checkout()">Checkout</button>
      <ng-template #emptyCart>
        <p>Your cart is empty.</p>
      </ng-template>
    </section>
  `,
})
export class CartComponent implements OnInit {
  basketNeeds: Need[] = [];
  userid:number |null = null;
  constructor(
    private cupboardService: CupboardService,
    private router: Router,
    private fundingBasketService: FundingBasketService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
) {}

  ngOnInit(): void {
    this.userid = this.authService.getId();
    this.fundingBasketService.getFundingBasketByHelperId(this.userid).subscribe({
      next: (data: Need[]) => {
        this.basketNeeds = data;
        console.log('fundingBasketService', this.basketNeeds);
        this.cdr.detectChanges();
      },
      error: (error: any) => {
        console.error('Error fetching cart data:', error);
      }
    });
    this.cupboardService.getCart().subscribe({
      next: (data: Need[]) => {
        this.basketNeeds = data;
        console.log('cupboardService', this.basketNeeds);
        this.cdr.detectChanges();
      },
      error: (error: any) => {
        console.error('Error fetching cart data:', error);
      }
    });
    console.log('basketNeeds',this.basketNeeds);
  }




  async increaseQuantity(index: number) {
    const cartItem = this.basketNeeds[index];
    const availableStock = await this.cupboardService.getStock(cartItem.id);

    console.log('inc', availableStock);
    if (cartItem.quantity < availableStock) {
      cartItem.quantity += 1;
      this.fundingBasketService.addNeedToFundingBasket(this.userid!,cartItem).subscribe(() => {
        this.cdr.detectChanges();
      });
    } else {
      console.log(`${cartItem.name} quantity cannot exceed the available stock of ${availableStock}.`);
    }
  }


  decreaseQuantity(index: number) {
    const cartItem = this.basketNeeds[index];
    if (cartItem.quantity > 1) {
      cartItem.quantity -= 1;
      this.fundingBasketService.addNeedToFundingBasket(this.userid!,cartItem).subscribe(() => {
        this.cdr.detectChanges();
      });
    } else {
      this.removeItem(index);
    }
  }


  removeItem(index: number) {
    const cartItem = this.basketNeeds[index];
    this.fundingBasketService.removeNeedFromFundingBasket(this.userid!,cartItem.id).subscribe(() => {
      this.basketNeeds.splice(index, 1);  // 从数组中移除
      this.cdr.detectChanges();  // 手动触发视图更新
    });
  }

  calculateTotalPrice(): number {
    return this.basketNeeds.reduce((total, need) => total + (need.price * need.quantity), 0);
  }

  checkout() {
    this.router.navigate(['/details']);
  }
}
