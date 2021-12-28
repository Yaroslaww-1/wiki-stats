export const TOP_WIKIS_CHANGED_EVENT_TYPE = "TopWikisChanged";

export interface ITopWikisChangedEventTopWiki {
  editsCount: number;
  wikiName: string;
}

export interface ITopWikisChangedEvent {
  wikis: ITopWikisChangedEventTopWiki[];
}