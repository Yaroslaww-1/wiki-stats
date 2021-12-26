import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, ISubscribedUserEditCreatedEvent } from "@api/users/subscribed-user-edit-created.event";
import { IUserEditsStatsModel } from "@api/users/user-edits-stats.model";
import { UsersApiService } from "@api/users/users-api.service";

export class SubscribedUserStatsState {
  recentEdits: IEditModel[] = [];
  keepEdits = 10;
  subscribedUserName: string = "";
  subscribedUserEditsStats: IUserEditsStatsModel | null = null;
  editsStatsWindow: number = 60;
  editsStatsStep: number = 1;

  constructor() {
    makeAutoObservable(this);

    this.setKeepEdits = this.setKeepEdits.bind(this);
    this.subscribeForUserEdits = this.subscribeForUserEdits.bind(this);
    this.setEditStatsWindow = this.setEditStatsWindow.bind(this);

    this.processEditCreatedEvent = this.processEditCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, this.processEditCreatedEvent);
  }

  private async processEditCreatedEvent(event: ISubscribedUserEditCreatedEvent) {
    this.recentEdits.push(event);
    this.recentEdits = this.recentEdits.slice(-this.keepEdits);
  };

  public setKeepEdits(keepEdits: number) {
    this.keepEdits = keepEdits;
  }

  public async setEditStatsWindow(window: number, step: number) {
    this.editsStatsWindow = window;
    this.editsStatsStep = step;
    this.subscribedUserEditsStats = await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
      step: this.editsStatsStep,
    });
  }

  public async subscribeForUserEdits(userName: string) {
    this.subscribedUserName = userName;
    await UsersApiService.subscribeForUserEdits({ userName: this.subscribedUserName });
    this.subscribedUserEditsStats = await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
      step: this.editsStatsStep,
    });
  }
}

export const subscribedUserStatsState = new SubscribedUserStatsState();
