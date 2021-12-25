import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserEditsInfoState, subscribedUserEditsInfoState } from "../../subscribed-user-edits.state";
import { EditsInfoComponent } from "../../components/edits-info";

interface IProps {
  state: SubscribedUserEditsInfoState;
}

const SubscribedUserEditsContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <EditsInfoComponent
      lastCreatedEdits={state.lastCreatedEdits}
      keepEdits={state.keepEdits}
      setKeepEdits={state.setKeepEdits}
      subscribedUserName={state.subscribedUserName}
      setSubscribedUserName={state.setSubscribedUserName}
      subscribeForUserEdits={state.subscribeForUserEdits}
    />
  );
});

export const SubscribedUserEditsContainer: React.FC = () => (
  <SubscribedUserEditsContainerInner state={subscribedUserEditsInfoState}/>
);
