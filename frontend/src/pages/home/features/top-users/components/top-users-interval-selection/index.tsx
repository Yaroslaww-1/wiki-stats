import React, { ChangeEvent } from "react";

import { TopUsersInterval } from "@api/top-users/top-users-interval.enum";

interface IProps {
  setInterval: (interval: TopUsersInterval) => void;
}

export const TopUsersIntervalSelectionComponent: React.FC<IProps> = ({ setInterval }) => {
  const onChange = (event: ChangeEvent<HTMLSelectElement>) => {
    if (event.target.value == "day") {
      setInterval(TopUsersInterval.DAY);
    }

    if (event.target.value == "month") {
      setInterval(TopUsersInterval.MONTH);
    }

    if (event.target.value == "year") {
      setInterval(TopUsersInterval.YEAR);
    }
  };

  return (
    <select onChange={onChange}>
      <option value="day">Day</option>
      <option value="month">Month</option>
      <option value="year">Year</option>
    </select>
  );
};
