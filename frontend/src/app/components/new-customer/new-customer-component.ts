import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer-service';

@Component({
  selector: 'app-new-customer-component',
  imports: [ ReactiveFormsModule ],
  templateUrl: './new-customer-component.html',
  styleUrl: './new-customer-component.css',
})
export class NewCustomerComponent implements OnInit{

  newCustomerFormGroup!: any;

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService) {}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.formBuilder.group({
      name: this.formBuilder.control(''),
      email: this.formBuilder.control(''),
    });
  }

  handleCreateCustomer(): void {
    const customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: date => {
        alert("Success!");
      },
      error: err => {
        console.log(err.message);
      }
    });
  }

}
