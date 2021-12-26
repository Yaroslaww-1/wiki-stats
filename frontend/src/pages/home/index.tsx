import React from "react";

import { PageComponent } from "@components/page";

import { AllUsersStatsContainer } from "./features/all-users-stats/containers";
import { AllWikisStatsContainer } from "./features/all-wikis-stats/containers";
import { SubscribedUserRecentEditsListContainer } from "./features/subscribed-user-stats/containers/subscribed-user-recent-edits-list";
import { AllRecentEditsListContainer } from "./features/all-recent-edits/containers";
import { SubscribedUserEditsGraphContainer } from "./features/subscribed-user-stats/containers/subscribed-user-edits-graph";

import styles from "./styles.module.scss";
import { ResetContainer } from "./features/reset/containers";

export const HomePage: React.FC = () => {
  return (
    <PageComponent>
      <div className={styles.root}>
        <div className={styles.usersWikisInfo}>
          <AllUsersStatsContainer />
          <AllWikisStatsContainer />
          <SubscribedUserEditsGraphContainer />
          <ResetContainer />
        </div>
        <div className={styles.editsInfo}>
          <SubscribedUserRecentEditsListContainer />
          <AllRecentEditsListContainer />
        </div>
      </div>
    </PageComponent>
  );
};
