import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { BidRequest, BidRequestStatus } from '../shared/types';
import { ShowMessageService } from './show-message.service';

@Injectable({ providedIn: 'root' })
export class BidRequestService {
  private baseUrl = `${environment.apiBaseUrl}/v1/bid-requests`;

  constructor(
    private http: HttpClient,
    private showMessageService: ShowMessageService
  ) {}

  getAll(landscaperId?: number) {
    if (landscaperId) {
      return this.http.get<BidRequest[]>(`${this.baseUrl}?landscaperId=${landscaperId}`);
    } else {
      return this.http.get<BidRequest[]>(`${this.baseUrl}`);
    }
  }

  getById(id: number) {
    return this.http.get<BidRequest>(`${this.baseUrl}/${id}`);
  }

  create(bidRequest: Partial<BidRequest>) {
    return this.http.post<BidRequest>(`${this.baseUrl}`, bidRequest);
  }

  async updateStatus(bidRequestId: number, newStatus: BidRequestStatus, awardedBidResponseId?: number): Promise<BidRequest> {
    var url: string;
    if (newStatus === BidRequestStatus.COMPLETED) {
      url = `${this.baseUrl}/${bidRequestId}/complete`
    } else if (newStatus === BidRequestStatus.CANCELLED) {  
      url = `${this.baseUrl}/${bidRequestId}/cancel`
    } else if (newStatus === BidRequestStatus.AWARDED) {
      url = `${this.baseUrl}/${bidRequestId}/award/${awardedBidResponseId}`
    } else {
      throw new Error('Invalid status');
    }

    return new Promise((resolve, reject) => {
      this.http.post<BidRequest>(url, {}).subscribe({
        next: (updatedBidRequest: BidRequest) => {
          resolve(updatedBidRequest);
        },
        error: (error: any) => {
          this.showMessageService.displayMessage('Error updating bid request. Please try again.', 1000);
          reject(error);
        }
      });
    });
  }
}
