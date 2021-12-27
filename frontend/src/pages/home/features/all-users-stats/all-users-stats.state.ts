import { makeAutoObservable } from "mobx";

import { UsersApiService } from "@api/users/users-api.service";
import { IUserModel } from "@api/users/user.model";
import wsApiHelper from "@api/ws-api.helper";
import { IUserCreatedEvent, USER_CREATED_EVENT_TYPE } from "@api/users/user-created.event";

export class AllUsersStatsState {
  totalUsersCount: number = 0;
  lastCreatedUser: IUserModel | null = null;

  constructor() {
    makeAutoObservable(this);

    this.processUserCreatedEvent = this.processUserCreatedEvent.bind(this);
    wsApiHelper.subscribe(USER_CREATED_EVENT_TYPE, this.processUserCreatedEvent);

    this.getUsersStats();
  }

  private processUserCreatedEvent = async (event: IUserCreatedEvent) => {
    this.totalUsersCount += 1;
    this.lastCreatedUser = event as IUserModel;
  };

  public async getUsersStats() {
    const usersStats = await UsersApiService.getUsersStats();
    this.totalUsersCount += usersStats.count;
  }
}

export const allUsersStatsState = new AllUsersStatsState();
