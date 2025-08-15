import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Landscaper } from './types';

@Component({
  selector: 'app-landscaper-selection-dialog',
  templateUrl: './landscaper-selection-dialog.component.html'
})
export class LandscaperSelectionDialogComponent {
  @Input() showModal = false;
  @Input() landscapers: Landscaper[] = [];

  @Output() showModalChange = new EventEmitter<boolean>();
  @Output() landscaperSelected = new EventEmitter<Landscaper>();
  @Output() createLandscaperRequested = new EventEmitter<void>();

  selectedLandscaperId: number | null = null;

  constructor() {}

  onLandscaperSelect() {
    if (!this.selectedLandscaperId) {
      // User selected the default "Select a landscaper" option, do nothing
      return;
    }

    const landscaper = this.landscapers.find(landscaper => landscaper.id === this.selectedLandscaperId);

    // Just emit the selected landscaper and close modal
    // Authentication will be handled by the parent component
    this.landscaperSelected.emit(landscaper);
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
    this.showModalChange.emit(false);
    this.selectedLandscaperId = null;
  }

  createNewLandscaper() {
    this.createLandscaperRequested.emit();
  }
}
