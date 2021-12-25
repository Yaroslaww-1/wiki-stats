import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import styles from "./styles.module.scss";
import { EditInfoComponent } from "pages/home/components/edit-info";

interface IProps {
  lastCreatedEdits: IEditModel[];
  keepEdits: number;
  setKeepEdits: (keepEdits: number) => void;
  subscribedUserName: string;
  setSubscribedUserName: (subscribedUserName: string) => void;
  subscribeForUserEdits: () => void;
}

export const EditsInfoComponent: React.FC<IProps> = ({
  lastCreatedEdits,
  keepEdits,
  setKeepEdits,
  subscribedUserName,
  setSubscribedUserName,
  subscribeForUserEdits,
}) => {
  const onKeepEditsValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeepEdits(parseInt(event.target.value) || 0);
  };

  const onSubscribedUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSubscribedUserName(event.target.value || "");
  };

  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <div className={styles.keepEdits}>
          <span>Keep <strong>edits:</strong></span>
          <input value={keepEdits} onChange={onKeepEditsValueChange}></input>
        </div>
        <div>
          <input value={subscribedUserName} onChange={onSubscribedUserNameChange}></input>
        </div>
        <button onClick={subscribeForUserEdits}>Subscribe</button>
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
