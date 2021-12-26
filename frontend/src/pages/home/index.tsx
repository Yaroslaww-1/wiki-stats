import React from "react";

import { PageComponent } from "@components/page";

import { AllUsersStatsContainer } from "./features/all-users-stats/containers";
import { AllWikisStatsContainer } from "./features/all-wikis-stats/containers";
import { SubscribedUserRecentChangesListContainer } from "./features/subscribed-user-stats/containers/subscribed-user-recent-changes-list";
import { AllRecentChangesListContainer } from "./features/all-recent-changes/containers";
import { SubscribedUserChangesGraphContainer } from "./features/subscribed-user-stats/containers/subscribed-user-changes-graph";

import styles from "./styles.module.scss";
import { ResetContainer } from "./features/reset/containers";

export const HomePage: React.FC = () => {
  return (
    <PageComponent>
      <div className={styles.root}>
        <div className={styles.usersWikisInfo}>
          <AllUsersStatsContainer />
          <AllWikisStatsContainer />
          <SubscribedUserChangesGraphContainer />
          <ResetContainer />
        </div>
        <div className={styles.changesInfo}>
          <SubscribedUserRecentChangesListContainer />
          <AllRecentChangesListContainer />
        </div>
      </div>
    </PageComponent>
  );
};
