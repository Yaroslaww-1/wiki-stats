import React from "react";

import { IUserModel } from "@api/users/user.model";
import { UserInfoComponent } from "pages/home/components/user-info";

interface IProps {
  totalUsersCount: number;
  lastCreatedUser: IUserModel | null;
}

export const UsersInfoComponent: React.FC<IProps> = ({ totalUsersCount, lastCreatedUser }) => {
  return (
    <div>
      <div>
        Total users count: {totalUsersCount}
      </div>
      <div>
        Last created user:
      </div>
      {lastCreatedUser && <UserInfoComponent name={lastCreatedUser.name} isBot={lastCreatedUser.isBot} />}
    </div>
  );
};
