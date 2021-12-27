import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  addCount: number;
  editCount: number;
}

export const SubscribedUserChangesAggregateStatsComponent: React.FC<IProps> = ({
  addCount,
  editCount,
}) => {
  return (
    <div className={styles.root}>
      <div>Add: <strong>{addCount}</strong></div>
      <div>Edit: <strong>{editCount}</strong></div>
    </div>
  );
};
