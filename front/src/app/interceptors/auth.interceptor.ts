import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  /**
   * Intercepte toutes les requêtes HTTP pour ajouter le token JWT
   * @param request - Requête HTTP originale
   * @param next - Handler pour la suite de la chaîne
   * @returns Observable de l'événement HTTP
   */
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();

    // Si token existe et n'est pas vide, ajouter l'en-tête Authorization
    if (token && token.trim() !== '') {
      const clonedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(clonedRequest);
    }

    // Sinon, envoyer la requête sans modification
    return next.handle(request);
  }
}
