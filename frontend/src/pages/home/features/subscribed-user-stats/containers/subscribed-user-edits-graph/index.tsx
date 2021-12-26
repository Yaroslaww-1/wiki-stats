import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { EditsGraphComponent } from "../../components/edits-graph";

interface IProps {
  state: SubscribedUserStatsState;
}

const SubscribedUserEditsGraphContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <EditsGraphComponent
      userEditStats={state.subscribedUserEditsStatsParts}
      initialWindow={state.editsStatsWindow}
      initialStep={state.editsStatsStep}
      setOptions={state.setEditStatsWindow}
    />
  );
});

export const SubscribedUserEditsGraphContainer: React.FC = () => (
  <SubscribedUserEditsGraphContainerInner state={subscribedUserStatsState}/>
);
