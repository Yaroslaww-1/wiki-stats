import React from "react";
import { LineChart, Line, XAxis, CartesianGrid, Tooltip } from "recharts";

import { IUserEditsStatsPartModel } from "@api/users/user-edits-stats.model";

import styles from "./styles.module.scss";

interface IProps {
  userEditStats?: IUserEditsStatsPartModel[];
  window: number;
  setWindow: (window: number) => void;
}

export const EditsGraphComponent: React.FC<IProps> = ({
  userEditStats = [],
  window,
  setWindow,
}) => {
  const onWindowValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setWindow(parseInt(event.target.value) || 10);
  };

  return (
    <div className={styles.root}>
      <input value={window} onChange={onWindowValueChange}></input>
      <LineChart
        width={400}
        height={400}
        data={userEditStats}
        margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
      >
        <XAxis dataKey="index" />
        <Tooltip />
        <CartesianGrid stroke="#f5f5f5" />
        <Line type="monotone" dataKey="edits" stroke="#ff7300" yAxisId={0} />
      </LineChart>
    </div>
  );
};
