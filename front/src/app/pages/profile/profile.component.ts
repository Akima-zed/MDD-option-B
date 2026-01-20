import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Theme } from '../../models/theme.model';
import { UserProfile } from '../../models/userProfile.model';

/** Composant de gestion du profil utilisateur */
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
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    HeaderComponent
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user!: UserProfile;
  subscribedThemes: Theme[] = [];
  profileForm!: FormGroup;
  isLoading = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      // Même règle que Register
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$')
        ]
      ]
    });

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.subscribedThemes = user.abonnements;

        this.profileForm.patchValue({
          username: user.username,
          email: user.email
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.snackBar.open('Veuillez corriger les erreurs du formulaire', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;

    const updatedData = this.profileForm.value;

    this.userService.updateUser(this.user.id, updatedData).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.snackBar.open('Profil mis à jour avec succès !', 'Fermer', { duration: 3000 });
      },
      error: () => {
        this.snackBar.open('Erreur lors de la mise à jour du profil', 'Fermer', { duration: 5000 });
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  unsubscribe(theme: Theme): void {
    this.userService.unsubscribeFromTheme(theme.id).subscribe({
      next: () => {
        this.subscribedThemes = this.subscribedThemes.filter(t => t.id !== theme.id);
      },
      error: () => {
        this.snackBar.open('Erreur lors du désabonnement', 'Fermer', { duration: 3000 });
      }
    });
  }
}