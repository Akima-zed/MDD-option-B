import { Component, OnInit, NgZone, OnDestroy } from '@angular/core';
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
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

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
export class ArticleComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  article: Article | null = null;
  comments: Comment[] = [];
  commentForm!: FormGroup;

  /** Chargement de l’article uniquement */
  isLoading = true;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ngZone: NgZone,
    private articleService: ArticleService,
    private commentService: CommentService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadArticle(id: number): void {
    this.isLoading = true; // 
    this.articleService.getArticleById(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: article => {
          this.article = article;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Article introuvable';
          this.isLoading = false;
        }
      });
  }

  loadComments(articleId: number): void {
    this.commentService.getCommentsByArticleId(articleId).subscribe({
      next: comments => this.comments = comments,
      error: () => {
        this.comments = [];
        this.snackBar.open('Impossible de charger les commentaires.', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.article) return;

    this.isSubmitting = true;
    const commentData: CreateCommentRequest = {
      content: this.commentForm.value.content.trim(),
      articleId: this.article.id
    };

    this.commentService.createComment(commentData).subscribe({
      next: comment => {
        this.comments.push(comment);
        this.commentForm.reset();
        Object.keys(this.commentForm.controls).forEach(key => {
          this.commentForm.get(key)?.setErrors(null);
          this.commentForm.get(key)?.markAsUntouched();
          this.commentForm.get(key)?.markAsPristine();
        });
        this.isSubmitting = false;
        this.snackBar.open('Commentaire ajouté !', 'Fermer', { duration: 3000 });
      },
      error: () => {
        this.isSubmitting = false;
        this.snackBar.open('Impossible d’ajouter le commentaire.', 'Fermer', { duration: 3000 });
      }
    });
  }

  goBack(): void {
    this.ngZone.run(() => this.router.navigate(['/feed']));
  }
}
