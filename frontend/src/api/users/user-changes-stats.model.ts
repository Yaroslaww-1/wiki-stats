export interface IUserChangesStatsPartModel {
  index: number;
  changes: number;
  durationInMinutes: number;
  endTimestamp: Date;
}

export interface IUserChangesStatsModel {
  parts: IUserChangesStatsPartModel[];
}
