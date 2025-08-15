import { NgModule, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import localeEnCA from '@angular/common/locales/en-CA'; 
import { registerLocaleData } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { BidRequestListComponent } from './pages/bid-request-list/bid-request-list.component';
import { BidRequestDetailComponent } from './pages/bid-request-detail/bid-request-detail.component';
import { SubmitBidComponent } from './pages/submit-bid/submit-bid.component';
import { CreateBidRequestComponent } from './pages/create-bid-request/create-bid-request.component';
import { CreateSupplierModalComponent } from './shared/create-supplier-modal.component';
import { CreateLandscaperModalComponent } from './shared/create-landscaper-modal.component';
import { SupplierSelectionDialogComponent } from './shared/supplier-selection-dialog.component';
import { LandscaperSelectionDialogComponent } from './shared/landscaper-selection-dialog.component';
import { GlobalShowMessageComponent } from './shared/global-show-message.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';

registerLocaleData(localeEnCA);

@NgModule({
      declarations: [
      AppComponent,
      HomeComponent,
      BidRequestListComponent,
      BidRequestDetailComponent,
      SubmitBidComponent,
      CreateBidRequestComponent,
      CreateSupplierModalComponent,
      CreateLandscaperModalComponent,
      SupplierSelectionDialogComponent,
      LandscaperSelectionDialogComponent,
      GlobalShowMessageComponent
    ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'en-CA' },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}