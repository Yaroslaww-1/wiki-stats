import React from "react";

import { PageComponent } from "@components/page";

import { UsersInfoContainer } from "./features/users-info/containers/users-info";
import { WikisInfoContainer } from "./features/wikis-info/containers/wikis-info";

import styles from "./styles.module.scss";
import { EditsInfoContainer } from "./features/edits-info/containers/edits-info";

export const HomePage: React.FC = () => {
  return (
    <PageComponent>
      <div className={styles.root}>
        <UsersInfoContainer />
        <WikisInfoContainer />
        <EditsInfoContainer />
      </div>
    </PageComponent>
  );
};
