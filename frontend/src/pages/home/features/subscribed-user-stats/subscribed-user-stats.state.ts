import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, ISubscribedUserEditCreatedEvent } from "@api/users/subscribed-user-edit-created.event";
import { IUserEditsStatsPartModel } from "@api/users/user-edits-stats.model";
import { UsersApiService } from "@api/users/users-api.service";
import { USER_EDITS_STATS_CHANGED_EVENT_TYPE, IUserEditsStatsChangedEvent } from "@api/users/user-edits-stats-changed.event";

export class SubscribedUserStatsState {
  recentEdits: IEditModel[] = [];
  keepEdits = 10;
  subscribedUserName: string = "";
  subscribedUserEditsStatsParts: IUserEditsStatsPartModel[] = [];
  editsStatsWindow: number = 60;
  editsStatsStep: number = 1;

  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.setKeepEdits = this.setKeepEdits.bind(this);
    this.subscribeForUserEdits = this.subscribeForUserEdits.bind(this);
    this.setEditStatsWindow = this.setEditStatsWindow.bind(this);

    this.processEditCreatedEvent = this.processEditCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, this.processEditCreatedEvent);

    this.processUserEditsStatsCreatedEvent = this.processUserEditsStatsCreatedEvent.bind(this);
    wsApiHelper.subscribe(USER_EDITS_STATS_CHANGED_EVENT_TYPE, this.processUserEditsStatsCreatedEvent);
  }

  private async processEditCreatedEvent(event: ISubscribedUserEditCreatedEvent) {
    this.recentEdits.push(event);
    this.recentEdits = this.recentEdits.slice(-this.keepEdits);
  };

  private async processUserEditsStatsCreatedEvent(event: IUserEditsStatsChangedEvent) {
    if (this.subscribedUserEditsStatsParts.length === 0) {
      return;
    }

    const lastEditStatsPart = this.subscribedUserEditsStatsParts[this.subscribedUserEditsStatsParts.length - 1];
    const eventStartTimestamp = new Date(event.startTimestamp);

    if (lastEditStatsPart.endTimestamp >= eventStartTimestamp) {
      // update last part
      this.subscribedUserEditsStatsParts[this.subscribedUserEditsStatsParts.length - 1] = {
        index: lastEditStatsPart.index,
        edits: event.editCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: lastEditStatsPart.endTimestamp,
      };
      this.subscribedUserEditsStatsParts = this.subscribedUserEditsStatsParts.slice(0); //TODO: replace by deepCopy?
    } else {
      // add new part
      this.subscribedUserEditsStatsParts.push({
        index: lastEditStatsPart.index + 1,
        edits: event.editCount,
        durationInMinutes: event.durationInMinutes,
        endTimestamp: new Date(
          eventStartTimestamp.setMinutes(eventStartTimestamp.getMinutes() + event.durationInMinutes),
        ),
      });
      this.subscribedUserEditsStatsParts = this.subscribedUserEditsStatsParts.slice(-this.editsStatsWindow);
    }
  };

  public setKeepEdits(keepEdits: number) {
    this.keepEdits = keepEdits;
  }

  public async setEditStatsWindow(window: number, step: number) {
    this.editsStatsWindow = window;
    this.editsStatsStep = step;
    this.subscribedUserEditsStatsParts = (await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
      step: this.editsStatsStep,
    })).parts;
  }

  public async subscribeForUserEdits(userName: string) {
    this.subscribedUserName = userName;
    await UsersApiService.subscribeForUserEdits({ userName: this.subscribedUserName });
    this.subscribedUserEditsStatsParts = (await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
      step: this.editsStatsStep,
    })).parts;
  }
}

export const subscribedUserStatsState = new SubscribedUserStatsState();
