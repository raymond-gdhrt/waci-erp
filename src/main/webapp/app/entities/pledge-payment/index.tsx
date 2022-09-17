import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PledgePayment from './pledge-payment';
import PledgePaymentDetail from './pledge-payment-detail';
import PledgePaymentUpdate from './pledge-payment-update';
import PledgePaymentDeleteDialog from './pledge-payment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PledgePaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PledgePaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PledgePaymentDetail} />
      <ErrorBoundaryRoute path={match.url} component={PledgePayment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PledgePaymentDeleteDialog} />
  </>
);

export default Routes;
