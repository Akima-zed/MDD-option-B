import { Component, OnDestroy, OnInit } from '@angular/core';
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
import { Subject } from 'rxjs/internal/Subject';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';

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
export class ThemesComponent implements OnInit,OnDestroy {

private destroy$ = new Subject<void>();

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
    this.isLoading = true;
    this.errorMessage = '';

    this.themeService.getThemes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (themes: Theme[]) => {
          this.themes = themes;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Erreur lors du chargement des thèmes';
          this.isLoading = false;
        }
      });
  }

  subscribe(theme: Theme): void {
    if (theme.subscribed) return;

    this.themeService.subscribe(theme.id)
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: () => {
        theme.subscribed = true; 
        this.snackBar.open(
          'Vous êtes maintenant abonné au thème ' + theme.name,
          'Fermer',
          {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          }
        );
      },
      error: () => {
        this.snackBar.open('Erreur lors de la souscription au thème', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }

  trackById(index: number, theme: Theme): number {
    return theme.id;
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

}