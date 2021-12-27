import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IChangeModel } from "@api/changes/change.model";
import { SUBSCRIBED_USER_CHANGE_CREATED_EVENT_TYPE, ISubscribedUserChangeCreatedEvent } from "@api/users/subscribed-user-change-created.event";
import { IUserChangesStatsPartModel } from "@api/users/user-changes-stats.model";
import { UsersApiService } from "@api/users/users-api.service";
import { USER_CHANGES_STATS_CHANGED_EVENT_TYPE, IUserChangesStatsChangedEvent } from "@api/users/user-changes-stats-changed.event";
import {
  IUserChangesStatsChangedItemEvent,
  IUserWikiChangesStatsChangedEvent,
  USER_WIKI_CHANGES_STATS_CHANGED_EVENT_TYPE,
} from "@api/users/user-wiki-changes-stats-changed.event";

export class SubscribedUserStatsState {
  recentChanges: IChangeModel[] = [];
  keepChanges = 10;
  subscribedUserName: string = "";
  subscribedUserChangesStatsParts: IUserChangesStatsPartModel[] = [];
  changesStatsWindow: number = 60;
  changesStatsStep: number = 1;
  subscribedUserTopWikis: IUserChangesStatsChangedItemEvent[] = [];

  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.setKeepChanges = this.setKeepChanges.bind(this);
    this.subscribeForUserChanges = this.subscribeForUserChanges.bind(this);
    this.setChangeStatsWindow = this.setChangeStatsWindow.bind(this);

    this.processChangeCreatedEvent = this.processChangeCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_CHANGE_CREATED_EVENT_TYPE, this.processChangeCreatedEvent);

    this.processUserChangesStatsCreatedEvent = this.processUserChangesStatsCreatedEvent.bind(this);
    wsApiHelper.subscribe(USER_CHANGES_STATS_CHANGED_EVENT_TYPE, this.processUserChangesStatsCreatedEvent);

    this.processUserWikiChangesStatsChangedEvent = this.processUserWikiChangesStatsChangedEvent.bind(this);
    wsApiHelper.subscribe(USER_WIKI_CHANGES_STATS_CHANGED_EVENT_TYPE, this.processUserWikiChangesStatsChangedEvent);
  }

  private async processChangeCreatedEvent(event: ISubscribedUserChangeCreatedEvent) {
    this.recentChanges.push(event);
    this.recentChanges = this.recentChanges.slice(-this.keepChanges);
  };

  private async processUserChangesStatsCreatedEvent(event: IUserChangesStatsChangedEvent) {
    if (this.subscribedUserChangesStatsParts.length === 0) {
      return;
    }

    const lastChangeStatsPart = this.subscribedUserChangesStatsParts[this.subscribedUserChangesStatsParts.length - 1];
    const eventStartTimestamp = new Date(event.startTimestamp);

    if (lastChangeStatsPart.endTimestamp >= eventStartTimestamp) {
      // update last part
      this.subscribedUserChangesStatsParts[this.subscribedUserChangesStatsParts.length - 1] = {
        index: lastChangeStatsPart.index,
        changes: event.changesCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: lastChangeStatsPart.endTimestamp,
      };
      this.subscribedUserChangesStatsParts = this.subscribedUserChangesStatsParts.slice(0); //TODO: replace by deepCopy?
    } else {
      // add new part
      this.subscribedUserChangesStatsParts.push({
        index: lastChangeStatsPart.index + 1,
        changes: event.changesCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: new Date(
          eventStartTimestamp.setMinutes(eventStartTimestamp.getMinutes() + event.durationInMinutes),
        ),
      });
      this.subscribedUserChangesStatsParts = this.subscribedUserChangesStatsParts.slice(-this.changesStatsWindow);
    }
  };

  private async processUserWikiChangesStatsChangedEvent(event: IUserWikiChangesStatsChangedEvent) {
    this.subscribedUserTopWikis = event.wikis;
  }

  public setKeepChanges(keepChanges: number) {
    this.keepChanges = keepChanges;
  }

  public async setChangeStatsWindow(window: number, step: number) {
    this.changesStatsWindow = window;
    this.changesStatsStep = step;
    this.subscribedUserChangesStatsParts = (await UsersApiService.getUserChangesStats({
      userName: this.subscribedUserName,
      window: this.changesStatsWindow,
      step: this.changesStatsStep,
    })).parts;
  }

  public async subscribeForUserChanges(userName: string) {
    this.subscribedUserName = userName;
    await UsersApiService.subscribeForUserChanges({ userName: this.subscribedUserName });
    this.subscribedUserChangesStatsParts = (await UsersApiService.getUserChangesStats({
      userName: this.subscribedUserName,
      window: this.changesStatsWindow,
      step: this.changesStatsStep,
    })).parts;
  }
}

export const subscribedUserStatsState = new SubscribedUserStatsState();
