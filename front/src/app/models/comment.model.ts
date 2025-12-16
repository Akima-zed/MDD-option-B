export interface Comment {
  id: number;
  contenu: string;
  dateCreation: string;
  auteur: User;
  article: Article;
}

import { User } from './user.model';
import { Article } from './article.model';
