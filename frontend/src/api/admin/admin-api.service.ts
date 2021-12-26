import httpApi from "../http-api.helper";

const endpoint = "/admin";

export class AdminApiService {
  static async reset(): Promise<void> {
    return httpApi.post(`${endpoint}/reset`, {});
  }
}
