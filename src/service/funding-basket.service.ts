import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import { Helper } from "../models/Helper";
import { Need } from "../models/Need";

@Injectable({
  providedIn: 'root'
})
export class FundingBasketService {
  private baseUrl = 'http://localhost:8080/fundingBasket';

  constructor(private http: HttpClient) { }

  createFundingBasket(helper: Helper): Observable<Need[]> {
    return this.http.post<Need[]>(`${this.baseUrl}/createFundingBasket`, helper);
  }


  getAllFundingBasket(): Observable<Helper[]> {
    return this.http.get<Helper[]>(`${this.baseUrl}`);
  }

  getFundingBasketByHelperId(helperId: number | null): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.baseUrl}/${helperId}`).pipe(
      catchError(this.handleError<Need[]>('getFundingBasketByHelperId', []))
    );
  }

  addNeedToFundingBasket(helperId: number, need: Need): Observable<Need[]> {
    return this.http.put<Need[]>(`${this.baseUrl}/${helperId}`, need);
  }

  removeNeedFromFundingBasket(helperId: number, id: number): Observable<Need[]> {
    return this.http.delete<Need[]>(`${this.baseUrl}/${helperId}/${id}`);
  }

  deleteFundingBasket(helperId: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.baseUrl}/${helperId}`);
  }

  getCartItems() {
    return [];
  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
