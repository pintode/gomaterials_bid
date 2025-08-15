import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Landscaper } from '../shared/types';

@Injectable({ providedIn: 'root' })
export class LandscaperService {
  private baseUrl = `${environment.apiBaseUrl}/v1/landscapers`;

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Landscaper[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Landscaper>(`${this.baseUrl}/${id}`);
  }

  create(landscaper: Partial<Landscaper>) {
    return this.http.post<Landscaper>(this.baseUrl, landscaper);
  }
}
