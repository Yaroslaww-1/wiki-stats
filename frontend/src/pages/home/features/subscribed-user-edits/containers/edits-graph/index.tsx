import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserEditsInfoState, subscribedUserEditsInfoState } from "../../subscribed-user-edits.state";
import { EditsGraphComponent } from "../../components/edits-graph";

interface IProps {
  state: SubscribedUserEditsInfoState;
}

const SubscribedUserEditsGraphContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <EditsGraphComponent
      userEditStats={state.subscribedUserEditsStats?.parts}
      window={state.editsStatsWindow}
      setWindow={state.setEditStatsWindow}
    />
  );
});

export const SubscribedUserEditsGraphContainer: React.FC = () => (
  <SubscribedUserEditsGraphContainerInner state={subscribedUserEditsInfoState}/>
);
