import React from "react";

import { PageComponent } from "@components/page";

import { UsersInfoContainer } from "./features/users-info/containers/users-info";
import { WikisInfoContainer } from "./features/wikis-info/containers/wikis-info";

import styles from "./styles.module.scss";
import { EditsInfoContainer } from "./features/edits-info/containers/edits-info";
import { SubscribedUserEditsContainer } from "./features/subscribed-user-edits/containers/edits-info";
import { SubscribedUserEditsGraphContainer } from "./features/subscribed-user-edits/containers/edits-graph";

export const HomePage: React.FC = () => {
  return (
    <PageComponent>
      <div className={styles.root}>
        <div className={styles.usersWikisInfo}>
          <UsersInfoContainer />
          <WikisInfoContainer />
          <SubscribedUserEditsContainer />
        </div>
        <div className={styles.editsInfo}>
          <EditsInfoContainer />
          <SubscribedUserEditsGraphContainer />
        </div>
      </div>
    </PageComponent>
  );
};
