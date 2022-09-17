import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/program">
        <Translate contentKey="global.menu.entities.program" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/church-member">
        <Translate contentKey="global.menu.entities.churchMember" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pledge">
        <Translate contentKey="global.menu.entities.pledge" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pledge-payment">
        <Translate contentKey="global.menu.entities.pledgePayment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
