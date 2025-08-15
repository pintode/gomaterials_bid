import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BidRequestService } from '../../services/bid-request.service';
import { BidResponseService } from '../../services/bid-response.service';
import { AuthService } from '../../services/auth.service';
import { BidRequest, BidResponse, BidRequestStatus, BidResponseStatus, ProfileType, Supplier } from '../../shared/types';
import { StatusUtilsService } from '../../shared/status-utils.service';
import { ShowMessageService } from '../../services/show-message.service';

@Component({
  selector: 'app-bid-request-detail',
  templateUrl: './bid-request-detail.component.html'
})
export class BidRequestDetailComponent implements OnInit {
  // Expose enums to template
  BidRequestStatus = BidRequestStatus;
  BidResponseStatus = BidResponseStatus;

  bidRequest!: BidRequest;
  loading = true;
  showBidDetailsModal = false;
  selectedBidResponse: BidResponse | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bidRequestService: BidRequestService,
    private bidResponseService: BidResponseService,
    public authService: AuthService,
    private statusUtilsService: StatusUtilsService,
    private showMessageService: ShowMessageService
  ) { }

  ngOnInit() {
    this.loadBidRequest();
  }

  private loadBidRequest() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bidRequestService.getById(id).subscribe(
      (bidRequest: BidRequest) => {
        this.bidRequest = bidRequest;
        this.loading = false;
      },
      (error: any) => {
        this.showMessageService.displayMessage('Failed to load bid request. Please try again.', 1000);
        this.loading = false;
        this.goBack();
      }
    );
  }

  getStatusClass(status: string): string {
    return this.statusUtilsService.getBidRequestStatusClass(status);
  }

  getBidResponseStatusClass(status: string): string {
    return this.statusUtilsService.getBidResponseStatusClass(status);
  }

  goBack() {
    this.router.navigate(['/bid-requests']);
  }

  navigateToSubmitBid() {
    const bidRequestId = this.route.snapshot.paramMap.get('id');
    this.router.navigate(['/bid-requests', bidRequestId, 'submit']);
  }

  async awardBid(bidResponse: BidResponse) {
    if (!await this.showMessageService.confirmMessage('Award this bid response to the supplier?')) {
      return;
    }

    await this.updateBidRequestStatus(BidRequestStatus.AWARDED, bidResponse.id);
    this.showMessageService.displayMessage('Bid awarded successfully!', 1000);
  }

  async completeAwarded() {
    if (!await this.showMessageService.confirmMessage('Are you sure you want to mark the project as completed?')) {
      return;
    }

    await this.updateBidRequestStatus(BidRequestStatus.COMPLETED);
    this.router.navigate(['/bid-requests']);
    this.showMessageService.displayMessage('Bid request completed successfully!', 1000);
  }

  async cancelBidRequest() {
    if (!await this.showMessageService.confirmMessage('Are you sure you want to cancel this bid request? This action cannot be undone.')) {
      return;
    }

    await this.updateBidRequestStatus(BidRequestStatus.CANCELLED);
    this.router.navigate(['/bid-requests']);
    this.showMessageService.displayMessage('Bid request cancelled successfully!', 1000);
  }

  private async updateBidRequestStatus(newStatus: BidRequestStatus, awardedBidResponseId?: number) {
    await this.authService.authenticateAndSetSession(ProfileType.LANDSCAPER, this.bidRequest.landscaper);

    const bidRequestId = Number(this.route.snapshot.paramMap.get('id'));
    this.bidRequest = await this.bidRequestService.updateStatus(bidRequestId, newStatus, awardedBidResponseId);
  }

  async withdrawBid(bidResponse: BidResponse) {
    await this.updateBidResponseStatus(bidResponse, BidResponseStatus.WITHDRAWN);
    this.showMessageService.displayMessage('Bid withdrawn successfully!', 1000);
  }

  async undoWithdrawBid(bidResponse: BidResponse) {
    await this.updateBidResponseStatus(bidResponse, BidResponseStatus.SUBMITTED);
    this.showMessageService.displayMessage('Bid moved back to Submitted!', 1000);
  }

  private async updateBidResponseStatus(bidResponse: BidResponse, newStatus: BidResponseStatus) {
    await this.authService.authenticateAndSetSession(ProfileType.SUPPLIER, bidResponse.supplier);

    const updatedBidResponse = await this.bidResponseService.updateStatus(bidResponse.id, newStatus);

    const index = this.bidRequest.bidResponses.indexOf(bidResponse);
    this.bidRequest.bidResponses[index] = updatedBidResponse;
    return updatedBidResponse;
  }

  viewBidDetails(bidResponse: BidResponse) {
    this.selectedBidResponse = bidResponse;
    this.showBidDetailsModal = true;
  }

  closeBidDetailsModal() {
    this.showBidDetailsModal = false;
    this.selectedBidResponse = null;
  }
}
