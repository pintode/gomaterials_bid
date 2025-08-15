import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Supplier } from '../shared/types';

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private baseUrl = `${environment.apiBaseUrl}/v1/suppliers`;

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Supplier[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Supplier>(`${this.baseUrl}/${id}`);
  }

  create(supplier: Partial<Supplier>) {
    return this.http.post<Supplier>(this.baseUrl, supplier);
  }
}
