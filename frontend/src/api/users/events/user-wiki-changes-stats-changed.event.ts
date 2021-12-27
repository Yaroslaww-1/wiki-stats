export const USER_WIKI_CHANGES_STATS_CHANGED_EVENT_TYPE = "UserWikiChangeStatsChanged";

export interface IUserChangesStatsChangedItemEvent {
  changesCount: number;
  wikiName: string;
}

export interface IUserWikiChangesStatsChangedEvent {
  wikis: IUserChangesStatsChangedItemEvent[];
}