import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pledge-payment.reducer';

export const PledgePaymentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const pledgePaymentEntity = useAppSelector(state => state.pledgePayment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pledgePaymentDetailsHeading">
          <Translate contentKey="waciErpApp.pledgePayment.detail.title">PledgePayment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pledgePaymentEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="waciErpApp.pledgePayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{pledgePaymentEntity.amount}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="waciErpApp.pledgePayment.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {pledgePaymentEntity.date ? <TextFormat value={pledgePaymentEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="memberName">
              <Translate contentKey="waciErpApp.pledgePayment.memberName">Member Name</Translate>
            </span>
          </dt>
          <dd>{pledgePaymentEntity.memberName}</dd>
          <dt>
            <Translate contentKey="waciErpApp.pledgePayment.pledge">Pledge</Translate>
          </dt>
          <dd>{pledgePaymentEntity.pledge ? pledgePaymentEntity.pledge.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pledge-payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pledge-payment/${pledgePaymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PledgePaymentDetail;
