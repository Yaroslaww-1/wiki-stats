import React from "react";
import { observer } from "mobx-react-lite";

import { AllWikisStatsState, allWikisStatsState } from "../all-wikis-stats.state";
import { AllWikisStatsComponent } from "../components";

interface IProps {
  state: AllWikisStatsState;
}

const AllWikisStatsContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <AllWikisStatsComponent
      totalWikisCount={state.totalWikisCount}
      lastCreatedWiki={state.lastCreatedWiki}
    />
  );
});

export const AllWikisStatsContainer: React.FC = () => (
  <AllWikisStatsContainerInner state={allWikisStatsState}/>
);
