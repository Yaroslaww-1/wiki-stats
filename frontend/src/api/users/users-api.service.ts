import httpApi from "../http-api.helper";

import { IUsersStatsModel } from "./users-stats.model";

const endpoint = "/users";

export class UsersApiService {
  static async getUsersStats(): Promise<IUsersStatsModel> {
    return httpApi.get(`${endpoint}/stats`);
  }
}
