import { Injectable } from '@angular/core';
import { BidRequestStatus, BidResponseStatus } from './types';

@Injectable({
  providedIn: 'root'
})
export class StatusUtilsService {

  constructor() { }

  getBidRequestStatusClass(status: string): string {
    switch (status) {
      case BidRequestStatus.OPEN:
        return 'bg-blue-100 text-blue-800';
      case BidRequestStatus.IN_PROGRESS:
        return 'bg-yellow-100 text-yellow-800';
      case BidRequestStatus.AWARDED:
        return 'bg-green-100 text-green-800';
      case BidRequestStatus.COMPLETED:
        return 'bg-purple-100 text-purple-800';
      case BidRequestStatus.CANCELLED:
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  getBidResponseStatusClass(status: string): string {
    switch (status) {
      case BidResponseStatus.SUBMITTED:
        return 'bg-blue-100 text-blue-800';
      case BidResponseStatus.AWARDED:
        return 'bg-green-100 text-green-800';
      case BidResponseStatus.REJECTED:
        return 'bg-red-100 text-red-800';
      case BidResponseStatus.WITHDRAWN:
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  getStatusClass(status: string): string {
    // Try bid request status first
    const bidRequestClass = this.getBidRequestStatusClass(status);
    if (bidRequestClass !== 'bg-gray-100 text-gray-800') {
      return bidRequestClass;
    }
    
    // If not a bid request status, try bid response status
    return this.getBidResponseStatusClass(status);
  }
}
