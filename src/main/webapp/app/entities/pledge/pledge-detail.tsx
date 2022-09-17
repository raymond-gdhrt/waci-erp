import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pledge.reducer';

export const PledgeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const pledgeEntity = useAppSelector(state => state.pledge.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pledgeDetailsHeading">
          <Translate contentKey="waciErpApp.pledge.detail.title">Pledge</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pledgeEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="waciErpApp.pledge.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{pledgeEntity.amount}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="waciErpApp.pledge.date">Date</Translate>
            </span>
          </dt>
          <dd>{pledgeEntity.date ? <TextFormat value={pledgeEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="memberName">
              <Translate contentKey="waciErpApp.pledge.memberName">Member Name</Translate>
            </span>
          </dt>
          <dd>{pledgeEntity.memberName}</dd>
          <dt>
            <Translate contentKey="waciErpApp.pledge.program">Program</Translate>
          </dt>
          <dd>{pledgeEntity.program ? pledgeEntity.program.names : ''}</dd>
        </dl>
        <Button tag={Link} to="/pledge" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pledge/${pledgeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PledgeDetail;
