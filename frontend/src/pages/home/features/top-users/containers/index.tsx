import React from "react";
import { observer } from "mobx-react-lite";

import { TopUsersChangesStatsState, topUsersChangesStatsState } from "../top-users.state";
import { TopUsersComponent } from "../components";

interface IProps {
  state: TopUsersChangesStatsState;
}

const TopUsersContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <TopUsersComponent
      topUsers={state.topUsers}
    />
  );
});

export const TopUsersContainer: React.FC = () => (
  <TopUsersContainerInner state={topUsersChangesStatsState}/>
);

