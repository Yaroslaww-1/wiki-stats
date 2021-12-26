import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { EDIT_CREATED_EVENT_TYPE, IEditCreatedEvent } from "@api/edits/edit-created.event";
import { EditsApiService } from "@api/edits/edits-api.service";

export class AllRecentEditsState {
  recentEdits: IEditModel[] = [];
  keepEdits = 10;
  processingDelay = 0;

  constructor() {
    makeAutoObservable(this);

    this.setKeepEdits = this.setKeepEdits.bind(this);
    this.setProcessingDelay = this.setProcessingDelay.bind(this);

    this.processEditCreatedEvent = this.processEditCreatedEvent.bind(this);
    wsApiHelper.subscribe(EDIT_CREATED_EVENT_TYPE, this.processEditCreatedEvent);
  }

  private processEditCreatedEvent = async (event: IEditCreatedEvent) => {
    this.recentEdits.push(event);
    this.recentEdits = this.recentEdits.slice(-this.keepEdits);
  };

  public setKeepEdits(keepEdits: number) {
    this.keepEdits = keepEdits;
  }

  public setProcessingDelay(processingDelay: number) {
    this.processingDelay = processingDelay;
    EditsApiService.setDelay({ delay: processingDelay });
  }
}

export const allRecentEditsState = new AllRecentEditsState();
