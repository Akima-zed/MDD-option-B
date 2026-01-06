
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
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
    MatSnackBarModule,
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

  constructor(
    private themeService: ThemeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadThemes();
    this.loadUserSubscriptions();
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

  loadUserSubscriptions(): void {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const user = JSON.parse(userStr);
      // Charger les abonnements de l'utilisateur depuis le backend
      this.themeService.getUserSubscriptions(user.id).subscribe({
        next: (themes: Theme[]) => {
          this.subscribedThemeIds = themes.map(theme => theme.id);
        },
        error: (error: HttpErrorResponse) => {
          // Erreur silencieuse - l'utilisateur verra une liste vide
        }
      });
    }
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemeIds.includes(themeId);
  }

  subscribe(themeId: number): void {
    if (this.isSubscribed(themeId)) {
      return; // Déjà abonné
    }
    
    this.themeService.subscribe(themeId).subscribe({
      next: () => {
        this.subscribedThemeIds.push(themeId);
        const theme = this.themes.find(t => t.id === themeId);
        this.snackBar.open('Abonné au thème ' + (theme?.nom || ''), 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      },
      error: (error: HttpErrorResponse) => {
        this.snackBar.open('Erreur lors de l\'abonnement', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }
}
