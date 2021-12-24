import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import styles from "./styles.module.scss";
import { EditInfoComponent } from "pages/home/components/edit-info";

interface IProps {
  lastCreatedEdits: IEditModel[];
  keepEdits: number;
  setKeepEdits: (newKeepEdits: number) => void;
}

export const EditsInfoComponent: React.FC<IProps> = ({ lastCreatedEdits, keepEdits, setKeepEdits }) => {
  const onKeepEditsValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeepEdits(parseInt(event.target.value));
  };

  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <span>Keep edits:</span>
        <input value={keepEdits} onChange={onKeepEditsValueChange}></input>
      </div>
      <div className={styles.edits}>
        {lastCreatedEdits.map(edit => (
          <EditInfoComponent
            key={edit.id}
            title={edit.title}
            comment={edit.comment}
            wikiName={edit.wiki.name}
            editorName={edit.editor.name}
          />
        ))}
      </div>
    </div>
  );
};
