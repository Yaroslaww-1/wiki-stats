export const SUBSCRIBED_USER_CHANGE_CREATED_EVENT_TYPE = "SubscribedUserChangeCreated";

export interface ISubscribedUserChangeCreatedEvent {
  id: string;
  timestamp: Date;
  title: string;
  comment: string;
  userName: string;
  wikiName: string;
}
