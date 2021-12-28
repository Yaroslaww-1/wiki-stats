import { TableComponent } from "pages/home/components/table";
import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  topWikis: {
    wikiName: string;
    editsCount: number;
  }[];
}

export const TopWikisTableComponent: React.FC<IProps> = ({ topWikis }) => {
  return (
    <>
      {topWikis.length > 0 && (
        <div className={styles.root}>
          <div className={styles.header}>
            Top {topWikis.length} wikis by edits
          </div>
          <TableComponent
            data={topWikis}
            keys={["wikiName", "editsCount"]}
          />
        </div>
      )}
    </>
  );
};
