import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  public getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(environment.backendHost + "/customers");
  }

  public searchCustomers(keyword: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(environment.backendHost + "/customers/search?keyword=" + keyword);
  }

  public saveCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(environment.backendHost + "/customers", customer);
  }

  public deleteCustomer(customer: Customer): Observable<void> {
    return this.http.delete<void>(environment.backendHost + "/customers/" + customer.id);
  }

}
