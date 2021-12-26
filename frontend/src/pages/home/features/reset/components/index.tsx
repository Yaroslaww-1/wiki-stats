import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  reset: () => void;
}

export const ResetComponent: React.FC<IProps> = ({ reset }) => {
  const onClick = () => {
    reset();
  };

  return (
    <button className={styles.root} onClick={onClick}>Reset</button>
  );
};
