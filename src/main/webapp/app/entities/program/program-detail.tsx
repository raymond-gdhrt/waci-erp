import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './program.reducer';

export const ProgramDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const programEntity = useAppSelector(state => state.program.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="programDetailsHeading">
          <Translate contentKey="waciErpApp.program.detail.title">Program</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{programEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="waciErpApp.program.name">Name</Translate>
            </span>
          </dt>
          <dd>{programEntity.name}</dd>
          <dt>
            <span id="minAmount">
              <Translate contentKey="waciErpApp.program.minAmount">Min Amount</Translate>
            </span>
          </dt>
          <dd>{programEntity.minAmount}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="waciErpApp.program.description">Description</Translate>
            </span>
          </dt>
          <dd>{programEntity.description}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="waciErpApp.program.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {programEntity.startDate ? <TextFormat value={programEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="waciErpApp.program.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{programEntity.endDate ? <TextFormat value={programEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/program" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/program/${programEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProgramDetail;
