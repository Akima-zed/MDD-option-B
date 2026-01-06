
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Theme } from '../../models/article.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    HeaderComponent
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  subscribedThemes: Theme[] = [];
  isEditMode: boolean = false;
  profileForm: FormGroup;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    // Récupérer les infos utilisateur du localStorage
    const userStr = localStorage.getItem('user');
    if (userStr) {
      this.user = JSON.parse(userStr);
      // Initialiser le formulaire avec les données actuelles
      this.profileForm.patchValue({
        username: this.user.username,
        email: this.user.email
      });
      // Charger les abonnements depuis le backend
      this.loadSubscriptions();
    } else {
      // Rediriger vers login si pas d'utilisateur
      this.router.navigate(['/login']);
    }
  }

  loadSubscriptions(): void {
    this.userService.getUserSubscriptions(this.user.id).subscribe({
      next: (themes: Theme[]) => {
        this.subscribedThemes = themes;
      },
      error: (error) => {
        // Erreur silencieuse - affichage d'une liste vide
      }
    });
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
    if (!this.isEditMode) {
      // Réinitialiser le formulaire si on annule
      this.profileForm.patchValue({
        username: this.user.username,
        email: this.user.email
      });
    }
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.snackBar.open('Veuillez corriger les erreurs du formulaire', 'Fermer', {
        duration: 3000
      });
      return;
    }

    const updatedData = this.profileForm.value;
    
    this.userService.updateUser(this.user.id, updatedData).subscribe({
      next: (updatedUser) => {
        // Mettre à jour les données locales
        this.user.username = updatedUser.username;
        this.user.email = updatedUser.email;
        
        // Mettre à jour le localStorage
        localStorage.setItem('user', JSON.stringify(this.user));
        
        // Quitter le mode édition
        this.isEditMode = false;
        
        this.snackBar.open('Profil mis à jour avec succès !', 'Fermer', {
          duration: 3000
        });
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Erreur lors de la mise à jour du profil';
        this.snackBar.open(errorMessage, 'Fermer', {
          duration: 5000
        });
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  unsubscribe(theme: Theme): void {
    this.authService.getAuthToken(); // Vérifier que l'utilisateur est connecté
    this.userService.unsubscribeFromTheme(this.user.id, theme.id).subscribe({
      next: () => {
        this.subscribedThemes = this.subscribedThemes.filter(t => t.id !== theme.id);
      },
      error: (error) => {
        this.snackBar.open('Erreur lors du désabonnement', 'Fermer', {
          duration: 3000
        });
      }
    });
  }
}
