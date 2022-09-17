import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProgram } from 'app/shared/model/program.model';
import { getEntities as getPrograms } from 'app/entities/program/program.reducer';
import { IPledge } from 'app/shared/model/pledge.model';
import { getEntity, updateEntity, createEntity, reset } from './pledge.reducer';

export const PledgeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const programs = useAppSelector(state => state.program.entities);
  const pledgeEntity = useAppSelector(state => state.pledge.entity);
  const loading = useAppSelector(state => state.pledge.loading);
  const updating = useAppSelector(state => state.pledge.updating);
  const updateSuccess = useAppSelector(state => state.pledge.updateSuccess);
  const handleClose = () => {
    props.history.push('/pledge');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPrograms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...pledgeEntity,
      ...values,
      program: programs.find(it => it.id.toString() === values.program.toString()),
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
          ...pledgeEntity,
          program: pledgeEntity?.program?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="waciErpApp.pledge.home.createOrEditLabel" data-cy="PledgeCreateUpdateHeading">
            <Translate contentKey="waciErpApp.pledge.home.createOrEditLabel">Create or edit a Pledge</Translate>
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
                  id="pledge-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('waciErpApp.pledge.amount')}
                id="pledge-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('waciErpApp.pledge.date')}
                id="pledge-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('waciErpApp.pledge.memberName')}
                id="pledge-memberName"
                name="memberName"
                data-cy="memberName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="pledge-program"
                name="program"
                data-cy="program"
                label={translate('waciErpApp.pledge.program')}
                type="select"
              >
                <option value="" key="0" />
                {programs
                  ? programs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pledge" replace color="info">
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

export default PledgeUpdate;
