export const USER_CHANGES_STATS_CHANGED_EVENT_TYPE = "UserChangeStatsChanged";

export interface IUserChangesStatsChangedEvent {
  startTimestamp: string;
  durationInMinutes: number;
  changesCount: number;
  userId: string;
}