import { Component, OnInit, signal } from '@angular/core';
import { CustomerService } from '../services/customer-service';
import { CommonModule } from '@angular/common';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Customer } from '../models/customer.model';

@Component({
  selector: 'app-customer-component',
  imports: [CommonModule],
  templateUrl: './customer-component.html',
  styleUrl: './customer-component.css',
})
export class CustomerComponent implements OnInit{

  customers!: Observable<Customer[]>;
  errorMessage: string = '';

  constructor(private customerService: CustomerService){}

  public ngOnInit(): void {
    this.customers = this.customerService.getCustomers().pipe(
      catchError((error) => {
        this.errorMessage = error.message;
        return throwError(() => new Error(this.errorMessage));
      })
    );
  }

}
