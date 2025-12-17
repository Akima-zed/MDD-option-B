import { User } from './user.model';

export interface Comment {
  id: number;
  contenu: string;
  dateCreation: string;
  auteur: User;
  articleId: number;
}

export interface CreateCommentRequest {
  contenu: string;
  articleId: number;
}
