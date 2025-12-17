
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Article } from '../../models/article.model';
import { Comment, CreateCommentRequest } from '../../models/comment.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-article',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  article: Article | null = null;
  comments: Comment[] = [];
  commentForm!: FormGroup;
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articleService: ArticleService,
    private commentService: CommentService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.commentForm = this.fb.group({
      contenu: ['', [Validators.required, Validators.minLength(3)]]
    });

    const articleId = this.route.snapshot.paramMap.get('id');
    if (articleId) {
      this.loadArticle(+articleId);
      this.loadComments(+articleId);
    }
  }

  loadArticle(id: number): void {
    this.articleService.getArticleById(id).subscribe({
      next: (article: Article) => {
        this.article = article;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = 'Article introuvable';
        this.isLoading = false;
      }
    });
  }

  loadComments(articleId: number): void {
    this.commentService.getCommentsByArticleId(articleId).subscribe({
      next: (comments: Comment[]) => {
        this.comments = comments;
      },
      error: (error: HttpErrorResponse) => {
        console.error('Erreur chargement commentaires', error);
      }
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.article) {
      return;
    }

    this.isSubmitting = true;
    const commentData: CreateCommentRequest = {
      contenu: this.commentForm.value.contenu,
      articleId: this.article.id
    };

    this.commentService.createComment(commentData).subscribe({
      next: (comment: Comment) => {
        this.comments.push(comment);
        this.commentForm.reset();
        this.isSubmitting = false;
      },
      error: (error: HttpErrorResponse) => {
        this.isSubmitting = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/feed']);
  }
}
