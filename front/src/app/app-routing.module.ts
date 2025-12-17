import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  // Routes publiques
  { path: '', loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) },
  { path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent) },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  
  // Routes privées (authentification requise)
  { 
    path: 'feed', 
    loadComponent: () => import('./pages/feed/feed.component').then(m => m.FeedComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'article/:id', 
    loadComponent: () => import('./pages/article/article.component').then(m => m.ArticleComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'article-create', 
    loadComponent: () => import('./pages/article-create/article-create.component').then(m => m.ArticleCreateComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'themes', 
    loadComponent: () => import('./pages/themes/themes.component').then(m => m.ThemesComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'profile', 
    loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
