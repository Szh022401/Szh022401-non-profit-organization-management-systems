import { Injectable } from '@angular/core';
import {Need} from "../models/Need";
import { FundingBasketService } from './funding-basket.service';
import {update} from "@angular-devkit/build-angular/src/tools/esbuild/angular/compilation/parallel-worker";
import { ChangeDetectorRef } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {AuthService} from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class CupboardService {
  private needs: Need[] = [];
  private cartItemsSubject: BehaviorSubject<Need[]> = new BehaviorSubject<Need[]>([]);
  cartItems$: Observable<Need[]> = this.cartItemsSubject.asObservable();
  private useId:number | null = null;

  // cupboard: Need[] = [
  //   {
  //     id: 1,
  //     name: 'Test1',
  //     price: 1,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/1.webp'
  //   },
  //   {
  //     id: 2,
  //     name: 'Test2',
  //     price: 2,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/2.webp'
  //   },
  //   {
  //     id: 3,
  //     name: 'Test3',
  //     price: 3,
  //     quantity: 3,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/3 .webp'
  //   },
  //   {
  //     id: 4,
  //     name: 'Test4',
  //     price: 4,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/4.webp'
  //   },
  //   {
  //     id: 5,
  //     name: 'Test5',
  //     price: 5,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/5.webp'
  //   },
  //   {
  //     id: 6,
  //     name: 'Test6',
  //     price: 6,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/6.webp'
  //   },
  //   {
  //     id: 7,
  //     name: 'Test7',
  //     price: 6,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/7.webp'
  //   },
  //   {
  //     id: 8,
  //     name: 'Test6',
  //     price: 6,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/8 .webp'
  //   },
  //   {
  //     id: 9,
  //     name: 'Test6',
  //     price: 6,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/9.webp'
  //   },
  //   {
  //     id: 10,
  //     name: 'Test6',
  //     price: 6,
  //     quantity: 10,
  //     type: 'goods',
  //     status: 'set',
  //     image: 'assets/10.png'
  //   },
  //
  // ];
  url = 'http://localhost:3000/cupboard';

  async getAllNeed(): Promise<Need[]> {
    try{
      const  response = await fetch(this.url);
      const data = await response.json();
      this.needs = data ?? [];
      console.log('Fetched needs:', this.needs);
      return this.needs;
    }catch (error){
      console.error('Error fetching needs:', error);
      return [];
    }
  }
  async getNeedById(id: number): Promise<Need | undefined> {
    const need = this.needs.find((item) => String(item.id) === String(id));
    console.log('getNeed',need);
    if (need) {
      return need;
    } else {
      console.log('No need found for id:', id);
      return undefined;
    }
  }

  addToCart(need: Need) {
    console.log('Before adding, current cart items:', this.cartItemsSubject.value);

    let cartItem = this.cartItemsSubject.value.find(item => String(item.id) === String(need.id));

    console.log('Need ID (type):', need.id, typeof need.id);
    console.log('Cart item IDs (types):', this.cartItemsSubject.value.map(item => ({ id: item.id, type: typeof item.id })));
    console.log('getID', cartItem);
    console.log('Checking IDs:', need.id, cartItem ? cartItem.id : 'Not found');
    if (cartItem) {
      cartItem.quantity += 1;
      console.log("++");
    } else {
      cartItem = { ...need, quantity: 1 };
      this.cartItemsSubject.next([...this.cartItemsSubject.value, cartItem]);
      console.log("+1");
    }
    const updatedCart = this.cartItemsSubject.value.map(item =>
      item.id === cartItem.id ? cartItem : item
    );

    if (!cartItem.id) {
      updatedCart.push(cartItem);
    }
    this.cartItemsSubject.next(updatedCart);
    this.useId = this.authService.getId();
    // Save the updated cart item to the backend
    this.fundingBasketService.addNeedToFundingBasket(this.useId!,cartItem).subscribe({
      next: (updatedItems: Need[]) => {
        console.log('Updated Items1:', updatedItems);
        this.cartItemsSubject.next(updatedItems);
      },
      error: (error) => {
        console.error('Error updating cart item:', error);
      },
      complete: () => {
        console.log('Request complete',cartItem);

      }
    });

    // Update the local stock
    const stockItem = this.needs.find(item => item.id === need.id);
    if (stockItem) {
      stockItem.quantity -= 1;
      console.log(`${need.name} stock reduced by 1.`);
    }
    console.log(`${need.name} added to cart.`);
    return this.fundingBasketService.addNeedToFundingBasket(this.useId!,cartItem);
  }


  getCart(): Observable<Need[]> {
    console.log("getCart");
    return this.cartItems$;
  }
  async getStock(id: number): Promise<number> {
    try {
      const need = await this.getNeedById(id);

      if (need) {
        console.log('Stock:', need.quantity);
        return need.quantity;
      } else {
        console.log('Need not found for id:', id);
        return 0;
      }
    } catch (error) {
      console.error('Error fetching stock:', error);
      return 0;
    }
  }



  submitPayment(paymentDetails: any) {
    console.log(`Payment received from ${paymentDetails.firstName} ${paymentDetails.lastName}:`);
    console.log(`Email: ${paymentDetails.email}`);
    console.log(`Card Number: ${paymentDetails.cardNumber}`);
    console.log(`Expiration: ${paymentDetails.expiration}`);
    console.log(`CVV: ${paymentDetails.cvv}`);
  }


  constructor(private fundingBasketService: FundingBasketService, private authService: AuthService) { }
}
