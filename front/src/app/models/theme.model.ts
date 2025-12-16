export interface Theme {
  id: number;
  nom: string;
  description: string;
  abonnes?: User[];
  articles?: Article[];
}

import { User } from './user.model';
import { Article } from './article.model';
