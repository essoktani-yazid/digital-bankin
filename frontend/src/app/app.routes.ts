import { Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer-component';
import { AccountComponent } from './components/account/account-component';
import { NewCustomerComponent } from './components/new-customer/new-customer-component';

export const routes: Routes = [
  { path: 'customers', component: CustomerComponent },
  { path: 'accounts', component: AccountComponent },
  { path: 'new-customer', component: NewCustomerComponent }
];