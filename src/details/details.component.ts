import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CupboardService } from '../service/cupboard.service';
import { Need } from '../models/Need';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import {Observable} from "rxjs";

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  template: `
    <article>
      <!-- Shipping Address Section -->
      <section class="shipping-address">
        <h2 class="section-heading">1. Shipping Address</h2>
        <form [formGroup]="addressForm" (submit)="submitAddress()">
          <label for="address-line1">Address Line 1</label>
          <input id="address-line1" type="text" formControlName="addressLine1">

          <label for="address-line2">Address Line 2 (Optional)</label>
          <input id="address-line2" type="text" formControlName="addressLine2">

          <label for="city">City</label>
          <input id="city" type="text" formControlName="city">

          <label for="state">State/Province/Region</label>
          <input id="state" type="text" formControlName="state">

          <label for="postal-code">ZIP/Postal Code</label>
          <input id="postal-code" type="text" formControlName="postalCode">

          <label for="country">Country</label>
          <input id="country" type="text" formControlName="country">
        </form>
      </section>

      <!-- Payment Method Section -->
      <section class="payment-method">
        <h2 class="section-heading">2. Payment Method</h2>
        <form [formGroup]="paymentForm" (submit)="submitPayment()">
          <label for="card-number">Card Number</label>
          <input id="card-number" type="text" formControlName="cardNumber">

          <label for="expiration">Expiration Date</label>
          <input id="expiration" type="text" formControlName="expiration">

          <label for="cvv">CVV</label>
          <input id="cvv" type="text" formControlName="cvv">
        </form>
      </section>

      <!-- Cart Items and Total Price Section -->
      <h2 class="listing-heading">3. Review Items and Shipping</h2>
      <section class="cart-items">
        <h2 class="section-heading">Items in your cart</h2>
        <ul>
          <li *ngFor="let item of cartItems" class="cart-item">
            <img [src]="item.image" alt="Image of {{ item.name }}" class="cart-item-photo"/>
            <div class="cart-item-details">
              <h3>{{ item.name }}</h3>
              <p>Quantity: {{ item.quantity }}</p>
              <p>Price: {{ item.price | currency }}</p>
            </div>
          </li>
        </ul>
      </section>

      <section class="total-price">
        <h3>Total Price</h3>
        <p>{{ getTotalPrice() | currency }}</p>
      </section>

      <!-- Submit Button -->
      <section class="order-actions">
        <button type="submit" class="primary" (click)="submitOrder()">Place Your Order and Pay</button>
      </section>
    </article>
  `,
  styleUrls: ['./details.component.css'],
})
export class DetailsComponent {
  // Inject ActivatedRoute to access route parameters
  route: ActivatedRoute = inject(ActivatedRoute);
  // Inject CupboardService to fetch the cart and item details
  cupboardService = inject(CupboardService);

  // Declare cartItems to hold the items in the cart
  cartItems: Need[] = [];

  // Declare need to hold the specific item's details
  need: Need | undefined;

  // Form group to handle address form inputs
  addressForm = new FormGroup({
    addressLine1: new FormControl(''),
    addressLine2: new FormControl(''),
    city: new FormControl(''),
    state: new FormControl(''),
    postalCode: new FormControl(''),
    country: new FormControl('')
  });

  // Form group to handle payment form inputs
  paymentForm = new FormGroup({
    cardNumber: new FormControl(''),
    expiration: new FormControl(''),
    cvv: new FormControl('')
  });

  constructor() {
    this.cupboardService.getCart().subscribe({
      next: (data: Need[]) => {
        this.cartItems = data;
      },
      error: (error: any) => {
        console.error('Error fetching cart items:', error);
      }
    });

  }
   getTotalPrice(): number {
     return this.cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
   }


  submitPayment() {
    this.cupboardService.submitPayment({
      cardNumber: this.paymentForm.value.cardNumber ?? '',
      expiration: this.paymentForm.value.expiration ?? '',
      cvv: this.paymentForm.value.cvv ?? ''
    });
  }


  submitAddress() {
    console.log('Address submitted:', this.addressForm.value);
  }


  submitOrder() {
    this.submitAddress();
    this.submitPayment();
    console.log('Order submitted!');
  }
}
