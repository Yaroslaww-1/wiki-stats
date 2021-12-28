import React from "react";

import styles from "./styles.module.scss";

interface IProps {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  data: Record<string, any>[];
  keys: string[];
}

export const TableComponent: React.FC<IProps> = ({
  data = [],
  keys = [],
}) => {
  return (
    <table className={styles.root}>
      {data.map(item => {
        const values = keys.filter(key => item[key])
          .map(key => item[key]);

        return (
          <tr key={JSON.stringify(item)}>
            {values.map(value => (
              <th key={JSON.stringify(value)}>{value}</th>
            ))}
          </tr>
        );
      })}
    </table>
  );
};
