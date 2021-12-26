import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { SubscribedUserRecentEditsListComponent } from "../../components/subscribed-user-recent-edits-list";

interface IProps {
  state: SubscribedUserStatsState;
}

const SubscribedUserRecentEditsListContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <SubscribedUserRecentEditsListComponent
      recentEdits={state.recentEdits}
      keepEdits={state.keepEdits}
      setKeepEdits={state.setKeepEdits}
      subscribedUserName={state.subscribedUserName}
      setSubscribedUserName={state.subscribeForUserEdits}
    />
  );
});

export const SubscribedUserRecentEditsListContainer: React.FC = () => (
  <SubscribedUserRecentEditsListContainerInner state={subscribedUserStatsState}/>
);

