import { User } from './user.model';

export interface Theme {
  id: number;
  nom: string;
  description?: string;
}

export interface Article {
  id: number;
  titre: string;
  contenu: string;
  dateCreation: string;
  auteur: User;
  theme: Theme;
}

export interface CreateArticleRequest {
  titre: string;
  contenu: string;
  themeId: number;
}
