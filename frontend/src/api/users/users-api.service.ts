import httpApi from "../http-api.helper";
import { IUserChangesStatsModel } from "./user-changes-stats.model";

import { IUsersStatsModel } from "./users-stats.model";

const endpoint = "/users";

export class UsersApiService {
  static async getUsersStats(): Promise<IUsersStatsModel> {
    return httpApi.get(`${endpoint}/stats`);
  }

  static async subscribeForUserChanges({ userName }: { userName: string }): Promise<void> {
    return httpApi.post(`${endpoint}/${userName}/subscribe`, {});
  }

  static async getUserChangesStats(
    { userName, window, step }: { userName: string; window: number, step: number },
  ): Promise<IUserChangesStatsModel> {
    const stats = await httpApi.get<IUserChangesStatsModel>(`${endpoint}/${userName}/stats`, { window, step });

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
