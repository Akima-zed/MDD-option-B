import { User } from './user.model';

export interface Comment {
  id: number;
  content: string;
  dateCreation: string;
  auteur: User;
  articleId: number;
}

export interface CreateCommentRequest {
  content: string;
  articleId: number;
}
