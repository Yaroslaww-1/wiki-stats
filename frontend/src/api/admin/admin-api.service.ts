import httpApi from "../http-api.helper";

const endpoint = "/admin";

export class AdminApiService {
  static async reset(): Promise<void> {
    return httpApi.post(`${endpoint}/reset`, {});
  }

  static async setDelay({ delay }: { delay: number }): Promise<void> {
    return httpApi.put(`${endpoint}/delay`, { delay });
  }

  static async subscribeForUserChanges({ userName }: { userName: string }): Promise<void> {
    return httpApi.post(`${endpoint}/${userName}/subscribe`, {});
  }
}
