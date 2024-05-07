import { Injectable } from '@angular/core';
import {Need} from "../models/Need";


@Injectable({
  providedIn: 'root'
})
export class CupboardService {
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
  async getAllNeed():Promise<Need[]>{
    const data = await fetch(this.url);
    return await data.json() ?? [];
  }
  async getNeedById(id: number): Promise<Need | undefined>{
    const data = await fetch(`${this.url}/${id}`);
    return await data.json() ?? {};
  }
  submitApplication(firstName: string, lastName: string, email: string) {
    console.log(`Homes application received: firstName: ${firstName}, lastName: ${lastName}, email: ${email}.`);
  }


  constructor() { }
}
