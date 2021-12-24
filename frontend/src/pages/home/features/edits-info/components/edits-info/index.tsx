import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import styles from "./styles.module.scss";
import { EditInfoComponent } from "pages/home/components/edit-info";

interface IProps {
  lastCreatedEdits: IEditModel[];
  keepEdits: number;
  setKeepEdits: (keepEdits: number) => void;
  processingDelay: number;
  setProcessingDelay: (processingDelay: number) => void;
}

export const EditsInfoComponent: React.FC<IProps> = ({
  lastCreatedEdits,
  keepEdits,
  setKeepEdits,
  processingDelay,
  setProcessingDelay,
}) => {
  const onKeepEditsValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeepEdits(parseInt(event.target.value) || 0);
  };

  const onProcessingDelayChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setProcessingDelay(parseInt(event.target.value) || 0);
  };

  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <div className={styles.keepEdits}>
          <span>Keep <strong>edits:</strong></span>
          <input value={keepEdits} onChange={onKeepEditsValueChange}></input>
        </div>
        <div className={styles.delay}>
          <span>Processing delay:</span>
          <input value={processingDelay} onChange={onProcessingDelayChange}></input>
        </div>
      </div>
      <div className={styles.edits}>
        {lastCreatedEdits.map(edit => (
          <div key={edit.id} className={styles.edit}>
            <EditInfoComponent
              title={edit.title}
              comment={edit.comment}
              wikiName={edit.wiki.name}
              editorName={edit.editor.name}
            />
          </div>
        ))}
      </div>
    </div>
  );
};
