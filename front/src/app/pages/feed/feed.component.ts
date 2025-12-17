
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../models/article.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  articles: Article[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.isLoading = true;
    this.articleService.getArticles().subscribe({
      next: (articles: Article[]) => {
        this.articles = articles;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = 'Erreur lors du chargement des articles';
        this.isLoading = false;
      }
    });
  }

  viewArticle(id: number): void {
    this.router.navigate(['/article', id]);
  }

  createArticle(): void {
    this.router.navigate(['/article-create']);
  }
}
