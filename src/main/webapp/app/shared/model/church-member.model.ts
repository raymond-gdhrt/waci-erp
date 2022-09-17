import dayjs from 'dayjs';

export interface IChurchMember {
  id?: number;
  fullName?: string;
  phoneNumber?: string;
  date?: string;
  startDate?: string;
  endDate?: string;
}

export const defaultValue: Readonly<IChurchMember> = {};
