import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './church-member.reducer';

export const ChurchMemberDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const churchMemberEntity = useAppSelector(state => state.churchMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="churchMemberDetailsHeading">
          <Translate contentKey="waciErpApp.churchMember.detail.title">ChurchMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{churchMemberEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="waciErpApp.churchMember.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{churchMemberEntity.fullName}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="waciErpApp.churchMember.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{churchMemberEntity.phoneNumber}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="waciErpApp.churchMember.date">Date</Translate>
            </span>
          </dt>
          <dd>{churchMemberEntity.date}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="waciErpApp.churchMember.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {churchMemberEntity.startDate ? (
              <TextFormat value={churchMemberEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="waciErpApp.churchMember.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {churchMemberEntity.endDate ? (
              <TextFormat value={churchMemberEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/church-member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/church-member/${churchMemberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChurchMemberDetail;
