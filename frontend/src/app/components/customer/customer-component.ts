import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Customer } from '../../models/customer.model';
import { CustomerService } from '../../services/customer-service';

@Component({
  selector: 'app-customer-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-component.html',
  styleUrls: ['./customer-component.css'],
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

  public handleCustomerDelete(customer: Customer): void {
    if (confirm("Are you sure to delete this customer?"))
    this.customerService.deleteCustomer(customer).subscribe({
      next: date => {
        this.customers = this.customers.pipe(
          map((customers) => {
            return customers.filter((c) => c.id !== customer.id);
          })
        );
      },
      error: err => {
        console.log(err.message);
      }
    });
  }

}
