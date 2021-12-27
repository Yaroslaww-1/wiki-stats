import React from "react";

import { RecentChangesListComponent } from "pages/home/components/recent-changes-list";
import { KeepChangesInputComponent } from "pages/home/components/keep-changes-input";
import { SubscribeUserInputComponent } from "../subscribe-user-input";

import styles from "./styles.module.scss";

interface IProps {
  recentChanges: {
    id: string;
    timestamp: Date;
    title: string;
    comment: string;
    userName: string;
    wikiName: string;
  }[];
  keepChanges: number;
  setKeepChanges: (keepChanges: number) => void;
  subscribedUserName: string;
  setSubscribedUserName: (subscribedUserName: string) => void;
}

export const SubscribedUserRecentChangesListComponent: React.FC<IProps> = ({
  recentChanges,
  keepChanges,
  setKeepChanges,
  subscribedUserName,
  setSubscribedUserName,
}) => {
  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <KeepChangesInputComponent initialKeepChanges={keepChanges} setKeepChanges={setKeepChanges} />
        <SubscribeUserInputComponent initialUserName={subscribedUserName} setUserName={setSubscribedUserName} />
      </div>
      <RecentChangesListComponent recentChanges={recentChanges} />
    </div>
  );
};
