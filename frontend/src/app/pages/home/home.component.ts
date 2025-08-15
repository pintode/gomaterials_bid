import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent {
  features = [
    {
      icon: '🌱',
      title: 'Plant Management',
      description: 'Browse and manage plant inventory with detailed specifications'
    },
    {
      icon: '💰',
      title: 'Bid System',
      description: 'Submit and track bids for landscaping projects'
    },
    {
      icon: '👥',
      title: 'Supplier Network',
      description: 'Connect with verified suppliers and landscapers'
    },
    {
      icon: '📊',
      title: 'Analytics',
      description: 'Track project progress and bid performance'
    }
  ];

  stats = [
    { number: '150+', label: 'Active Suppliers' },
    { number: '500+', label: 'Projects Completed' },
    { number: '1000+', label: 'Plants Available' },
    { number: '95%', label: 'Satisfaction Rate' }
  ];
}
