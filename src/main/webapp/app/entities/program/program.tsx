import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProgram } from 'app/shared/model/program.model';
import { getEntities } from './program.reducer';

export const Program = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const programList = useAppSelector(state => state.program.entities);
  const loading = useAppSelector(state => state.program.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="program-heading" data-cy="ProgramHeading">
        <Translate contentKey="waciErpApp.program.home.title">Programs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="waciErpApp.program.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/program/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="waciErpApp.program.home.createLabel">Create new Program</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {programList && programList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="waciErpApp.program.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.program.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.program.minAmount">Min Amount</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.program.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.program.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.program.endDate">End Date</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {programList.map((program, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/program/${program.id}`} color="link" size="sm">
                      {program.id}
                    </Button>
                  </td>
                  <td>{program.name}</td>
                  <td>{program.minAmount}</td>
                  <td>{program.description}</td>
                  <td>{program.startDate ? <TextFormat type="date" value={program.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{program.endDate ? <TextFormat type="date" value={program.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/program/${program.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/program/${program.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/program/${program.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="waciErpApp.program.home.notFound">No Programs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Program;
