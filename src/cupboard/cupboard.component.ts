import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Need } from '../models/Need';
import { RouterModule } from '@angular/router';
import { CupboardService } from '../service/cupboard.service';
import { FundingBasketService } from '../service/funding-basket.service';
import {Observable} from "rxjs";
import {AuthService } from '../service/auth.service'

@Component({
  selector: 'app-cupboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="cupboard-item">
      <img
        class="cupboard-photo"
        [src]="need.image"
        alt="Photo of {{ need.name }}"
        crossorigin="anonymous"
      />
      <h2 class="cupboard-heading">{{ need.name }}</h2>
      <p class="cupboard-price">Price: $ {{ need.price }}</p>
      <p class="cupboard-quantity">Quantity: {{ need.quantity }}</p>
      <p class="cupboard-status">Status: {{ need.status }}</p>
      <button
        (click)="addToCart()"
        [disabled]="isAddedToCart || need.quantity <= 0"
        [ngClass]="{'out-of-stock': need.quantity <= 0, 'added-to-cart': isAddedToCart}">
        {{ isAddedToCart ? 'Added to Cart' : 'Add to Cart' }}
      </button>
    </section>
  `,
  styleUrls: ['./cupboard.component.css']
})
export class CupboardComponent {
  @Input() need!: Need;
  isAddedToCart = false;
  basketNeeds: Need[] = [];
  test: Need[] = [];
  userid:number |null = null;
  constructor(
    private cupboardService: CupboardService,
    private fundingBasketService: FundingBasketService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userid = this.authService.getId();
    this.fundingBasketService.getFundingBasketByHelperId(this.userid).subscribe({
      next: (data: Need[]) => {
        this.basketNeeds = data;
        console.log('Funding Basket:', this.basketNeeds);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error fetching funding basket:', error);
      }
    });
  }

  addToCart() {
    if (this.need.quantity > 0) {
      this.cupboardService.addToCart(this.need).subscribe({
        next: (updatedItems: Need[]) => {
          this.basketNeeds = updatedItems;
          this.isAddedToCart = false;
          this.cdr.detectChanges();
          this.test = this.basketNeeds;
          console.log('test1',this.test);
        },
        error: (error: any) => {
          console.error('Error adding to cart:', error);
        }
      });
    } else {
      console.log(`${this.need.name} is out of stock and cannot be added to the cart.`);

    }
  }
}
