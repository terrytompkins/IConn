import { IUser } from '@/shared/model/user.model';
import { IApplication } from '@/shared/model/application.model';

import { TechType } from '@/shared/model/enumerations/tech-type.model';
export interface ITech {
  id?: number;
  name?: string | null;
  techType?: TechType | null;
  description?: string | null;
  createdAt?: Date;
  modifiedAt?: Date;
  user?: IUser;
  applications?: IApplication[] | null;
}

export class Tech implements ITech {
  constructor(
    public id?: number,
    public name?: string | null,
    public techType?: TechType | null,
    public description?: string | null,
    public createdAt?: Date,
    public modifiedAt?: Date,
    public user?: IUser,
    public applications?: IApplication[] | null
  ) {}
}
