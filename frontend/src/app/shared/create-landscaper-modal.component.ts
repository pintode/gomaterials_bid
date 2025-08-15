import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Landscaper } from './types';

@Component({
  selector: 'app-create-landscaper-modal',
  templateUrl: './create-landscaper-modal.component.html'
})
export class CreateLandscaperModalComponent {
  @Input() showModal = false;
  @Output() showModalChange = new EventEmitter<boolean>();
  @Output() landscaperCreated = new EventEmitter<Partial<Landscaper>>();

  newLandscaper: Landscaper = { name: '' } as Landscaper;
  
  // Validation errors
  validationErrors = {
    name: ''
  };

  createLandscaper() {
    if (this.validateForm()) {
      this.landscaperCreated.emit(this.newLandscaper);
      this.cancel();
    }
  }

  validateForm(): boolean {
    this.validationErrors.name = '';
    
    if (!this.newLandscaper.name.trim()) {
      this.validationErrors.name = 'Landscaper name is required';
      return false;
    }
    
    return true;
  }

  cancel() {
    this.newLandscaper = { name: '' } as Landscaper;
    this.showModalChange.emit(false);
  }
}
