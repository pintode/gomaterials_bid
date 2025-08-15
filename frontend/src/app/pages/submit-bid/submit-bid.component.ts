import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BidResponseService } from '../../services/bid-response.service';
import { SupplierService } from '../../services/supplier.service';
import { BidRequestService } from '../../services/bid-request.service';
import { BidRequest, Supplier, BidRequestStatus, ProfileType } from 'src/app/shared/types';

import { AuthService } from '../../services/auth.service';
import { ShowMessageService } from '../../services/show-message.service';

@Component({
  selector: 'app-submit-bid',
  templateUrl: './submit-bid.component.html'
})
export class SubmitBidComponent implements OnInit {
  // Expose enum to template
  BidRequestStatus = BidRequestStatus;

  bid = { totalPrice: 0, estimatedDeliveryDate: '', notes: '' };
  suppliers: Supplier[] = [];
  bidRequest!: BidRequest;
  selectedSupplier: Supplier | null = null;
  showCreateSupplier = false;
  showSupplierSelectionModal = false;
  isAuthenticating = false;
  isSubmitting = false;
  formattedTotalPrice = '$0.00';

  // Validation errors
  validationErrors = {
    totalPrice: '',
    estimatedDeliveryDate: '',
    notes: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bidResponseService: BidResponseService,
    private supplierService: SupplierService,
    private bidRequestService: BidRequestService,
    private authService: AuthService,
    private showMessageService: ShowMessageService
  ) { }

  ngOnInit() {
    this.loadBidRequest();
    this.loadSuppliers();
  }

  private loadBidRequest() {
    const bidRequestId = Number(this.route.snapshot.paramMap.get('id'));
    this.bidRequestService.getById(bidRequestId).subscribe((data: BidRequest) => this.bidRequest = data);
  }

  loadSuppliers() {
    this.supplierService.getAll().subscribe((data: Supplier[]) => {
      this.suppliers = data;
      this.handleSupplierSelection();
    });
  }

  private handleSupplierSelection() {
    // If no supplier is selected and no suppliers exist, open create supplier modal
    // If no supplier is selected and suppliers exist, open supplier selection modal
    if (!this.selectedSupplier) {
      if (this.suppliers.length === 0) {
        this.showCreateSupplier = true;
      } else {
        this.showSupplierSelectionModal = true;
      }
    }
  }

  onSupplierSelectedFromModal(supplier: Supplier) {
    this.showSupplierSelectionModal = false;
    this.selectedSupplier = supplier;
    this.authenticateSupplier(supplier);
  }

  onSupplierModalClosed() {
    // If no supplier was selected and modal was closed, redirect back to bid request
    if (!this.selectedSupplier) {
      const bidRequestId = this.route.snapshot.paramMap.get('id');
      this.router.navigate(['/bid-requests', bidRequestId]);
    }
  }

  onCreateSupplierRequested() {
    // Close the supplier selection modal and open the create supplier modal
    this.showSupplierSelectionModal = false;
    this.showCreateSupplier = true;
  }

  onSupplierCreated(supplier: Partial<Supplier>) {
    this.supplierService.create(supplier).subscribe(
      (createdSupplier: Supplier) => {
        this.suppliers.unshift(createdSupplier);
        this.showCreateSupplier = false;
        this.selectedSupplier = createdSupplier;
        this.authenticateSupplier(createdSupplier);
      },
      (error: any) => {
        this.showMessageService.displayMessage('Failed to create supplier. Please try again.', 1000);
      }
    );
  }

  private authenticateSupplier(supplier: Supplier) {
    this.isAuthenticating = true;
    const authRequest = {
      profileType: ProfileType.SUPPLIER,
      profileId: supplier.id
    };

    this.authService.login(authRequest).subscribe({
      next: (response: any) => {
        this.authService.setUserSession(response);
        this.isAuthenticating = false;
      },
      error: (error: any) => {
        this.selectedSupplier = null;
        this.isAuthenticating = false;
        this.showMessageService.displayMessage('Failed to sign in as supplier. Please try again.', 1000);
        this.handleSupplierSelection();
      }
    });
  }

  validateForm(): boolean {
    let isValid = true;

    // Reset validation errors
    this.validationErrors = {
      totalPrice: '',
      estimatedDeliveryDate: '',
      notes: ''
    };

    // Validate total price
    if (!this.bid.totalPrice || this.bid.totalPrice <= 0) {
      this.validationErrors.totalPrice = 'Please enter a valid total price';
      isValid = false;
    }

    // Validate estimated delivery date
    if (!this.bid.estimatedDeliveryDate) {
      this.validationErrors.estimatedDeliveryDate = 'Please select an estimated delivery date';
      isValid = false;
    }

    return isValid;
  }

  submitBidResponse() {
    if (this.validateForm()) {
      this.isSubmitting = true;
      const bidRequestId = Number(this.route.snapshot.paramMap.get('id'));

      this.bidResponseService.submit(bidRequestId, this.bid).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.showMessageService.displayMessage('Bid submitted successfully!', 1000, () => {
            this.router.navigate(['/bid-requests', bidRequestId]);
          });
        },
        error: (error: any) => {
          this.isSubmitting = false;
          this.showMessageService.displayMessage('Error submitting bid response. Please try again.', 1000);
        }
      });
    }
  }

  goBack() {
    const bidRequestId = this.route.snapshot.paramMap.get('id');
    this.router.navigate(['/bid-requests', bidRequestId]);
  }

  onTotalPriceInput(event: any, precision: number = 12, scale: number = 2) {
    let value = event.target.value;

    // Split the value in the last decimal point
    let parts = value.split(/\.([^.]*)$/, 2);

    // Remove all non-numeric characters from existing parts and join with a decimal point
    value = parts[0].replace(/\D/g, '')
    if (parts.length === 2) {
      value += '.' + parts[1].replace(/\D/g, '').substring(0, scale);
    }

    // Limit the length of the value to the precision
    let maxLength = precision + (parts.length === 2 ? 1 : 0);
    if (maxLength > value.length) {
      value = value.substring(0, maxLength);
    }

    const numValue = parseFloat(value) || 0;
    this.bid.totalPrice = numValue;

    this.formattedTotalPrice = value;
  }

  onTotalPriceFocus(event: any) {
    // Remove currency formatting when focusing
    this.formattedTotalPrice = !this.bid.totalPrice ? "" : this.bid.totalPrice.toString();
  }

  onTotalPriceBlur(event: any) {
    // Format the total price when the input loses focus
    const numValue = this.bid.totalPrice;
    this.formattedTotalPrice = this.formatCurrency(numValue);
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(value);
  }
}
