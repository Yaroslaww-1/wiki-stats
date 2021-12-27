import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  wikisStats: {
    changesCount: number;
    wikiName: string;
  }[];
}

export const TopWikisByChangesComponent: React.FC<IProps> = ({
  wikisStats = [],
}) => {
  return (
    <>
      {wikisStats.length > 0 && (
        <div className={styles.root}>
          <div className={styles.header}>
            Top {wikisStats.length} user wikis by changes
          </div>
          <div className={styles.table}>
            <table>
              {wikisStats.map(stats => (
                <tr key={stats.wikiName}>
                  <th><strong>{stats.wikiName}</strong></th>
                  <th>{stats.changesCount}</th>
                </tr>
              ))}
            </table>
          </div>
        </div>
      )}
    </>
  );
};
