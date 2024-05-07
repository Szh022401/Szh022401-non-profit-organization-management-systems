import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Need } from '../models/Need';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-cupboard',
  standalone: true,
  imports: [CommonModule,RouterModule],
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
      <a [routerLink]="['/details',need.id]">Learn More</a>
    </section>
  `,
  styleUrls: ['./cupboard.component.css']
})
export class CupboardComponent {
  @Input() need!: Need;
}
