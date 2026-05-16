import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-customer-component',
  imports: [ CommonModule, ReactiveFormsModule ],
  templateUrl: './new-customer-component.html',
  styleUrl: './new-customer-component.css',
})
export class NewCustomerComponent implements OnInit{

  newCustomerFormGroup!: any;

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.formBuilder.group({
      name: this.formBuilder.control('', [Validators.required, Validators.minLength(4)]),
      email: this.formBuilder.control('', [Validators.required, Validators.email]),
    });
  }

  handleCreateCustomer(): void {
    const customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: date => {
        this.router.navigate(['/customers']);
      },
      error: err => {
        console.log(err.message);
      }
    });
  }

}
