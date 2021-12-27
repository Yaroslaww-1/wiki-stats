import React from "react";
import { observer } from "mobx-react-lite";

import { SubscribedUserStatsState, subscribedUserStatsState } from "../../subscribed-user-stats.state";
import { TopWikisByChangesComponent } from "../../components/top-wikis-by-changes";

interface IProps {
  state: SubscribedUserStatsState;
}

const TopWikisByChangesContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <TopWikisByChangesComponent
      wikisStats={state.subscribedUserTopWikis}
    />
  );
});

export const TopWikisByChangesContainer: React.FC = () => (
  <TopWikisByChangesContainerInner state={subscribedUserStatsState}/>
);

