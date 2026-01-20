import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../models/article.model';
import { Subject, takeUntil } from 'rxjs';


/**
 * Page affichant le fil des articles.
 * Permet de consulter les articles et de les trier par date.
 */@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HeaderComponent,
    DatePipe
  ],
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  articles: Article[] = [];
  isLoading = true;
  errorMessage = '';

  /** true = tri décroissant (flèche ↓), false = tri croissant (flèche ↑) */
  isSortDesc = true;

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadArticles(): void {
    this.isLoading = true;

    this.articleService.getArticles()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (articles) => {
          this.articles = this.sortArticlesByDateDesc([...articles]);
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Erreur lors du chargement des articles';
          this.isLoading = false;
        }
      });
  }

  /** Tri décroissant */
  private sortArticlesByDateDesc(articles: Article[]): Article[] {
    return articles.sort((a, b) =>
      new Date(b.dateCreation).getTime() - new Date(a.dateCreation).getTime()
    );
  }

  /** Tri croissant */
  private sortArticlesByDateAsc(articles: Article[]): Article[] {
    return articles.sort((a, b) =>
      new Date(a.dateCreation).getTime() - new Date(b.dateCreation).getTime()
    );
  }

  /** Inverse le tri et met à jour la liste */
  toggleSort(): void {
    this.isSortDesc = !this.isSortDesc;

    this.articles = this.isSortDesc
      ? this.sortArticlesByDateDesc([...this.articles])
      : this.sortArticlesByDateAsc([...this.articles]);
  }

  viewArticle(id: number): void {
    this.router.navigate(['/article', id]);
  }

  createArticle(): void {
    this.router.navigate(['/article-create']);
  }
}
