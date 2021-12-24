import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  title: string;
  comment: string;
  editorName: string;
  wikiName: string;
}

export const EditInfoComponent: React.FC<IProps> = ({ title, comment, editorName, wikiName }) => {
  return (
    <div className={styles.root}>
      <div>Title: {title}</div>
      <div>Comment: {comment}</div>
      <div>Editor: {editorName}</div>
      <div>Wiki: {wikiName}</div>
    </div>
  );
};
