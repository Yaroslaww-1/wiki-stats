import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  name: string;
  isBot: boolean;
}

export const UserInfoComponent: React.FC<IProps> = ({ name, isBot }) => {
  return (
    <div className={styles.root}>
      <strong>{name}</strong>
      <div>
        Bot: {`${isBot}`}
      </div>
    </div>
  );
};
