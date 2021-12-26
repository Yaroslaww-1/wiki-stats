import React from "react";
import { observer } from "mobx-react-lite";

import { AllRecentEditsState, allRecentEditsState } from "../all-recent-edits.state";
import { AllRecentEditsListComponent } from "../components";

interface IProps {
  state: AllRecentEditsState;
}

const AllRecentEditsListContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <AllRecentEditsListComponent
      recentEdits={state.recentEdits}
      keepEdits={state.keepEdits}
      setKeepEdits={state.setKeepEdits}
      processingDelay={state.processingDelay}
      setProcessingDelay={state.setProcessingDelay}
    />
  );
});

export const AllRecentEditsListContainer: React.FC = () => (
  <AllRecentEditsListContainerInner state={allRecentEditsState}/>
);
