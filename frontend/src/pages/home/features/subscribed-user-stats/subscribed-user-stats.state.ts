import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IUserChangesStatsPartModel } from "@api/users/user-changes-stats.model";
import { UsersApiService } from "@api/users/users-api.service";
import { AdminApiService } from "@api/admin/admin-api.service";
import {
  ISubscribedUserTopWikiChangedEvent,
  ISubscribedUserTopWikiChangedEventWiki,
  SUBSCRIBED_USER_TOP_WIKI_CHANGED_EVENT_TYPE,
} from "@api/subscribed-user/subscribed-user-top-wiki-changed.event";
import {
  ISubscribedUserAggregateStatsChangedEvent,
  SUBSCRIBED_USER_AGGREGATE_STATS_CHANGED_EVENT_TYPE,
} from "@api/subscribed-user/subscribed-user-aggregate-stats-changed.event";
import {
  ISubscribedUserChangeCreatedEvent,
  SUBSCRIBED_USER_CHANGE_CREATED_EVENT_TYPE,
} from "@api/subscribed-user/subscribed-user-change-created.event";
import {
  ISubscribedUserStatsChangedEvent,
  SUBSCRIBED_USER_STATS_CHANGED_EVENT_TYPE,
} from "@api/subscribed-user/subscribed-user-stats-changed.event";

export class SubscribedUserStatsState {
  recentChanges: ISubscribedUserChangeCreatedEvent[] = [];
  keepChanges = 10;
  subscribedUserName: string = "";
  subscribedUserStatsParts: IUserChangesStatsPartModel[] = [];
  changesStatsWindow: number = 60;
  changesStatsStep: number = 1;
  subscribedUserTopWikis: ISubscribedUserTopWikiChangedEventWiki[] = [];
  subscribedUserAggregateStats: ISubscribedUserAggregateStatsChangedEvent | null = null;

  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.setKeepChanges = this.setKeepChanges.bind(this);
    this.subscribeForUserChanges = this.subscribeForUserChanges.bind(this);
    this.setChangeStatsWindow = this.setChangeStatsWindow.bind(this);

    this.processSubscribedUserChangeCreatedEvent = this.processSubscribedUserChangeCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_CHANGE_CREATED_EVENT_TYPE, this.processSubscribedUserChangeCreatedEvent);

    this.processSubscribedUserStatsCreatedEvent = this.processSubscribedUserStatsCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_STATS_CHANGED_EVENT_TYPE, this.processSubscribedUserStatsCreatedEvent);

    this.processSubscribedUserAggregateStatsCreatedEvent =
      this.processSubscribedUserAggregateStatsCreatedEvent.bind(this);
    wsApiHelper.subscribe(
      SUBSCRIBED_USER_AGGREGATE_STATS_CHANGED_EVENT_TYPE,
      this.processSubscribedUserAggregateStatsCreatedEvent,
    );

    this.processSubscribedUserTopWikiChangedEvent = this.processSubscribedUserTopWikiChangedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_TOP_WIKI_CHANGED_EVENT_TYPE, this.processSubscribedUserTopWikiChangedEvent);
  }

  private async processSubscribedUserChangeCreatedEvent(event: ISubscribedUserChangeCreatedEvent) {
    this.recentChanges.push(event);
    this.recentChanges = this.recentChanges.slice(-this.keepChanges);
  };

  private async processSubscribedUserStatsCreatedEvent(event: ISubscribedUserStatsChangedEvent) {
    if (this.subscribedUserStatsParts.length === 0) {
      return;
    }

    const lastChangeStatsPart = this.subscribedUserStatsParts[this.subscribedUserStatsParts.length - 1];
    const eventStartTimestamp = new Date(event.startTimestamp);

    if (lastChangeStatsPart.endTimestamp >= eventStartTimestamp) {
      // update last part
      this.subscribedUserStatsParts[this.subscribedUserStatsParts.length - 1] = {
        index: lastChangeStatsPart.index,
        changes: event.changesCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: lastChangeStatsPart.endTimestamp,
      };
      this.subscribedUserStatsParts = this.subscribedUserStatsParts.slice(0); //TODO: replace by deepCopy?
    } else {
      // add new part
      this.subscribedUserStatsParts.push({
        index: lastChangeStatsPart.index + 1,
        changes: event.changesCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: new Date(
          eventStartTimestamp.setMinutes(eventStartTimestamp.getMinutes() + event.durationInMinutes),
        ),
      });
      this.subscribedUserStatsParts = this.subscribedUserStatsParts.slice(-this.changesStatsWindow);
    }
  };

  private async processSubscribedUserAggregateStatsCreatedEvent(event: ISubscribedUserAggregateStatsChangedEvent) {
    this.subscribedUserAggregateStats = event;
  }

  private async processSubscribedUserTopWikiChangedEvent(event: ISubscribedUserTopWikiChangedEvent) {
    console.log(event.wikis.length);
    this.subscribedUserTopWikis = event.wikis;
  }

  public setKeepChanges(keepChanges: number) {
    this.keepChanges = keepChanges;
  }

  public async setChangeStatsWindow(window: number, step: number) {
    this.changesStatsWindow = window;
    this.changesStatsStep = step;
    this.subscribedUserStatsParts = (await UsersApiService.getUserChangesStats({
      userName: this.subscribedUserName,
      window: this.changesStatsWindow,
      step: this.changesStatsStep,
    })).parts;
  }

  public async subscribeForUserChanges(userName: string) {
    this.subscribedUserName = userName;
    await AdminApiService.subscribeForUserChanges({ userName: this.subscribedUserName });
    this.subscribedUserStatsParts = (await UsersApiService.getUserChangesStats({
      userName: this.subscribedUserName,
      window: this.changesStatsWindow,
      step: this.changesStatsStep,
    })).parts;
  }
}

export const subscribedUserStatsState = new SubscribedUserStatsState();
