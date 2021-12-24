import React from "react";

import { IWikiModel } from "@api/wikis/wiki.model";
import { WikiInfoComponent } from "pages/home/components/wiki-info";

import styles from "./styles.module.scss";

interface IProps {
  totalWikisCount: number;
  lastCreatedWiki: IWikiModel | null;
}

export const WikisInfoComponent: React.FC<IProps> = ({ totalWikisCount, lastCreatedWiki }) => {
  return (
    <div className={styles.root}>
      <div className={styles.total}>
        <span>Total wikis count: </span>
        <strong>{totalWikisCount}</strong>
      </div>
      <div className={styles.lastCreatedWiki}>
        Last created wiki:
      </div>
      {lastCreatedWiki && <WikiInfoComponent name={lastCreatedWiki.name} />}
    </div>
  );
};
