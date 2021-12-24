import React from "react";

import { IUserModel } from "@api/users/user.model";
import { UserInfoComponent } from "pages/home/components/user-info";

import styles from "./styles.module.scss";

interface IProps {
  totalUsersCount: number;
  lastCreatedUser: IUserModel | null;
}

export const UsersInfoComponent: React.FC<IProps> = ({ totalUsersCount, lastCreatedUser }) => {
  return (
    <div className={styles.root}>
      <div className={styles.total}>
        <span>Total users count: </span>
        <strong>{totalUsersCount}</strong>
      </div>
      <div className={styles.lastCreatedUser}>
        Last created user:
      </div>
      {lastCreatedUser && <UserInfoComponent name={lastCreatedUser.name} isBot={lastCreatedUser.isBot} />}
    </div>
  );
};
