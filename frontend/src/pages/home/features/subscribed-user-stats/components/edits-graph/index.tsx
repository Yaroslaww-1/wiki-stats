import React, { useEffect, useState } from "react";
import { LineChart, Line, XAxis, CartesianGrid, Tooltip } from "recharts";

import { IUserEditsStatsPartModel } from "@api/users/user-edits-stats.model";

import styles from "./styles.module.scss";

interface IProps {
  userEditStats?: IUserEditsStatsPartModel[];
  initialWindow: number;
  initialStep: number;
  setOptions: (window: number, step: number) => void;
}

export const EditsGraphComponent: React.FC<IProps> = ({
  userEditStats = [],
  initialWindow,
  initialStep,
  setOptions,
}) => {
  const [window, setWindow] = useState(initialWindow);
  const [step, setStep] = useState(initialStep);

  const onWindowValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setWindow(parseInt(event.target.value) || 60);
  };

  const onStepValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStep(parseInt(event.target.value) || 1);
  };

  const onClick = () => {
    setOptions(window, step);
  };

  return (
    <div className={styles.root}>
      <div className={styles.controls}>
        <div className={styles.control}>
          Window in minutes:
          <input value={window} onChange={onWindowValueChange}></input>
        </div>
        <div className={styles.control}>
          Step in minutes:
          <input value={step} onChange={onStepValueChange}></input>
        </div>
        <button onClick={onClick}>Apply</button>
      </div>
      <LineChart
        width={400}
        height={400}
        data={userEditStats}
        margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
        key={userEditStats[userEditStats.length - 1]?.edits}
      >
        <XAxis dataKey="index" />
        <Tooltip />
        <CartesianGrid stroke="#f5f5f5" />
        <Line type="monotone" dataKey="edits" stroke="#ff7300" yAxisId={0} isAnimationActive={false} />
      </LineChart>
    </div>
  );
};
