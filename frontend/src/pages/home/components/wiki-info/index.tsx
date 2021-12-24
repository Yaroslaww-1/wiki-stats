import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  name: string;
}

export const WikiInfoComponent: React.FC<IProps> = ({ name }) => {
  return (
    <div className={styles.root}>
      <div>
        Name: {name}
      </div>
    </div>
  );
};
