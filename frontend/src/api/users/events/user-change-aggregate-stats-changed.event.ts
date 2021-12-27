export const USER_CHANGE_AGGREGATE_STATS_CHANGED_EVENT_TYPE = "UserChangeAggregateStatsChanged";

export interface IUserChangeAggregateStatsChangedEvent {
  userId: string;
  addCount: number;
  editCount: number;
}