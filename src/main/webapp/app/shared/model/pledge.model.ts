import dayjs from 'dayjs';
import { IProgram } from 'app/shared/model/program.model';
import { IPledgePayment } from 'app/shared/model/pledge-payment.model';

export interface IPledge {
  id?: number;
  amount?: number;
  date?: string;
  memberName?: string;
  program?: IProgram | null;
  pledgePayments?: IPledgePayment[] | null;
}

export const defaultValue: Readonly<IPledge> = {};
