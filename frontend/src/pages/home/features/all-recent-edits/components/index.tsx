import React from "react";

import { IEditModel } from "@api/edits/edit.model";

import { RecentEditsListComponent } from "pages/home/components/recent-edits-list";
import { KeepEditsInputComponent } from "pages/home/components/keep-edits-input";
import { EditsProcessingInputComponent } from "pages/home/components/edits-processing-delay-input";

import styles from "./styles.module.scss";

interface IProps {
  recentEdits: IEditModel[];
  keepEdits: number;
  setKeepEdits: (keepEdits: number) => void;
  processingDelay: number;
  setProcessingDelay: (processingDelay: number) => void;
}

export const AllRecentEditsListComponent: React.FC<IProps> = ({
  recentEdits,
  keepEdits,
  setKeepEdits,
  processingDelay,
  setProcessingDelay,
}) => {
  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <KeepEditsInputComponent initialKeepEdits={keepEdits} setKeepEdits={setKeepEdits} />
        <EditsProcessingInputComponent initialDelay={processingDelay} setDelay={setProcessingDelay} />
      </div>
      <div className={styles.edits}>
        <RecentEditsListComponent recentEdits={recentEdits} />
      </div>
    </div>
  );
};
