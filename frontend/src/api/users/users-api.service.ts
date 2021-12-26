import httpApi from "../http-api.helper";
import { IUserEditsStatsModel } from "./user-edits-stats.model";

import { IUsersStatsModel } from "./users-stats.model";

const endpoint = "/users";

export class UsersApiService {
  static async getUsersStats(): Promise<IUsersStatsModel> {
    return httpApi.get(`${endpoint}/stats`);
  }

  static async getUserEditsStats(
    { userName, window }: { userName: string; window: number },
  ): Promise<IUserEditsStatsModel> {
    return httpApi.get(`${endpoint}/${userName}/stats`, { window });
  }
}
