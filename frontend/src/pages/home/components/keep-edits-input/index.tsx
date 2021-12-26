import React, { useState } from "react";

interface IProps {
  initialKeepEdits: number;
  setKeepEdits: (keepEdits: number) => void;
}

export const KeepEditsInputComponent: React.FC<IProps> = ({
  initialKeepEdits,
  setKeepEdits: submitKeepEdits,
}) => {
  const [keepEdits, setKeepEdits] = useState(initialKeepEdits);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeepEdits(parseInt(event.target.value) || 0);
  };

  const onClick = () => {
    submitKeepEdits(keepEdits);
  };

  return (
    <>
      <span>Keep <strong>edits:</strong></span>
      <input value={keepEdits} onChange={onChange}></input>
      <button onClick={onClick}>Apply</button>
    </>
  );
};
