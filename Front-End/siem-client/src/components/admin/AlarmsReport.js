import axios from "axios";
import React, { useState, useEffect } from "react";
import { API } from "../../common/API";
import "../style/AlarmsReport.css";
import DatePicker from "react-multi-date-picker";
import AlarmsTable from "../tables/AlarmsTable";

function AlarmsReport() {
  const [alarms, setAlarms] = useState([]);
  const [devices, setDevices] = useState([]);
  const [chosenDevice, setChosenDevice] = useState(null);
  const [chosenSystem, setChosenSystem] = useState(null);
  const [systems, setSystems] = useState([]);
  const [alarmsPerMachine, setAlarmsPerMachine] = useState(false);
  const [alarmsPerSystem, setAlarmsPerSystem] = useState(false);
  const [datePickerValue, setDatePickerValue] = useState(null);

  useEffect(() => {
    fetchDevices();
    fetchSystems();
    fetchAlarms();
  }, []);

  const fetchDevices = () => {
    axios({
      method: "GET",
      url: API + "devices",
    })
      .then((response) => {
        setChosenDevice(response.data[0].ipAddress);
        setDevices(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchSystems = () => {
    axios({
      method: "GET",
      url: API + "systems",
    })
      .then((response) => {
        setChosenSystem(response.data[0].name);
        setSystems(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchAlarms = () => {
    axios({
      method: "GET",
      url: API + "alarms",
    })
      .then((response) => {
        let temp = response.data;
        temp = prettifyDateTime(temp);
        setAlarms(temp);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  const filterAlarms = () => {
    let date = JSON.stringify(datePickerValue);
    const data = {
      alarmsPerMachine,
      alarmsPerSystem,
      chosenDevice,
      chosenSystem,
      date,
    };
    
    console.log(data);
  };

  const expandModal = (data) => {
    console.log(data);
  };

  const prettifyDateTime = (alarms) => {
    for (let i = 0; i < alarms.length; i++) {
      let timestamp = Date.parse(alarms[i]["timestamp"]);
      let date = new Date(timestamp);
      alarms[i]["timestamp"] =
        date.getDate() +
        "/" +
        (date.getMonth() + 1) +
        "/" +
        date.getFullYear() +
        " (" +
        date.getHours() +
        ":" +
        date.getMinutes() +
        ":" +
        date.getSeconds() +
        ")";
    }

    return alarms;
  };

  const handleChosenMachine = (event) => {
    setChosenDevice(event.target.value);
  };

  const handleChosenSystem = (event) => {
    setChosenSystem(event.target.value);
  };

  return (
    <div className="reports-wrapper">
      <h1 className="activity-report"> Activity report </h1>
      <div className="alarms-filter">
        <div className="alarms-radio-1">
          <label className="alarms-label">Alarms per machine</label>
          <input
            type="radio"
            name="alarms"
            value={alarmsPerMachine}
            onChange={() => {
              setAlarmsPerMachine(true);
              setAlarmsPerSystem(false);
            }}
          />
        </div>
        <div className="alarms-radio-2">
          <label className="alarms-label">Alarms per system</label>
          <input
            type="radio"
            name="alarms"
            value={alarmsPerSystem}
            onChange={() => {
              setAlarmsPerMachine(false);
              setAlarmsPerSystem(true);
            }}
          />
        </div>
        {devices && (
          <select
            className="machine-select"
            disabled={!alarmsPerMachine}
            onChange={handleChosenMachine}
            value={chosenDevice}
          >
            {devices.map((device) => {
              return (
                <option key={device.id} value={device.ipAddress}>
                  {" "}
                  {device.ipAddress}{" "}
                </option>
              );
            })}
          </select>
        )}
        {systems && (
          <select
            className="system-select"
            disabled={!alarmsPerSystem}
            onChange={handleChosenSystem}
            value={chosenSystem}
          >
            {systems.map((system) => {
              return (
                <option key={system.id} value={system.name}>
                  {" "}
                  {system.name}{" "}
                </option>
              );
            })}
          </select>
        )}
        <DatePicker
          value={datePickerValue}
          onChange={setDatePickerValue}
          format="DD/MM/YYYY"
          isClearable
          range
        />
        <button className="search-alarms" onClick={filterAlarms}>
          Search
        </button>
      </div>
      {alarms && <AlarmsTable alarmsData={alarms} expandModal={expandModal} />}
    </div>
  );
}

export default AlarmsReport;
