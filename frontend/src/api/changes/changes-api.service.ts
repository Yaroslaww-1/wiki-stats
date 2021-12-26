import httpApi from "../http-api.helper";

const endpoint = "/changes";

export class ChangesApiService {
  static async setDelay({ delay }: { delay: number }): Promise<void> {
    return httpApi.put(`${endpoint}/delay`, { delay });
  }
}
