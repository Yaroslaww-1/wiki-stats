import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  title: string;
  comment: string;
  editorName: string;
  wikiName: string;
}

export const ChangeComponent: React.FC<IProps> = ({ title, comment, editorName, wikiName }) => {
  return (
    <div className={styles.root}>
      <div><strong>{editorName}</strong> - <strong>{wikiName}</strong></div>
      <div>{title}</div>
      <div>{comment}</div>
    </div>
  );
};
