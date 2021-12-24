import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { EDIT_CREATED_EVENT_TYPE, IEditCreatedEvent } from "@api/edits/edit-created.event";
import { EditsApiService } from "@api/edits/edits-api.service";

export class EditsInfoState {
  lastCreatedEdits: IEditModel[] = [];
  keepEdits = 10;
  processingDelay = 0;

  constructor() {
    makeAutoObservable(this);

    this.setKeepEdits = this.setKeepEdits.bind(this);
    this.setProcessingDelay = this.setProcessingDelay.bind(this);

    this.processWikiCreatedEvent = this.processWikiCreatedEvent.bind(this);
    wsApiHelper.subscribe(EDIT_CREATED_EVENT_TYPE, this.processWikiCreatedEvent);
  }

  private processWikiCreatedEvent = async (event: IEditCreatedEvent) => {
    this.lastCreatedEdits.push(event);
    this.lastCreatedEdits = this.lastCreatedEdits.slice(-this.keepEdits);
  };

  public setKeepEdits(keepEdits: number) {
    this.keepEdits = keepEdits;
  }

  public setProcessingDelay(processingDelay: number) {
    this.processingDelay = processingDelay;
    EditsApiService.setDelay({ delay: processingDelay });
  }
}

export const editsInfoState = new EditsInfoState();
