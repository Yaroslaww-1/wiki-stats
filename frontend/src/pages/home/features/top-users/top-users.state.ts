import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import {
  ITopUsersChangedEventTopUser,
  TOP_USERS_CHANGED_EVENT_TYPE,
  ITopUsersChangedEvent,
} from "@api/top-users/top-users-changed.event";
import { TopUsersInterval } from "@api/top-users/top-users-interval.enum";
import { AdminApiService } from "@api/admin/admin-api.service";

export class TopUsersChangesStatsState {
  topUsers: ITopUsersChangedEventTopUser[] = [];
  topUsersInterval: TopUsersInterval = TopUsersInterval.DAY;

  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.setTopUsersInterval = this.setTopUsersInterval.bind(this);

    this.processTopUsersChangedEvent = this.processTopUsersChangedEvent.bind(this);
    wsApiHelper.subscribe(TOP_USERS_CHANGED_EVENT_TYPE, this.processTopUsersChangedEvent);
  }

  private async processTopUsersChangedEvent(event: ITopUsersChangedEvent) {
    this.topUsers = event.users;
  };

  public setTopUsersInterval(interval: TopUsersInterval) {
    this.topUsersInterval = interval;
    AdminApiService.setTopUsersInterval({ interval });
  }
}

export const topUsersChangesStatsState = new TopUsersChangesStatsState();
