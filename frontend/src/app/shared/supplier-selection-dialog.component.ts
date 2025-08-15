import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Supplier } from './types';

@Component({
  selector: 'app-supplier-selection-dialog',
  templateUrl: './supplier-selection-dialog.component.html'
})
export class SupplierSelectionDialogComponent {
  @Input() showModal = false;
  @Input() suppliers: Supplier[] = [];

  @Output() showModalChange = new EventEmitter<boolean>();
  @Output() supplierSelected = new EventEmitter<Supplier>();
  @Output() createSupplierRequested = new EventEmitter<void>();

  selectedSupplierId: number | null = null;

  constructor() {}

  onSupplierSelect() {
    if (!this.selectedSupplierId) {
      // User selected the default "Select a supplier" option, do nothing
      return;
    }

    const supplier = this.suppliers.find(supplier => supplier.id === this.selectedSupplierId);
    
    // Just emit the selected supplier and close modal
    // Authentication will be handled by the parent component
    this.supplierSelected.emit(supplier);
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
    this.showModalChange.emit(false);
    this.selectedSupplierId = null;
  }

  createNewSupplier() {
    this.createSupplierRequested.emit();
  }
}
