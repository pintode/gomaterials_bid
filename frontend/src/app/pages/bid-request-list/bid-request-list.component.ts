import { Component, OnInit } from '@angular/core';
import { BidRequestService } from '../../services/bid-request.service';
import { AuthService } from '../../services/auth.service';
import { BidRequest } from '../../shared/types';
import { StatusUtilsService } from '../../shared/status-utils.service';
import { ShowMessageService } from '../../services/show-message.service';

@Component({
  selector: 'app-bid-request-list',
  templateUrl: './bid-request-list.component.html'
})
export class BidRequestListComponent implements OnInit {
  bidRequests!: BidRequest[];
  loading = true;

  constructor(
    private bidRequestService: BidRequestService,
    public authService: AuthService,
    private statusUtilsService: StatusUtilsService,
    private showMessageService: ShowMessageService
  ) {}

  ngOnInit() {
    this.loadBidRequests();
  }

  private loadBidRequests() {
    this.bidRequestService.getAll().subscribe(
      (bidRequests: BidRequest[]) => {
        this.bidRequests = bidRequests;
        this.loading = false;
      },
      (error: any) => {
        this.showMessageService.displayMessage('Failed to load bid requests. Please try again.', 2000);
        this.loadBidRequests();
      }
    );
  }

  getStatusClass(status: string): string {
    return this.statusUtilsService.getBidRequestStatusClass(status);
  }
}
