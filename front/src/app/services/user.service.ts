import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme } from '../models/article.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) {}

  /**
   * Récupère les abonnements (thèmes) d'un utilisateur
   * @param userId - ID de l'utilisateur
   * @returns Observable avec la liste des thèmes auxquels l'utilisateur est abonné
   */
  getUserSubscriptions(userId: number): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/${userId}/subscriptions`);
  }
}
