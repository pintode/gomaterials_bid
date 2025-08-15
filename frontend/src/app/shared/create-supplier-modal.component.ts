import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Supplier } from './types';

@Component({
  selector: 'app-create-supplier-modal',
  templateUrl: './create-supplier-modal.component.html'
})
export class CreateSupplierModalComponent {
  @Input() showModal = false;
  @Output() showModalChange = new EventEmitter<boolean>();
  @Output() supplierCreated = new EventEmitter<Partial<Supplier>>();

  newSupplier: Supplier = { name: '' } as Supplier;
  
  // Validation errors
  validationErrors = {
    name: ''
  };

  createSupplier() {
    if (this.validateForm()) {
      this.supplierCreated.emit(this.newSupplier);
      this.cancel();
    }
  }

  validateForm(): boolean {
    this.validationErrors.name = '';
    
    if (!this.newSupplier.name.trim()) {
      this.validationErrors.name = 'Supplier name is required';
      return false;
    }
    
    return true;
  }

  cancel() {
    this.newSupplier = { name: '' } as Supplier;
    this.showModalChange.emit(false);
  }
}
