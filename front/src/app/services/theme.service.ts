import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme } from '../models/article.model';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = 'http://localhost:8080/api/themes';

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste de tous les thèmes
   * @returns Observable avec la liste des thèmes
   */
  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(this.apiUrl);
  }

  /**
   * Récupère un thème par son ID
   * @param id - ID du thème
   * @returns Observable avec le thème
   */
  getThemeById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/${id}`);
  }

  /**
   * Abonne l'utilisateur à un thème
   * @param id - ID du thème
   * @returns Observable vide
   */
  subscribe(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/subscribe`, {});
  }

  /**
   * Désabonne l'utilisateur d'un thème
   * @param id - ID du thème
   * @returns Observable vide
   */
  unsubscribe(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/unsubscribe`, {});
  }
}
