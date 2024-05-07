import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import  {CupboardService} from '../service/cupboard.service';
import  {Need} from '../models/Need';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  template: `
    <article>
      <img
        class="listing-photo"
        [src]="need?.image"
        alt="Exterior photo of {{ need?.name }}"
        crossorigin
      />
      <section class="listing-description">
        <h2 class="listing-heading">{{ need?.name }}</h2>
      </section>
      <section class="listing-features">
        <h2 class="section-heading">About this item</h2>
        <ul>
          <li>available: {{ need?.status }}</li>
          <li>type: {{ need?.type }}</li>
          <li>quantity: {{ need?.quantity }}</li>
        </ul>
      </section>
      <section class="listing-apply">
        <h2 class="section-heading">Your information to pay</h2>
        <form [formGroup]="applyForm" (submit)="submitApplication()">
          <label for="first-name">First Name</label>
          <input id="first-name" type="text" formControlName="firstName">

          <label for="last-name">Last Name</label>
          <input id="last-name" type="text" formControlName="lastName">

          <label for="email">Email</label>
          <input id="email" type="email" formControlName="email">
          <button type="submit" class="primary">submit now</button>
        </form>
      </section>
    </article>
  `,
  styleUrls: ['./details.component.css'],
})
export class DetailsComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  cupboardService = inject(CupboardService);
  need: Need | undefined;
  applyForm = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    email: new FormControl('')
  });
  constructor() {
    const cupboardId = Number(this.route.snapshot.params['id']); // Correct variable name spelling and removed unnecessary radix parameter
    this.cupboardService.getNeedById(cupboardId).then((need) => {
      this.need = need;
    });
  }
  submitApplication() {
    this.cupboardService.submitApplication(
      this.applyForm.value.firstName ?? '',
      this.applyForm.value.lastName ?? '',
      this.applyForm.value.email ?? ''
    );
  }

}

