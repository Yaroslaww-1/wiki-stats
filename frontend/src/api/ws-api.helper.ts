/* eslint-disable no-unused-vars */

const WS_BASE_URL = process.env.REACT_APP_WS_API_URL || "";

class WsApi {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private readonly subscriptions: Map<string, (event: any) => Promise<void>>;
  private readonly socket: WebSocket;

  constructor() {
    this.subscriptions = new Map();
    this.socket = new WebSocket(WS_BASE_URL);

    this.socket.onopen = () => {
      this.socket.send("ping");
    };

    this.handleSocketEvent = this.handleSocketEvent.bind(this);
    this.socket.addEventListener("message", this.handleSocketEvent);
  }

  subscribe<E>(eventType: string, handler: (event: E) => Promise<void>) {
    this.subscriptions.set(eventType, handler);
  }

  private handleSocketEvent(messageEvent: MessageEvent<string>) {
    const data = JSON.parse(messageEvent.data);
    const { type, payload } = data;
    
    if (this.subscriptions.has(type)) {
      const handler = this.subscriptions.get(type)!;
      handler(payload);
    }
  }
}

export default new WsApi();
