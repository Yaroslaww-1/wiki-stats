import httpApi from "../http-api.helper";

import { IWikisStatsModel } from "./wikis-stats.model";

const endpoint = "/wikis";

export class WikisApiService {
  static async getWikisStats(): Promise<IWikisStatsModel> {
    return httpApi.get(`${endpoint}/stats`);
  }
}
