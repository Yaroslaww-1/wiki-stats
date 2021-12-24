import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import { IEditModel } from "@api/edits/edit.model";
import { EDIT_CREATED_EVENT_TYPE, IEditCreatedEvent } from "@api/edits/edit-created.event";

export class EditsInfoState {
  lastCreatedEdits: IEditModel[] = [];
  keepEdits = 10;

  constructor() {
    makeAutoObservable(this);

    this.setKeepEdits = this.setKeepEdits.bind(this);

    this.processWikiCreatedEvent = this.processWikiCreatedEvent.bind(this);
    wsApiHelper.subscribe(EDIT_CREATED_EVENT_TYPE, this.processWikiCreatedEvent);
  }

  private processWikiCreatedEvent = async (event: IEditCreatedEvent) => {
    this.lastCreatedEdits.push(event);
    this.lastCreatedEdits = this.lastCreatedEdits.slice(-this.keepEdits);
  };

  public setKeepEdits(newKeepEdits: number) {
    this.keepEdits = newKeepEdits;
  }
}

export const editsInfoState = new EditsInfoState();
