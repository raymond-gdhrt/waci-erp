import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ChurchMember from './church-member';
import ChurchMemberDetail from './church-member-detail';
import ChurchMemberUpdate from './church-member-update';
import ChurchMemberDeleteDialog from './church-member-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ChurchMemberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ChurchMemberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ChurchMemberDetail} />
      <ErrorBoundaryRoute path={match.url} component={ChurchMember} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ChurchMemberDeleteDialog} />
  </>
);

export default Routes;
