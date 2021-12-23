export const USER_CREATED_EVENT_TYPE = "UserCreated";

export interface IUserCreatedEvent {
  id: string;
  name: string;
  isBot: boolean;
}
