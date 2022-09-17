import dayjs from 'dayjs';
import { IPledge } from 'app/shared/model/pledge.model';

export interface IPledgePayment {
  id?: number;
  amount?: number;
  date?: string;
  memberName?: string;
  pledge?: IPledge | null;
}

export const defaultValue: Readonly<IPledgePayment> = {};
