import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BidRequestService } from '../../services/bid-request.service';
import { LandscaperService } from '../../services/landscaper.service';
import { AuthService } from '../../services/auth.service';
import { BidRequest, Landscaper, PlantItem, ProfileType } from '../../shared/types';
import { ShowMessageService } from '../../services/show-message.service';

@Component({
  selector: 'app-create-bid-request',
  templateUrl: './create-bid-request.component.html'
})
export class CreateBidRequestComponent implements OnInit {
  bidRequest: BidRequest = {
    projectName: '',
    requiredBy: '',
    plantItems: [] as PlantItem[],
  } as BidRequest;

  newPlantItem: PlantItem = {
    name: '',
    grade: '',
    quantity: 1,
    unit: 'units'
  } as PlantItem;

  landscapers: Landscaper[] = [];
  showCreateLandscaper = false;
  showLandscaperSelectionModal = false;
  selectedLandscaper: Landscaper | null = null;
  isAuthenticating = false;

  // Validation errors
  validationErrors = {
    projectName: '',
    requiredBy: '',
    plantItems: ''
  };

  newPlantItemErrors = {
    name: '',
    grade: '',
    quantity: '',
    unit: ''
  };

  constructor(
    private router: Router,
    private bidRequestService: BidRequestService,
    private landscaperService: LandscaperService,
    public authService: AuthService,
    private showMessageService: ShowMessageService
  ) { }

  ngOnInit() {
    this.loadLandscapers();
  }

  loadLandscapers() {
    this.landscaperService.getAll().subscribe((data: Landscaper[]) => {
      this.landscapers = data;
      this.handleLandscaperSelection();
    });
  }

  private handleLandscaperSelection() {
    // If no landscaper is selected and no landscapers exist, open create landscaper modal
    // If no landscaper is selected and landscapers exist, open landscaper selection modal
    if (!this.selectedLandscaper) {
      if (this.landscapers.length === 0) {
        this.showCreateLandscaper = true;
      } else {
        this.showLandscaperSelectionModal = true;
      }
    }
  } 

  onLandscaperSelectedFromModal(landscaper: Landscaper) {
    this.selectedLandscaper = landscaper;
    this.showLandscaperSelectionModal = false;
    // Automatically authenticate the selected landscaper
    this.authenticateLandscaper(landscaper);
  }

  onLandscaperModalClosed() {
    // If no landscaper was selected and modal was closed, redirect back to bid requests
    if (!this.selectedLandscaper) {
      this.router.navigate(['/bid-requests']);
    }
  }

  onCreateLandscaperRequested() {
    // Close the landscaper selection modal and open the create landscaper modal
    this.showLandscaperSelectionModal = false;
    this.showCreateLandscaper = true;
  }

  onLandscaperCreated(landscaper: Partial<Landscaper>) {
    this.landscaperService.create(landscaper).subscribe(
      (createdLandscaper: Landscaper) => {
        this.landscapers.unshift(createdLandscaper);
        this.selectedLandscaper = createdLandscaper;
        this.authenticateLandscaper(createdLandscaper)
      },
      (error: any) => {
        this.showMessageService.displayMessage('Failed to create landscaper. Please try again.', 1000);
      }
    );
  }

  private authenticateLandscaper(landscaper: Landscaper) {
    this.isAuthenticating = true;
    const authRequest = {
      profileType: ProfileType.LANDSCAPER,
      profileId: landscaper.id
    };

    this.authService.login(authRequest).subscribe({
      next: (response: any) => {
        this.authService.setUserSession(response);
        this.isAuthenticating = false;
      },
      error: (error: any) => {
        this.selectedLandscaper = null;
        this.isAuthenticating = false;
        this.showMessageService.displayMessage('Failed to sign in as landscaper. Please try again.', 1000);
        this.handleLandscaperSelection();
      }
    });
  }

  validateNewPlantItem(): boolean {
    let isValid = true;

    // Reset validation errors
    this.newPlantItemErrors = {
      name: '',
      grade: '',
      quantity: '',
      unit: ''
    };

    if (!this.newPlantItem.name.trim()) {
      this.newPlantItemErrors.name = 'Plant name is required';
      isValid = false;
    }

    if (!this.newPlantItem.grade.trim()) {
      this.newPlantItemErrors.grade = 'Grade is required';
      isValid = false;
    }

    if (!this.newPlantItem.quantity || this.newPlantItem.quantity <= 0) {
      this.newPlantItemErrors.quantity = 'Quantity must be greater than 0';
      isValid = false;
    }

    if (!this.newPlantItem.unit) {
      this.newPlantItemErrors.unit = 'Unit is required';
      isValid = false;
    }

    return isValid;
  }

  addPlantItem() {
    if (this.validateNewPlantItem()) {
      this.bidRequest.plantItems.push({ ...this.newPlantItem });
      this.newPlantItem = { name: '', grade: '', quantity: 1, unit: 'units' } as PlantItem;
      // Clear validation errors after successful addition
      this.newPlantItemErrors = { name: '', grade: '', quantity: '', unit: '' };
      this.validationErrors.plantItems = '';
    }
  }

  removePlantItem(index: number) {
    this.bidRequest.plantItems.splice(index, 1);
  }

  validateForm(): boolean {
    let isValid = true;

    // Reset validation errors
    this.validationErrors = {
      projectName: '',
      requiredBy: '',
      plantItems: ''
    };

    if (!this.bidRequest.projectName.trim()) {
      this.validationErrors.projectName = 'Project name is required';
      isValid = false;
    }

    if (!this.bidRequest.requiredBy) {
      this.validationErrors.requiredBy = 'Required by date is required';
      isValid = false;
    }

    if (this.bidRequest.plantItems.length === 0) {
      this.validationErrors.plantItems = 'At least one plant item is required';
      isValid = false;
    }

    return isValid;
  }

  createBidRequest() {
    if (this.validateForm()) {
      this.bidRequestService.create(this.bidRequest).subscribe(() => {
        this.router.navigate(['/bid-requests']);
      });
    }
  }

  cancel() {
    this.router.navigate(['/bid-requests']);
  }
}
