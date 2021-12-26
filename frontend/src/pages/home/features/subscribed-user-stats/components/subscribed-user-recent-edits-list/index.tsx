import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import { RecentEditsListComponent } from "pages/home/components/recent-edits-list";
import { KeepEditsInputComponent } from "pages/home/components/keep-edits-input";
import { SubscribeUserInputComponent } from "../subscribe-user-input";

import styles from "./styles.module.scss";

interface IProps {
  recentEdits: IEditModel[];
  keepEdits: number;
  setKeepEdits: (keepEdits: number) => void;
  subscribedUserName: string;
  setSubscribedUserName: (subscribedUserName: string) => void;
}

export const SubscribedUserRecentEditsListComponent: React.FC<IProps> = ({
  recentEdits,
  keepEdits,
  setKeepEdits,
  subscribedUserName,
  setSubscribedUserName,
}) => {
  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <KeepEditsInputComponent initialKeepEdits={keepEdits} setKeepEdits={setKeepEdits} />
        <SubscribeUserInputComponent initialUserName={subscribedUserName} setUserName={setSubscribedUserName} />
      </div>
      <div className={styles.edits}>
        <RecentEditsListComponent recentEdits={recentEdits} />
      </div>
    </div>
  );
};
