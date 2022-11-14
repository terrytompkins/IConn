import { IUser } from '@/shared/model/user.model';
import { ITech } from '@/shared/model/tech.model';

export interface IApplication {
  id?: number;
  name?: string | null;
  description?: string | null;
  wikiUrl?: string | null;
  logoImageContentType?: string | null;
  logoImage?: string | null;
  createdAt?: Date;
  modifiedAt?: Date;
  user?: IUser;
  teches?: ITech[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public wikiUrl?: string | null,
    public logoImageContentType?: string | null,
    public logoImage?: string | null,
    public createdAt?: Date,
    public modifiedAt?: Date,
    public user?: IUser,
    public teches?: ITech[] | null
  ) {}
}
