import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/userProfile.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) {}

  /** Désabonne un utilisateur d'un thème */
  unsubscribeFromTheme(themeId: number): Observable<void> {
    return this.http.post<void>(`http://localhost:8081/api/themes/${themeId}/unsubscribe`, {});
  }

  /** Met à jour le profil utilisateur */
  updateUser(userId: number, userData: { username?: string; email?: string }): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.apiUrl}/${userId}`, userData);
  }

  /** Récupère le profil complet de l'utilisateur connecté */
  getCurrentUser(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/me`);
  }
}