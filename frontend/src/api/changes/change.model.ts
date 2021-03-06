export interface IChangeModel {
  id: string;
  timestamp: Date;
  title: string;
  comment: string;
  editor: {
    id: string;
    name: string;
  };
  wiki: {
    id: string;
    name: string;
  }
}
