import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { SubscribedUserRecentChangesListComponent } from "../../components/subscribed-user-recent-changes-list";

interface IProps {
  state: SubscribedUserStatsState;
}

const SubscribedUserRecentChangesListContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <SubscribedUserRecentChangesListComponent
      recentChanges={state.recentChanges}
      keepChanges={state.keepChanges}
      setKeepChanges={state.setKeepChanges}
      subscribedUserName={state.subscribedUserName}
      setSubscribedUserName={state.subscribeForUserChanges}
    />
  );
});

export const SubscribedUserRecentChangesListContainer: React.FC = () => (
  <SubscribedUserRecentChangesListContainerInner state={subscribedUserStatsState}/>
);

