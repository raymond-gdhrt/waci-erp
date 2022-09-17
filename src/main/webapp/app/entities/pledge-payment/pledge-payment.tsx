import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPledgePayment } from 'app/shared/model/pledge-payment.model';
import { getEntities } from './pledge-payment.reducer';

export const PledgePayment = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const pledgePaymentList = useAppSelector(state => state.pledgePayment.entities);
  const loading = useAppSelector(state => state.pledgePayment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="pledge-payment-heading" data-cy="PledgePaymentHeading">
        <Translate contentKey="waciErpApp.pledgePayment.home.title">Pledge Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="waciErpApp.pledgePayment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pledge-payment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="waciErpApp.pledgePayment.home.createLabel">Create new Pledge Payment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pledgePaymentList && pledgePaymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="waciErpApp.pledgePayment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledgePayment.amount">Amount</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledgePayment.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledgePayment.memberName">Member Name</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledgePayment.pledge">Pledge</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pledgePaymentList.map((pledgePayment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pledge-payment/${pledgePayment.id}`} color="link" size="sm">
                      {pledgePayment.id}
                    </Button>
                  </td>
                  <td>{pledgePayment.amount}</td>
                  <td>
                    {pledgePayment.date ? <TextFormat type="date" value={pledgePayment.date} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{pledgePayment.memberName}</td>
                  <td>{pledgePayment.pledge ? <Link to={`/pledge/${pledgePayment.pledge.id}`}>{pledgePayment.pledge.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pledge-payment/${pledgePayment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pledge-payment/${pledgePayment.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pledge-payment/${pledgePayment.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="waciErpApp.pledgePayment.home.notFound">No Pledge Payments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PledgePayment;
