import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  name: string;
}

export const WikiComponent: React.FC<IProps> = ({ name }) => {
  return (
    <div className={styles.root}>
      <strong>{name}</strong>
    </div>
  );
};
