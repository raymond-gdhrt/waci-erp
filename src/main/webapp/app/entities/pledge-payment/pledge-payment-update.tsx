import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPledge } from 'app/shared/model/pledge.model';
import { getEntities as getPledges } from 'app/entities/pledge/pledge.reducer';
import { IPledgePayment } from 'app/shared/model/pledge-payment.model';
import { getEntity, updateEntity, createEntity, reset } from './pledge-payment.reducer';

export const PledgePaymentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const pledges = useAppSelector(state => state.pledge.entities);
  const pledgePaymentEntity = useAppSelector(state => state.pledgePayment.entity);
  const loading = useAppSelector(state => state.pledgePayment.loading);
  const updating = useAppSelector(state => state.pledgePayment.updating);
  const updateSuccess = useAppSelector(state => state.pledgePayment.updateSuccess);
  const handleClose = () => {
    props.history.push('/pledge-payment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPledges({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...pledgePaymentEntity,
      ...values,
      pledge: pledges.find(it => it.id.toString() === values.pledge.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pledgePaymentEntity,
          pledge: pledgePaymentEntity?.pledge?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="waciErpApp.pledgePayment.home.createOrEditLabel" data-cy="PledgePaymentCreateUpdateHeading">
            <Translate contentKey="waciErpApp.pledgePayment.home.createOrEditLabel">Create or edit a PledgePayment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="pledge-payment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('waciErpApp.pledgePayment.amount')}
                id="pledge-payment-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('waciErpApp.pledgePayment.date')}
                id="pledge-payment-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('waciErpApp.pledgePayment.memberName')}
                id="pledge-payment-memberName"
                name="memberName"
                data-cy="memberName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="pledge-payment-pledge"
                name="pledge"
                data-cy="pledge"
                label={translate('waciErpApp.pledgePayment.pledge')}
                type="select"
              >
                <option value="" key="0" />
                {pledges
                  ? pledges.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pledge-payment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PledgePaymentUpdate;
