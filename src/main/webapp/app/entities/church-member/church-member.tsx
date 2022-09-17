import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChurchMember } from 'app/shared/model/church-member.model';
import { getEntities } from './church-member.reducer';

export const ChurchMember = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const churchMemberList = useAppSelector(state => state.churchMember.entities);
  const loading = useAppSelector(state => state.churchMember.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="church-member-heading" data-cy="ChurchMemberHeading">
        <Translate contentKey="waciErpApp.churchMember.home.title">Church Members</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="waciErpApp.churchMember.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/church-member/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="waciErpApp.churchMember.home.createLabel">Create new Church Member</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {churchMemberList && churchMemberList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.fullName">Full Name</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.phoneNumber">Phone Number</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="waciErpApp.churchMember.endDate">End Date</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {churchMemberList.map((churchMember, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/church-member/${churchMember.id}`} color="link" size="sm">
                      {churchMember.id}
                    </Button>
                  </td>
                  <td>{churchMember.fullName}</td>
                  <td>{churchMember.phoneNumber}</td>
                  <td>{churchMember.date}</td>
                  <td>
                    {churchMember.startDate ? (
                      <TextFormat type="date" value={churchMember.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {churchMember.endDate ? <TextFormat type="date" value={churchMember.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/church-member/${churchMember.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/church-member/${churchMember.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/church-member/${churchMember.id}/delete`}
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
              <Translate contentKey="waciErpApp.churchMember.home.notFound">No Church Members found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ChurchMember;
