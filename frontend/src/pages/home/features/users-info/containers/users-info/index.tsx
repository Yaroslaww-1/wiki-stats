import React from "react";
import { observer } from "mobx-react-lite";

import { UsersInfoState, usersInfoState } from "../../users-info.state";

import { UsersInfoComponent } from "../../components/users-info";

interface IProps {
  state: UsersInfoState;
}

const UsersInfoContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <UsersInfoComponent
      totalUsersCount={state.totalUsersCount}
      lastCreatedUser={state.lastCreatedUser}
    />
  );
});

export const UsersInfoContainer: React.FC = () => (
  <UsersInfoContainerInner state={usersInfoState}/>
);
