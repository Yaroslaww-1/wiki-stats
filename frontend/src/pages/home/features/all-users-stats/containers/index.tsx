import React from "react";
import { observer } from "mobx-react-lite";

import { AllUsersStatsState, allUsersStatsState } from "../all-users-stats.state";

import { AllUsersStatsComponent } from "../components";

interface IProps {
  state: AllUsersStatsState;
}

const AllUsersStatsContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <AllUsersStatsComponent
      totalUsersCount={state.totalUsersCount}
      lastCreatedUser={state.lastCreatedUser}
    />
  );
});

export const AllUsersStatsContainer: React.FC = () => (
  <AllUsersStatsContainerInner state={allUsersStatsState}/>
);
