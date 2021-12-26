export const SUBSCRIBED_USER_EDIT_CREATED_EVENT_TYPE = "SubscribedUserEditCreated";

export interface ISubscribedUserEditCreatedEvent {
  id: string;
  timestamp: Date;
  title: string;
  comment: string;
  editor: {
    id: string;
    name: string;
  };
  wiki: {
    id: string;
    name: string;
  }
}
