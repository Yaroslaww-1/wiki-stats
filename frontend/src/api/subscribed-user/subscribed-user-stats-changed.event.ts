export const SUBSCRIBED_USER_STATS_CHANGED_EVENT_TYPE = "SubscribedUserStatsChanged";

export interface ISubscribedUserStatsChangedEvent {
  startTimestamp: string;
  durationInMinutes: number;
  changesCount: number;
  userId: string;
}