import axios from "axios";
import React, { useState } from "react";
import { useEffect } from "react/cjs/react.development";
import { API } from "../../common/API";
import "../style/UserDeviceRule.css";

function UserDeviceRule() {
  const [devices, setDevices] = useState([]);
  const [chosenIpAddress, setChosenIpAddress] = useState("");
  const [days, setDays] = useState(1);
  const [numberOfLogs, setNumberOfLogs] = useState(0);

  useEffect(() => {
    axios({
      method: 'GET',
      url: API + 'devices'
    })
    .then(response => {
      setDevices(response.data);
    })
    .catch(err => {
      console.log(err);
    })
  }, []);

  const createRule = () => {
    const data = {ipAddress: chosenIpAddress, numberOfLogs, days};
    console.log(data);

    axios({
      method: 'POST',
      url: API + 'rules/devices',
      data: data
    })
    .then(response => {
      console.log('Rule inserted');
    })
    .catch(err => {
      console.log(err);
    });
  };

  return (
    <div className="user-device-wrapper">
      <p className="rule-explanation">
        Create a rule for blacklisting devices. If a device exibits malicious behaviour of some sort, specify it's IP address and a limit as to how many logs can it generate, so if the device exceedes a limit, the system will raise an alarm.
      </p>
      <div className="rule-form">
        <div className="rule-col-1">
          <div className="field">
            <p>Device to blacklist</p>
            {
              devices &&
              <select
              className="select"
              value={chosenIpAddress}
              onChange={(e) => setChosenIpAddress(e.target.value)}
              >
                {
                  devices.map(device => { return(<option key={device.ipAddress} value={device.ipAddress}> {device.ipAddress} </option>) })
                }
              </select>
            }
          </div>
        </div>
        <div className="rule-col-2">
          <div className="field">
            <p>After a number of logs</p>
            <input
              className="input"
              type="number"
              min={0}
              max={100}
              value={numberOfLogs}
              onChange={(e) => setNumberOfLogs(e.target.value)}
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
        Create a rule
      </button>
    </div>
  );
}

export default UserDeviceRule;
