import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { BidResponse, BidResponseStatus } from '../shared/types';
import { ShowMessageService } from './show-message.service';

@Injectable({ providedIn: 'root' })
export class BidResponseService {
  private baseUrl = `${environment.apiBaseUrl}/v1/bid-responses`;

  constructor(
    private http: HttpClient,
    private showMessageService: ShowMessageService
  ) {}

  getByBidRequest(bidRequestId: number) {
    return this.http.get<BidResponse[]>(`${this.baseUrl}?bidRequestId=${bidRequestId}`);
  }

  submit(bidRequestId: number, bid: Partial<BidResponse>) {
    return this.http.post<BidResponse>(`${this.baseUrl}?bidRequestId=${bidRequestId}`, bid);
  }

  withdrawBidResponse(bidResponseId: number) {
    return this.http.post<BidResponse>(`${this.baseUrl}/${bidResponseId}/withdraw`, {});
  }

  undoWithdrawBidResponse(bidResponseId: number) {
    return this.http.post<BidResponse>(`${this.baseUrl}/${bidResponseId}/undo-withdraw`, {});
  }

  async updateStatus(bidRequestId: number, newStatus: BidResponseStatus): Promise<BidResponse> {
    var url: string;
    if (newStatus === BidResponseStatus.WITHDRAWN) {
      url = `${this.baseUrl}/${bidRequestId}/withdraw`
    } else if (newStatus === BidResponseStatus.SUBMITTED) {  
      url = `${this.baseUrl}/${bidRequestId}/undo-withdraw`
    } else {
      throw new Error('Invalid status');
    }

    return new Promise((resolve, reject) => {
      this.http.post<BidResponse>(url, {}).subscribe({
        next: (updatedBidResponse: BidResponse) => {
          resolve(updatedBidResponse);
        },
        error: (error: any) => {
          this.showMessageService.displayMessage('Error updating bid response. Please try again.', 1000);
          reject(error);
        }
      });
    });
  }
}
