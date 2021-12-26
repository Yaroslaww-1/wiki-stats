export interface IUserEditsStatsPartModel {
  index: number;
  edits: number;
  durationInMinutes: number;
  endTimestamp: Date;
}

export interface IUserEditsStatsModel {
  parts: IUserEditsStatsPartModel[];
}
