export interface IUserEditsStatsPartModel {
  index: number;
  edits: number;
  durationInDays: number;
  endYear: number;
  endDay: number;
}

export interface IUserEditsStatsModel {
  parts: IUserEditsStatsPartModel[];
}
