export const USER_CHANGE_STATS_CHANGED_EVENT_TYPE = "UserChangeStatsChanged";

export interface IUserChangeStatsChangedEvent {
  startTimestamp: string;
  durationInMinutes: number;
  changesCount: number;
  userId: string;
}