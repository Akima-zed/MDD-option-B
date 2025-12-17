import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment, CreateCommentRequest } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8080/api/articles';

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste des commentaires d'un article
   * @param articleId - ID de l'article
   * @returns Observable avec la liste des commentaires
   */
  getCommentsByArticleId(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/${articleId}/comments`);
  }

  /**
   * Crée un nouveau commentaire sur un article
   * @param data - Données du commentaire (contenu, articleId)
   * @returns Observable avec le commentaire créé
   */
  createComment(data: CreateCommentRequest): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.apiUrl}/${data.articleId}/comments`,
      { contenu: data.contenu }
    );
  }
}
