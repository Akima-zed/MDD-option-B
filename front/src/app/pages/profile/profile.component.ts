
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
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
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    HeaderComponent
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  subscribedThemes: Theme[] = [];

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Récupérer les infos utilisateur du localStorage
    const userStr = localStorage.getItem('user');
    if (userStr) {
      this.user = JSON.parse(userStr);
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
        console.error('Erreur lors du chargement des abonnements:', error);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  unsubscribe(theme: Theme): void {
    // TODO: Appeler le backend pour se désabonner
    this.subscribedThemes = this.subscribedThemes.filter(t => t.id !== theme.id);
  }
}
