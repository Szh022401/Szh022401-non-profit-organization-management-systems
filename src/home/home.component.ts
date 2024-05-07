import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import { CupboardComponent } from '../cupboard/cupboard.component';
import { Need } from '../models/Need';
import {CupboardService} from '../service/cupboard.service'

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CupboardComponent],
  template: `
    <section>
      <form>
        <input type="text" placeholder="Filter" #filter>
        <button class="primary" type="button" (click)="filterResults(filter.value)">Search</button>
      </form>
    </section>
    <section class="results">

      <app-cupboard
        *ngFor="let need of filteredNeedList"
        [need]="need"></app-cupboard>
    </section>
  `,
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  cupboard: Need[] =[];
  cupboardService : CupboardService = inject(CupboardService);
  filteredNeedList: Need[] = [];
  constructor() {
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
}
