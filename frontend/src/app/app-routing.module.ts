import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { BidRequestListComponent } from './pages/bid-request-list/bid-request-list.component';
import { BidRequestDetailComponent } from './pages/bid-request-detail/bid-request-detail.component';
import { SubmitBidComponent } from './pages/submit-bid/submit-bid.component';
import { CreateBidRequestComponent } from './pages/create-bid-request/create-bid-request.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'bid-requests', component: BidRequestListComponent },
  { path: 'bid-requests/create', component: CreateBidRequestComponent },
  { path: 'bid-requests/:id', component: BidRequestDetailComponent },
  { path: 'bid-requests/:id/submit', component: SubmitBidComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
