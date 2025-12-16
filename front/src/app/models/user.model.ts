export interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
  dateInscription: string;
  abonnements?: Theme[];
  articles?: Article[];
  commentaires?: Comment[];
}

import { Theme } from './theme.model';
import { Article } from './article.model';
import { Comment } from './comment.model';
