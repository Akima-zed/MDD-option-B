import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Article } from '../../models/article.model';
import { Comment, CreateCommentRequest } from '../../models/comment.model';
import { HttpErrorResponse } from '@angular/common/http';

/**
 * Affiche un article et ses commentaires.
 * Permet d’ajouter un commentaire via un formulaire réactif.
 */
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
    MatProgressSpinnerModule,
    MatSnackBarModule,
    HeaderComponent,
    DatePipe
  ],
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  article: Article | null = null;
  comments: Comment[] = [];
  commentForm!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articleService: ArticleService,
    private commentService: CommentService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  // Initialise le formulaire et charge l'article + commentaires
  ngOnInit(): void {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(3), Validators.pattern(/\S+/)]]
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
      error: () => {
        this.errorMessage = 'Article not found';
        this.isLoading = false;
      }
    });
  }

  loadComments(articleId: number): void {
  this.commentService.getCommentsByArticleId(articleId).subscribe({
    next: (comments: Comment[]) => {
      this.comments = comments;
    },
    error: () => {
      // En cas d’erreur, on vide la liste et on informe l’utilisateur
      this.comments = [];
      this.snackBar.open('Impossible de charger les commentaires.', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top'
      });
    }
  });
}


  // Envoie un commentaire si le formulaire est valide
  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.article) return;

    this.isSubmitting = true;

    const commentData: CreateCommentRequest = {
      content: this.commentForm.value.content.trim(),
      articleId: this.article.id
    };

    this.commentService.createComment(commentData).subscribe({
      next: (comment: Comment) => {
        this.comments.push(comment);
        this.commentForm.reset();
        this.isSubmitting = false;
        this.snackBar.open('Comment added successfully!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      },
      error: () => {
        this.isSubmitting = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/feed']);
  }
}