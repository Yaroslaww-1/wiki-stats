import React from "react";
import { observer } from "mobx-react-lite";

import { AllRecentChangesState, allRecentChangesState } from "../all-recent-changes.state";
import { AllRecentChangesListComponent } from "../components";

interface IProps {
  state: AllRecentChangesState;
}

const AllRecentChangesListContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <AllRecentChangesListComponent
      recentChanges={state.recentChanges.map(c => ({
        ...c,
        userName: c.editor.name,
        wikiName: c.wiki.name,
      }))}
      keepChanges={state.keepChanges}
      setKeepChanges={state.setKeepChanges}
      processingDelay={state.processingDelay}
      setProcessingDelay={state.setProcessingDelay}
    />
  );
});

export const AllRecentChangesListContainer: React.FC = () => (
  <AllRecentChangesListContainerInner state={allRecentChangesState}/>
);
