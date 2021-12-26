import React from "react";

import { AdminApiService } from "@api/admin/admin-api.service";

import { ResetComponent } from "../components";

export const ResetContainer: React.FC = () => {
  const reset = () => {
    AdminApiService.reset();
    window.location.reload();
  };

  return (
    <ResetComponent
      reset={reset}
    />
  );
};
