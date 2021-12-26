import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import { EditComponent } from "../edit";

import styles from "./styles.module.scss";

interface IProps {
  recentEdits: IEditModel[];
}

export const RecentEditsListComponent: React.FC<IProps> = ({
  recentEdits,
}) => {
  return (
    <div className={styles.root}>
      {recentEdits.map(edit => (
        <div key={edit.id} className={styles.edit}>
          <EditComponent
            title={edit.title}
            comment={edit.comment}
            wikiName={edit.wiki.name}
            editorName={edit.editor.name}
          />
        </div>
      ))}
    </div>
  );
};
