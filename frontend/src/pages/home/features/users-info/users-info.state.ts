import { makeAutoObservable } from "mobx";

import { IUserModel } from "@api/users/user.model";
import wsApiHelper from "@api/ws-api.helper";
import { IUserCreatedEvent, USER_CREATED_EVENT_TYPE } from "@api/users/user-created.event";

export class UsersInfoState {
  totalUsersCount: number = 0;
  lastCreatedUser: IUserModel | null = null;

  constructor() {
    makeAutoObservable(this);

    this.processUserCreatedEvent = this.processUserCreatedEvent.bind(this);
    wsApiHelper.subscribe(USER_CREATED_EVENT_TYPE, this.processUserCreatedEvent);
  }

  processUserCreatedEvent = async (event: IUserCreatedEvent) => {
    this.totalUsersCount += 1;
    this.lastCreatedUser = event as IUserModel;
  };
}

export const usersInfoState = new UsersInfoState();
