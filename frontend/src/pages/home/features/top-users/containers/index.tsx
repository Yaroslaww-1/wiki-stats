import React from "react";
import { observer } from "mobx-react-lite";

import { TopUsersChangesStatsState, topUsersChangesStatsState } from "../top-users.state";
import { TopUsersTableComponent } from "../components/top-users-table";
import { TopUsersIntervalSelectionComponent } from "../components/top-users-interval-selection";

interface IProps {
  state: TopUsersChangesStatsState;
}

const TopUsersContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <div>
      <TopUsersIntervalSelectionComponent
        setInterval={state.setTopUsersInterval}
      />
      <TopUsersTableComponent
        topUsers={state.topUsers}
      />
    </div>
  );
});

export const TopUsersContainer: React.FC = () => (
  <TopUsersContainerInner state={topUsersChangesStatsState}/>
);

