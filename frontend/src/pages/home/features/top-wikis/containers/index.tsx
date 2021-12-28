import React from "react";
import { observer } from "mobx-react-lite";

import { TopWikisChangesStatsState, topWikisChangesStatsState } from "../top-wikis.state";
import { TopWikisTableComponent } from "../components/top-wikis-table";

interface IProps {
  state: TopWikisChangesStatsState;
}

const TopWikisContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <div>
      <TopWikisTableComponent
        topWikis={state.topWikis}
      />
    </div>
  );
});

export const TopWikisContainer: React.FC = () => (
  <TopWikisContainerInner state={topWikisChangesStatsState}/>
);

