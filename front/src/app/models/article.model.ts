export interface Article {
  id: number;
  titre: string;
  contenu: string;
  dateCreation: string;
  auteur: User;
  theme: Theme;
  commentaires?: Comment[];
}

import { User } from './user.model';
import { Theme } from './theme.model';
import { Comment } from './comment.model';
