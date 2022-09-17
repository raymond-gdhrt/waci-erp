import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Program from './program';
import ChurchMember from './church-member';
import Pledge from './pledge';
import PledgePayment from './pledge-payment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}program`} component={Program} />
        <ErrorBoundaryRoute path={`${match.url}church-member`} component={ChurchMember} />
        <ErrorBoundaryRoute path={`${match.url}pledge`} component={Pledge} />
        <ErrorBoundaryRoute path={`${match.url}pledge-payment`} component={PledgePayment} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
