export const SUBSCRIBED_USER_TOP_WIKI_CHANGED_EVENT_TYPE = "SubscribedUserTopWikiChanged";

export interface ISubscribedUserTopWikiChangedEventWiki {
  changesCount: number;
  wikiName: string;
}

export interface ISubscribedUserTopWikiChangedEvent {
  wikis: ISubscribedUserTopWikiChangedEventWiki[];
}