import React from "react";

import { ChangeComponent } from "../change";

import styles from "./styles.module.scss";

interface IProps {
  recentChanges: {
    id: string;
    title: string;
    comment: string;
    userName: string;
    wikiName: string;
  }[];
}

export const RecentChangesListComponent: React.FC<IProps> = ({
  recentChanges,
}) => {
  return (
    <div className={styles.root}>
      {recentChanges.map(change => (
        <div key={change.id} className={styles.change}>
          <ChangeComponent
            title={change.title}
            comment={change.comment}
            wikiName={change.wikiName}
            editorName={change.userName}
          />
        </div>
      ))}
    </div>
  );
};
