import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IChangeModel } from "@api/changes/change.model";
import { CHANGE_CREATED_EVENT_TYPE, IChangeCreatedEvent } from "@api/changes/change-created.event";
import { AdminApiService } from "@api/admin/admin-api.service";

export class AllRecentChangesState {
  recentChanges: IChangeModel[] = [];
  keepChanges = 10;
  processingDelay = 0;

  constructor() {
    makeAutoObservable(this);

    this.setKeepChanges = this.setKeepChanges.bind(this);
    this.setProcessingDelay = this.setProcessingDelay.bind(this);

    this.processChangeCreatedEvent = this.processChangeCreatedEvent.bind(this);
    wsApiHelper.subscribe(CHANGE_CREATED_EVENT_TYPE, this.processChangeCreatedEvent);
  }

  private processChangeCreatedEvent = async (event: IChangeCreatedEvent) => {
    this.recentChanges.push(event);
    this.recentChanges = this.recentChanges.slice(-this.keepChanges);
  };

  public setKeepChanges(keepChanges: number) {
    this.keepChanges = keepChanges;
  }

  public setProcessingDelay(processingDelay: number) {
    this.processingDelay = processingDelay;
    AdminApiService.setDelay({ delay: processingDelay });
  }
}

export const allRecentChangesState = new AllRecentChangesState();
