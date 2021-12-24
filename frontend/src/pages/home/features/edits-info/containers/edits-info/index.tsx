import React from "react";
import { observer } from "mobx-react-lite";

import { EditsInfoState, editsInfoState } from "../../edits-info.state";
import { EditsInfoComponent } from "../../components/edits-info";

interface IProps {
  state: EditsInfoState;
}

const EditsInfoContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <EditsInfoComponent
      lastCreatedEdits={state.lastCreatedEdits}
      keepEdits={state.keepEdits}
      setKeepEdits={state.setKeepEdits}
      processingDelay={state.processingDelay}
      setProcessingDelay={state.setProcessingDelay}
    />
  );
});

export const EditsInfoContainer: React.FC = () => (
  <EditsInfoContainerInner state={editsInfoState}/>
);
