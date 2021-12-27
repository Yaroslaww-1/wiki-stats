export const SUBSCRIBED_USER_AGGREGATE_STATS_CHANGED_EVENT_TYPE = "SubscribedUserAggregateStatsChanged";

export interface ISubscribedUserAggregateStatsChangedEvent {
  userId: string;
  addCount: number;
  editCount: number;
}