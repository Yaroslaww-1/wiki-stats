import { makeAutoObservable } from "mobx";

import wsApiHelper from "@api/ws-api.helper";
import {
  ITopWikisChangedEvent,
  ITopWikisChangedEventTopWiki,
  TOP_WIKIS_CHANGED_EVENT_TYPE,
} from "@api/top-wikis/top-wikis-changed.event";

export class TopWikisChangesStatsState {
  topWikis: ITopWikisChangedEventTopWiki[] = [];

  constructor() {
    makeAutoObservable(this, {}, { deep: true });

    this.processTopWikisChangedEvent = this.processTopWikisChangedEvent.bind(this);
    wsApiHelper.subscribe(TOP_WIKIS_CHANGED_EVENT_TYPE, this.processTopWikisChangedEvent);
  }

  private async processTopWikisChangedEvent(event: ITopWikisChangedEvent) {
    this.topWikis = event.wikis;
  };
}

export const topWikisChangesStatsState = new TopWikisChangesStatsState();
