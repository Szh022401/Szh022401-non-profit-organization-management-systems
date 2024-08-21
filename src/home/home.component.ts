import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import { CupboardComponent } from '../cupboard/cupboard.component';
import { Need } from '../models/Need';
import {CupboardService} from '../service/cupboard.service'
import {CartComponent} from "../cart/cart.component";
import {FundingBasketService} from "../service/funding-basket.service";
import {Observable, of} from "rxjs";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CupboardComponent, CartComponent],
  template: `
    <section>
      <form>
        <input type="text" placeholder="Filter" #filter>
        <button class="primary" type="button" (click)="filterResults(filter.value)">Search</button>
      </form>
      <button class="cart-toggle" (click)="toggleCart()">🛒</button>
    </section>
    <section class="results">

      <app-cupboard
        *ngFor="let need of filteredNeedList"
        [need]="need"></app-cupboard>
      <div class="cart-container" [class.hidden]="!isCartVisible">
        <app-cart></app-cart>
      </div>


    </section>
  `,
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  cupboard: Need[] =[];
  needs: Observable<Need[]> = of([])
  cupboardService : CupboardService = inject(CupboardService);
  filteredNeedList: Need[] = [];
  isCartVisible = false;

  constructor( private fundingBasketService: FundingBasketService) {
    this.cupboardService.getAllNeed().then((cupboard: Need[]) => {
      this.cupboard = cupboard;
      this.filteredNeedList = cupboard;
    });
  }
  filterResults(text: string) {
    if (!text) {
      this.filteredNeedList = this.cupboard;
      return;
    }

    this.filteredNeedList = this.cupboard.filter(
      need => need?.name.toLowerCase().includes(text.toLowerCase())
    );
  }
  toggleCart() {
    this.fundingBasketService.getFundingBasketByHelperId(2).subscribe({
      next: (data: Need[]) => {
        console.log('Emitted data:', data); // Log the actual emitted values
        this.cupboard = data;
        this.isCartVisible = !this.isCartVisible; // Toggle visibility after data is fetched
      },
      error: (error: any) => {
        console.error('Error fetching data:', error);
      }
    });
  }
}
