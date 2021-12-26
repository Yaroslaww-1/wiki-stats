import React, { useState } from "react";

interface IProps {
  initialUserName: string;
  setUserName: (userName: string) => void;
}

export const SubscribeUserInputComponent: React.FC<IProps> = ({
  initialUserName,
  setUserName: submitUserName,
}) => {
  const [userName, setUserName] = useState(initialUserName);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUserName(event.target.value || "");
  };

  const onClick = () => {
    submitUserName(userName);
  };

  return (
    <>
      <span>Subscribed user:</span>
      <input value={userName} onChange={onChange}></input>
      <button onClick={onClick}>Subscribe</button>
    </>
  );
};
