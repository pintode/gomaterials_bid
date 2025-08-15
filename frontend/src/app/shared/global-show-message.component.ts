import { Component } from '@angular/core';
import { ShowMessageService } from '../services/show-message.service';

@Component({
  selector: 'app-global-show-message',
  templateUrl: './global-show-message.component.html'
})
export class GlobalShowMessageComponent {
  isVisible$ = this.showMessageService.isVisible$;
  message$ = this.showMessageService.message$;
  
  // Confirmation dialog observables
  showConfirm$ = this.showMessageService.showConfirm$;
  confirmMessageText$ = this.showMessageService.confirmMessageText$;
  confirmYesText$ = this.showMessageService.confirmYesText$;
  confirmNoText$ = this.showMessageService.confirmNoText$;

  constructor(private showMessageService: ShowMessageService) {}

  onConfirmYes() {
    this.showMessageService.onConfirmYes();
  }

  onConfirmNo() {
    this.showMessageService.onConfirmNo();
  }
}
