import { Routes } from '@angular/router';
import { HomeComponent } from '../home/home.component';  // 确保路径正确
import { DetailsComponent } from '../details/details.component';  // 确保路径正确
import {LoginComponent} from "../login/login.component";
import {RegisterComponent} from "../register/register.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'Home page'
  },
  {
    path: 'details',
    component: DetailsComponent,
    title: 'Details page'
  },
  {
    path:'login',
    component: LoginComponent,

  },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
];
