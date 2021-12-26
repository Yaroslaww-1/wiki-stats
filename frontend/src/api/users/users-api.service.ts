import httpApi from "../http-api.helper";
import { IUserEditsStatsModel } from "./user-edits-stats.model";

import { IUsersStatsModel } from "./users-stats.model";

const endpoint = "/users";

export class UsersApiService {
  static async getUsersStats(): Promise<IUsersStatsModel> {
    return httpApi.get(`${endpoint}/stats`);
  }

  static async subscribeForUserEdits({ userName }: { userName: string }): Promise<void> {
    return httpApi.post(`${endpoint}/${userName}/subscribe`, {});
  }

  static async getUserEditsStats(
    { userName, window, step }: { userName: string; window: number, step: number },
  ): Promise<IUserEditsStatsModel> {
    const stats = await httpApi.get<IUserEditsStatsModel>(`${endpoint}/${userName}/stats`, { window, step });

    stats.parts = stats.parts.map(part => {
      const endTimestamp = new Date(part.endTimestamp);

      return {
        ...part,
        endTimestamp,
      };
    });

    return stats;
  }
}
