import program from 'app/entities/program/program.reducer';
import churchMember from 'app/entities/church-member/church-member.reducer';
import pledge from 'app/entities/pledge/pledge.reducer';
import pledgePayment from 'app/entities/pledge-payment/pledge-payment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  program,
  churchMember,
  pledge,
  pledgePayment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
