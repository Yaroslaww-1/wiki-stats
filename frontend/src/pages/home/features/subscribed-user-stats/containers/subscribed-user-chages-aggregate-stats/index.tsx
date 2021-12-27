import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { SubscribedUserChangesAggregateStatsComponent } from "../../components/subscribed-user-chages-aggregate-stats";

interface IProps {
  state: SubscribedUserStatsState;
}

const SubscribedUserChangesAggregateStatsContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <>
      {state.subscribedUserAggregateStats && (
        <SubscribedUserChangesAggregateStatsComponent
          addCount={state.subscribedUserAggregateStats.addCount}
          editCount={state.subscribedUserAggregateStats.editCount}
        />
      )}
    </>
  );
});

export const SubscribedUserChangesAggregateStatsContainer: React.FC = () => (
  <SubscribedUserChangesAggregateStatsContainerInner state={subscribedUserStatsState}/>
);

