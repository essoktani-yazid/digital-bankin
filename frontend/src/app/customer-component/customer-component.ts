import { Component, OnInit, signal } from '@angular/core';
import { CustomerService } from '../services/customer-service';
import { CommonModule } from '@angular/common';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Customer } from '../models/customer.model';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-component.html',
  styleUrl: './customer-component.css',
})
export class CustomerComponent implements OnInit{

  customers!: Observable<Customer[]>;
  errorMessage: string = '';
  searchFormGroup!: FormGroup;

  constructor(private customerService: CustomerService, private formBuilder: FormBuilder){}

  public ngOnInit(): void {
    this.customers = this.customerService.getCustomers().pipe(
      catchError((error) => {
        this.errorMessage = error.message;
        return throwError(() => new Error(this.errorMessage));
      })
    );
    this.searchFormGroup = this.formBuilder.group({
      keyword: this.formBuilder.control('')
    });
  }

  public handleSearchCustomers(): void {
    const keyword = this.searchFormGroup.value.keyword;
    this.customers = this.customerService.searchCustomers(keyword).pipe(
      catchError((error) => {
        this.errorMessage = error.message;
        return throwError(() => new Error(this.errorMessage));
      })
    );
  }

}
