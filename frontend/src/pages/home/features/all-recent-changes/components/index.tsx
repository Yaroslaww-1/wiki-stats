import React from "react";

import { IChangeModel } from "@api/changes/change.model";

import { RecentChangesListComponent } from "pages/home/components/recent-changes-list";
import { KeepChangesInputComponent } from "pages/home/components/keep-changes-input";
import { ChangesProcessingInputComponent } from "pages/home/components/changes-processing-delay-input";

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
  processingDelay: number;
  setProcessingDelay: (processingDelay: number) => void;
}

export const AllRecentChangesListComponent: React.FC<IProps> = ({
  recentChanges,
  keepChanges,
  setKeepChanges,
  processingDelay,
  setProcessingDelay,
}) => {
  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <KeepChangesInputComponent initialKeepChanges={keepChanges} setKeepChanges={setKeepChanges} />
        <ChangesProcessingInputComponent initialDelay={processingDelay} setDelay={setProcessingDelay} />
      </div>
      <RecentChangesListComponent recentChanges={recentChanges} />
    </div>
  );
};
