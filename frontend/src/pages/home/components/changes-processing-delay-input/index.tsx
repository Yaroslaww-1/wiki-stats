import React, { useState } from "react";

interface IProps {
  initialDelay: number;
  setDelay: (delay: number) => void;
}

export const ChangesProcessingInputComponent: React.FC<IProps> = ({
  initialDelay,
  setDelay: submitDelay,
}) => {
  const [delay, setDelay] = useState(initialDelay);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDelay(parseInt(event.target.value) || 0);
  };

  const onClick = () => {
    submitDelay(delay);
  };

  return (
    <>
      <span>Processing delay:</span>
      <input value={delay} onChange={onChange}></input>
      <button onClick={onClick}>Apply</button>
    </>
  );
};
