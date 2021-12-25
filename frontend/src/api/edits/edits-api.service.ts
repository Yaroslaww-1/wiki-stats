import httpApi from "../http-api.helper";

const endpoint = "/edits";

export class EditsApiService {
  static async setDelay({ delay }: { delay: number }): Promise<void> {
    return httpApi.put(`${endpoint}/delay`, { delay });
  }

  static async subscribeForUserEdits({ userName }: { userName: string }): Promise<void> {
    return httpApi.post(`${endpoint}/subscribe`, { userName });
  }
}
