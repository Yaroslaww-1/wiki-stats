import React from "react";
import { observer } from "mobx-react-lite";

import { WikisInfoState, wikisInfoState } from "../../wikis-info.state";
import { WikisInfoComponent } from "../../components/wikis-info";


interface IProps {
  state: WikisInfoState;
}

const WikisInfoContainerInner: React.FC<IProps> = observer(({ state }) => {
  return (
    <WikisInfoComponent
      totalWikisCount={state.totalWikisCount}
      lastCreatedWiki={state.lastCreatedWiki}
    />
  );
});

export const WikisInfoContainer: React.FC = () => (
  <WikisInfoContainerInner state={wikisInfoState}/>
);
