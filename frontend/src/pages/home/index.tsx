import React from "react";

import { PageComponent } from "@components/page";

import { UsersInfoContainer } from "./features/users-info/containers/users-info";

export const HomePage: React.FC = () => {
  return (
    <PageComponent>
      <UsersInfoContainer />
    </PageComponent>
  );
};
