
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../models/article.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-themes',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HeaderComponent
  ],
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  themes: Theme[] = [];
  subscribedThemeIds: number[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = 'Erreur lors du chargement des thèmes';
        this.isLoading = false;
      }
    });
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemeIds.includes(themeId);
  }

  toggleSubscription(themeId: number): void {
    if (this.isSubscribed(themeId)) {
      this.unsubscribe(themeId);
    } else {
      this.subscribe(themeId);
    }
  }

  subscribe(themeId: number): void {
    this.themeService.subscribe(themeId).subscribe({
      next: () => {
        this.subscribedThemeIds.push(themeId);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Erreur abonnement', error);
      }
    });
  }

  unsubscribe(themeId: number): void {
    this.themeService.unsubscribe(themeId).subscribe({
      next: () => {
        this.subscribedThemeIds = this.subscribedThemeIds.filter(id => id !== themeId);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Erreur désabonnement', error);
      }
    });
  }
}
