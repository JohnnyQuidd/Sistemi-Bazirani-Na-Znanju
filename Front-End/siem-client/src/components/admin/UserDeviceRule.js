import axios from "axios";
import React, { useState } from "react";
import { API } from "../../common/API";
import "../style/UserDeviceRule.css";

function UserDeviceRule() {
  const [previousRiskCategory, setPreviousRiskCategory] = useState("LOW");
  const [newRiskCategory, setNewRiskCategory] = useState("MODERATE");
  const [days, setDays] = useState(0);
  const [numberOfDevices, setNumberOfDevices] = useState(0);

  const createRule = () => {
    const data = {
      numberOfDevices,
      previousRiskCategory,
      newRiskCategory,
      days,
    };
    if (previousRiskCategory === newRiskCategory) {
      return alert("Previous category cannot be same as a new category");
    }
    if (days <= 0 || numberOfDevices <= 0) {
      return alert("Values have to be positive");
    }

    axios({
      method: "POST",
      url: API + "rules/users/devices",
      data: data,
    })
      .then((response) => {
        console.log("Rule created");
      })
      .catch((err) => {
        console.log(err);
      });
  };

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
            <select
              className="select"
              value={previousRiskCategory}
              onChange={(e) => setPreviousRiskCategory(e.target.value)}
            >
              <option value="LOW">LOW</option>
              <option value="MODERATE">MODERATE</option>
              <option value="HIGH">HIGH</option>
              <option value="EXTREME">EXTREME</option>
            </select>
          </div>
          <div className="field">
            <p>Change into</p>
            <select
              className="select"
              value={newRiskCategory}
              onChange={(e) => setNewRiskCategory(e.target.value)}
            >
              <option value="LOW">LOW</option>
              <option value="MODERATE">MODERATE</option>
              <option value="HIGH">HIGH</option>
              <option value="EXTREME">EXTREME</option>
            </select>
          </div>
        </div>
        <div className="rule-col-2">
          <div className="field">
            <p>After a number of devices</p>
            <input
              className="input"
              type="number"
              min={1}
              max={100}
              value={numberOfDevices}
              onChange={(e) => setNumberOfDevices(e.target.value)}
            />
          </div>
          <div className="field">
            <p>Over days</p>
            <input
              className="input"
              type="number"
              min={1}
              max={100}
              value={days}
              onChange={(e) => setDays(e.target.value)}
            />
          </div>
        </div>
      </div>
      <button className="create-a-rule" onClick={createRule}>
        {" "}
        Create a rule{" "}
      </button>
    </div>
  );
}

export default UserDeviceRule;
