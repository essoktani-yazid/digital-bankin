import { Routes } from '@angular/router';
import { CustomerComponent } from './customer-component/customer-component';
import { AccountComponent } from './account-component/account-component';

export const routes: Routes = [
  { path: 'customers', component: CustomerComponent },
  { path: 'accounts', component: AccountComponent }
];