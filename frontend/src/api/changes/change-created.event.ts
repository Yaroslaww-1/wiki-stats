export const CHANGE_CREATED_EVENT_TYPE = "ChangeCreated";

export interface IChangeCreatedEvent {
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
