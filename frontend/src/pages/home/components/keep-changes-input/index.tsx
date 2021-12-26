import React, { useState } from "react";

interface IProps {
  initialKeepChanges: number;
  setKeepChanges: (keepChanges: number) => void;
}

export const KeepChangesInputComponent: React.FC<IProps> = ({
  initialKeepChanges,
  setKeepChanges: submitKeepChanges,
}) => {
  const [keepChanges, setKeepChanges] = useState(initialKeepChanges);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeepChanges(parseInt(event.target.value) || 0);
  };

  const onClick = () => {
    submitKeepChanges(keepChanges);
  };

  return (
    <>
      <span>Keep <strong>changes:</strong></span>
      <input value={keepChanges} onChange={onChange}></input>
      <button onClick={onClick}>Apply</button>
    </>
  );
};
