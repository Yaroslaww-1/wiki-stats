import { TableComponent } from "pages/home/components/table";
import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  wikisStats: {
    wikiName: string;
    changesCount: number;
  }[];
}

export const TopWikisByChangesComponent: React.FC<IProps> = ({
  wikisStats = [],
}) => {
  console.log(wikisStats);
  return (
    <>
      {wikisStats.length > 0 && (
        <div className={styles.root}>
          <div className={styles.header}>
            Top {wikisStats.length} user wikis by changes
          </div>
          <TableComponent
            data={wikisStats}
            keys={["wikiName", "changesCount"]}
          />
        </div>
      )}
    </>
  );
};
