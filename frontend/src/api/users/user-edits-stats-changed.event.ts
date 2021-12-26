export const USER_EDITS_STATS_CHANGED_EVENT_TYPE = "EditStatsChanged";

export interface IUserEditsStatsChangedEvent {
  startTimestamp: string;
  durationInMinutes: number;
  addCount: number;
  editCount: number;
  userId: string;
}