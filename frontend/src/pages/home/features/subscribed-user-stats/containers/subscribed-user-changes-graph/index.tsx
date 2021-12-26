import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { ChangesGraphComponent } from "../../components/changes-graph";

interface IProps {
  state: SubscribedUserStatsState;
}

const SubscribedUserChangesGraphContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <ChangesGraphComponent
      userChangeStats={state.subscribedUserChangesStatsParts}
      initialWindow={state.changesStatsWindow}
      initialStep={state.changesStatsStep}
      setOptions={state.setChangeStatsWindow}
    />
  );
});

export const SubscribedUserChangesGraphContainer: React.FC = () => (
  <SubscribedUserChangesGraphContainerInner state={subscribedUserStatsState}/>
);
