import { TableComponent } from "pages/home/components/table";
import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  topUsers: {
    userName: string;
    changesCount: number;
  }[];
}

export const TopUsersComponent: React.FC<IProps> = ({ topUsers }) => {
  return (
    <>
      {topUsers.length > 0 && (
        <div className={styles.root}>
          <div className={styles.header}>
            Top {topUsers.length} users by changes
          </div>
          <TableComponent
            data={topUsers}
            keys={["userName", "changesCount"]}
          />
        </div>
      )}
    </>
  );
};
