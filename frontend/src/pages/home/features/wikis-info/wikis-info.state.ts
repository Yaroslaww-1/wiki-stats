import { makeAutoObservable } from "mobx";

import { WikisApiService } from "@api/wikis/wikis-api.service";
import { IWikiModel } from "@api/wikis/wiki.model";
import wsApiHelper from "@api/ws-api.helper";
import { IWikiCreatedEvent, WIKI_CREATED_EVENT_TYPE } from "@api/wikis/wiki-created.event";

export class WikisInfoState {
  totalWikisCount: number = 0;
  lastCreatedWiki: IWikiModel | null = null;

  constructor() {
    makeAutoObservable(this);

    this.processWikiCreatedEvent = this.processWikiCreatedEvent.bind(this);
    wsApiHelper.subscribe(WIKI_CREATED_EVENT_TYPE, this.processWikiCreatedEvent);

    this.getWikisStats();
  }

  private processWikiCreatedEvent = async (event: IWikiCreatedEvent) => {
    this.totalWikisCount += 1;
    this.lastCreatedWiki = event as IWikiModel;
  };

  public async getWikisStats() {
    const WikisStats = await WikisApiService.getWikisStats();
    this.totalWikisCount += WikisStats.count;
  }
}

export const wikisInfoState = new WikisInfoState();
