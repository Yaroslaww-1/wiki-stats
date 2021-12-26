import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, ISubscribedUserEditCreatedEvent } from "@api/edits/subscribed-user-edit-created.event";
import { EditsApiService } from "@api/edits/edits-api.service";
import { IUserEditsStatsModel } from "@api/users/user-edits-stats.model";
import { UsersApiService } from "@api/users/users-api.service";

export class SubscribedUserEditsInfoState {
  lastCreatedEdits: IEditModel[] = [];
  keepEdits = 10;
  subscribedUserName: string = "";
  subscribedUserEditsStats: IUserEditsStatsModel | null = null;
  editsStatsWindow: number = 10;

  constructor() {
    makeAutoObservable(this);

    this.setKeepEdits = this.setKeepEdits.bind(this);
    this.setSubscribedUserName = this.setSubscribedUserName.bind(this);
    this.subscribeForUserEdits = this.subscribeForUserEdits.bind(this);

    this.processEditCreatedEvent = this.processEditCreatedEvent.bind(this);
    wsApiHelper.subscribe(SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE, this.processEditCreatedEvent);
  }

  private async processEditCreatedEvent(event: ISubscribedUserEditCreatedEvent) {
    this.lastCreatedEdits.push(event);
    this.lastCreatedEdits = this.lastCreatedEdits.slice(-this.keepEdits);
  };

  public setKeepEdits(keepEdits: number) {
    this.keepEdits = keepEdits;
  }

  public setSubscribedUserName(userName: string) {
    this.subscribedUserName = userName;
  }

  public async setEditStatsWindow(window: number) {
    this.editsStatsWindow = window;
    this.subscribedUserEditsStats = await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
    });
  }

  public async subscribeForUserEdits() {
    await EditsApiService.subscribeForUserEdits({ userName: this.subscribedUserName });
    this.subscribedUserEditsStats = await UsersApiService.getUserEditsStats({
      userName: this.subscribedUserName,
      window: this.editsStatsWindow,
    });
  }
}

export const subscribedUserEditsInfoState = new SubscribedUserEditsInfoState();
