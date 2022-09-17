import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPledge } from 'app/shared/model/pledge.model';
import { getEntities } from './pledge.reducer';

export const Pledge = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const pledgeList = useAppSelector(state => state.pledge.entities);
  const loading = useAppSelector(state => state.pledge.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="pledge-heading" data-cy="PledgeHeading">
        <Translate contentKey="waciErpApp.pledge.home.title">Pledges</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="waciErpApp.pledge.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pledge/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="waciErpApp.pledge.home.createLabel">Create new Pledge</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pledgeList && pledgeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="waciErpApp.pledge.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledge.amount">Amount</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledge.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledge.memberName">Member Name</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.pledge.program">Program</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pledgeList.map((pledge, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pledge/${pledge.id}`} color="link" size="sm">
                      {pledge.id}
                    </Button>
                  </td>
                  <td>{pledge.amount}</td>
                  <td>{pledge.date ? <TextFormat type="date" value={pledge.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{pledge.memberName}</td>
                  <td>{pledge.program ? <Link to={`/program/${pledge.program.id}`}>{pledge.program.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pledge/${pledge.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pledge/${pledge.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pledge/${pledge.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="waciErpApp.pledge.home.notFound">No Pledges found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Pledge;
