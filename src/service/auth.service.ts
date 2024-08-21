import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/user';
  private userId:number | null = null;

  constructor(private http:HttpClient) {
    const storedId = localStorage.getItem('id');
    if (storedId) {
      this.userId = parseInt(storedId, 10);
    }
  }
  register(username:string):Observable<any>{
    const user = {username};
    return this.http.post<any>(`${this.apiUrl}/register`, user);
  }
  login(username:string):Observable<any>{
    const credentials = { username };
    return this.http.post<any>(`${this.apiUrl}/login`, credentials);
  }
  setId(userId:number):void{
    this.userId = userId;
    localStorage.setItem('userId', userId.toString());
  }
  getId():number | null{
    if(this.userId !== null){
      return this.userId;
    } else {
      const storedId = localStorage.getItem('userId');
      return storedId ? parseInt(storedId, 10) : null;
    }

  }
  logout(): void {
    this.userId = null;
    localStorage.removeItem('userId');
  }
}
