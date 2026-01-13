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
import { Theme } from '../../models/theme.model';

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
  isLoading = true;
  errorMessage = '';

  constructor(
    private themeService: ThemeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes; // 🔥 contient déjà "subscribed"
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Error loading themes';
        this.isLoading = false;
      }
    });
  }

  subscribe(theme: Theme): void {
    if (theme.subscribed) return;

    this.themeService.subscribe(theme.id).subscribe({
      next: () => {
        theme.subscribed = true; // 🔥 met à jour l’état local
        this.snackBar.open(
          'Subscribed to theme ' + theme.name,
          'Close',
          {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          }
        );
      },
      error: () => {
        this.snackBar.open('Error subscribing to theme', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }
}