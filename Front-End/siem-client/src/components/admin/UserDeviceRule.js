import React from "react";
import "../style/UserDeviceRule.css";

function UserDeviceRule() {
  return (
    <div className="user-device-wrapper">
      <p className="rule-explanation">
        Create a rule for categorizing Users based on how many devices have a
        specific User used to log in over a certain period of time. Those rules
        can give an insight into whether or not many devices tried to log into
        the same account and change the risk category of a User accordingly
      </p>
      <div className="rule-form">
        <div className="rule-col-1">
          <div className="field">
            <p>Current Risk Category</p>
            <select className="select">
              <option>LOW</option>
              <option>MODERATE</option>
              <option>HIGH</option>
              <option>EXTREME</option>
            </select>
          </div>
          <div className="field">
            <p>Change into</p>
            <select className="select">
              <option>LOW</option>
              <option>MODERATE</option>
              <option>HIGH</option>
              <option>EXTREME</option>
            </select>
          </div>
        </div>
        <div className="rule-col-2">
          <div className="field">
            <p>After a number of devices</p>
            <input className="input" type="number" min={1} max={100} />
          </div>
          <div className="field">
            <p>Over days</p>
            <input className="input" type="number" min={1} max={100} />
          </div>
        </div>
      </div>
      <button className="create-a-rule"> Create a rule </button>
    </div>
  );
}

export default UserDeviceRule;
