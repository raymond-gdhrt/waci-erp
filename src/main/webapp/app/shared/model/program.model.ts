import dayjs from 'dayjs';
import { IPledge } from 'app/shared/model/pledge.model';

export interface IProgram {
  id?: number;
  name?: string;
  minAmount?: number;
  description?: string;
  startDate?: string;
  endDate?: string;
  pledges?: IPledge[] | null;
}

export const defaultValue: Readonly<IProgram> = {};
