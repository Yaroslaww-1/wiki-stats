import React from "react";

import { IWikiModel } from "@api/wikis/wiki.model";
import { WikiInfoComponent } from "pages/home/components/wiki-info";

interface IProps {
  totalWikisCount: number;
  lastCreatedWiki: IWikiModel | null;
}

export const WikisInfoComponent: React.FC<IProps> = ({ totalWikisCount, lastCreatedWiki }) => {
  return (
    <div>
      <div>
        Total wikis count: {totalWikisCount}
      </div>
      <div>
        Last created wiki:
      </div>
      {lastCreatedWiki && <WikiInfoComponent name={lastCreatedWiki.name} />}
    </div>
  );
};
