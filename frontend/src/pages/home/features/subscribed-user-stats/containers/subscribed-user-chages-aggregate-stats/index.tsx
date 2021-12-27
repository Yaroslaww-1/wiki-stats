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
      {state.subscribedUserChangesAggregateStats && (
        <SubscribedUserChangesAggregateStatsComponent
          addCount={state.subscribedUserChangesAggregateStats.addCount}
          editCount={state.subscribedUserChangesAggregateStats.editCount}
        />
      )}
    </>
  );
});

export const SubscribedUserChangesAggregateStatsContainer: React.FC = () => (
  <SubscribedUserChangesAggregateStatsContainerInner state={subscribedUserStatsState}/>
);

