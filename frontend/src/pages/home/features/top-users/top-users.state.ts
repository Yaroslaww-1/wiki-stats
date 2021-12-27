import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import {
  ITopUsersChangedEventTopUser,
  TOP_USERS_CHANGED_EVENT_TYPE,
  ITopUsersChangedEvent,
} from "@api/top-users/top-users-changed.event";

export class TopUsersChangesStatsState {
  topUsers: ITopUsersChangedEventTopUser[] = [];
 
  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.processTopUsersChangedEvent = this.processTopUsersChangedEvent.bind(this);
    wsApiHelper.subscribe(TOP_USERS_CHANGED_EVENT_TYPE, this.processTopUsersChangedEvent);
  }

  private async processTopUsersChangedEvent(event: ITopUsersChangedEvent) {
    this.topUsers = event.users;
  };
}

export const topUsersChangesStatsState = new TopUsersChangesStatsState();
