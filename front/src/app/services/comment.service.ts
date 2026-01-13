import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment, CreateCommentRequest } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8081/api/articles';

  constructor(private http: HttpClient) {}

  getCommentsByArticleId(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/${articleId}/comments`);
  }

  createComment(data: CreateCommentRequest): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.apiUrl}/${data.articleId}/comments`,
      { content: data.content }
    );
  }
}