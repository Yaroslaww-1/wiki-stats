export const TOP_USERS_CHANGED_EVENT_TYPE = "TopUsersChanged";

export interface ITopUsersChangedEventTopUser {
  changesCount: number;
  userName: string;
}

export interface ITopUsersChangedEvent {
  users: ITopUsersChangedEventTopUser[];
}