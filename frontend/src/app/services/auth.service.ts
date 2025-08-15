import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Landscaper, ProfileType, Supplier } from '../shared/types';
import { ShowMessageService } from './show-message.service';

export interface AuthRequest {
  profileType: ProfileType;
  profileId: number;
}

export interface AuthResponse {
  profileType: ProfileType;
  profileId: number;
  profileName: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser = new BehaviorSubject<AuthResponse | null>(null);

  constructor(
    private http: HttpClient,
    private showMessageService: ShowMessageService
  ) { }

  login(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiBaseUrl}/auth/login`, authRequest);
  }

  logout(): void {
    this.currentUser.next(null);
  }

  getToken(): string | null {
    const currentUser = this.getCurrentUser();
    return currentUser?.token || null;
  }

  getCurrentUser(): AuthResponse | null {
    return this.currentUser.value;
  }

  setUserSession(authResponse: AuthResponse): void {
    this.currentUser.next(authResponse);
  }

  async authenticateAndSetSession(profileType: ProfileType, object: Landscaper | Supplier): Promise<AuthResponse> {
    await this.showMessageService.displayMessage(`Signing in as ${profileType.toLowerCase()} ${object.name}.`, 1000);

    return new Promise((resolve, reject) => {
      this.login({ profileType, profileId: object.id }).subscribe({
        next: (response: AuthResponse) => {
          this.setUserSession(response);
          resolve(response);
        },
        error: (error: any) => {
          this.showMessageService.displayMessage('Failed to authenticate. Please try again.', 1000);
          reject(error);
        }
      });
    });
  }
}
