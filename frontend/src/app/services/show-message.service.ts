import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShowMessageService {
  private isVisible = new BehaviorSubject<boolean>(false);
  private message = new BehaviorSubject<string>('');
  private messageTimeout: number | undefined;
  private onHide?: () => void;
  
  // Confirmation dialog properties
  private showConfirm = new BehaviorSubject<boolean>(false);
  private confirmMessageText = new BehaviorSubject<string>('');
  private confirmYesText = new BehaviorSubject<string>('Yes');
  private confirmNoText = new BehaviorSubject<string>('No');
  private confirmCallback?: (confirmed: boolean) => void;
  
  isVisible$ = this.isVisible.asObservable();
  message$ = this.message.asObservable();
  showConfirm$ = this.showConfirm.asObservable();
  confirmMessageText$ = this.confirmMessageText.asObservable();
  confirmYesText$ = this.confirmYesText.asObservable();
  confirmNoText$ = this.confirmNoText.asObservable();

  displayMessage(message: string, duration: number = 2000, onHide?: () => void): Promise<void> {
    return new Promise((resolve) => {
      // Clear any existing timeout and hide current message
      if (this.messageTimeout !== undefined) {
        clearTimeout(this.messageTimeout);
        this.hideMessage();
      }

      // Set new message and show it
      this.message.next(message);
      this.isVisible.next(true);
      this.onHide = onHide;
      
      // Set new timeout to hide message
      this.messageTimeout = setTimeout(() => {
        this.hideMessage();
        resolve();
      }, duration);
    });
  }

  hideMessage() {
    this.isVisible.next(false);
    this.message.next('');
    this.messageTimeout = undefined;
    this.onHide?.();
    this.onHide = undefined;
  }

  confirmMessage(message: string, yesText: string = 'Yes', noText: string = 'No'): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmMessageText.next(message);
      this.confirmYesText.next(yesText);
      this.confirmNoText.next(noText);
      
      // Store resolve function to call when user makes choice
      this.confirmCallback = (confirmed: boolean) => {
        this.showConfirm.next(false);
        this.confirmCallback = undefined;
        resolve(confirmed);
      };
      
      this.showConfirm.next(true);
    });
  }

  onConfirmYes() {
    this.confirmCallback?.(true);
  }

  onConfirmNo() {
    this.confirmCallback?.(false);
  }
}
