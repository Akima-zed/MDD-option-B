import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article, CreateArticleRequest } from '../models/article.model';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = 'http://localhost:8080/api/articles';

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste de tous les articles (fil d'actualité)
   * @returns Observable avec la liste des articles
   */
  getArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(this.apiUrl);
  }

  /**
   * Récupère un article par son ID
   * @param id - ID de l'article
   * @returns Observable avec l'article
   */
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crée un nouvel article
   * @param data - Données de l'article (titre, contenu, themeId)
   * @returns Observable avec l'article créé
   */
  createArticle(data: CreateArticleRequest): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, data);
  }
}
