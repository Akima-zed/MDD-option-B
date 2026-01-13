import { User } from './user.model';

export interface Comment {
  id: number;
  content: string;
  dateCreation: string;
  author: User;
  articleId: number;
}

export interface CreateCommentRequest {
  content: string;
  articleId: number;
}