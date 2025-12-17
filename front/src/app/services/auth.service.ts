import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { RegisterRequest, LoginRequest, AuthResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) {}

  /**
   * Inscrit un nouvel utilisateur
   * @param data - Données d'inscription (username, email, password)
   * @returns Observable avec le token JWT et les infos utilisateur
   */
  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data);
  }

  /**
   * Connecte un utilisateur existant
   * @param data - Données de connexion (email/username et password)
   * @returns Observable avec le token JWT et les infos utilisateur
   */
  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data).pipe(
      tap(response => {
        // Stocke le token et les infos utilisateur dans le localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify({
          id: response.id,
          username: response.username,
          email: response.email
        }));
      })
    );
  }

  /**
   * Déconnecte l'utilisateur en supprimant le token et les infos
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  /**
   * Récupère le token JWT depuis le localStorage
   * @returns Le token JWT ou null s'il n'existe pas
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Vérifie si l'utilisateur est authentifié
   * @returns true si un token existe, false sinon
   */
  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  /**
   * Récupère le token avec le préfixe Bearer pour les requêtes HTTP
   * @returns Le token avec préfixe Bearer ou null
   */
  getAuthToken(): string | null {
    const token = this.getToken();
    return token ? `Bearer ${token}` : null;
  }
}
