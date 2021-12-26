import React from "react";

import { IChangeModel } from "@api/changes/change.model";

import { ChangeComponent } from "../change";

import styles from "./styles.module.scss";

interface IProps {
  recentChanges: IChangeModel[];
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
            wikiName={change.wiki.name}
            editorName={change.editor.name}
          />
        </div>
      ))}
    </div>
  );
};
