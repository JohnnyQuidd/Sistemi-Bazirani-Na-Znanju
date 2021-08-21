import axios from "axios";
import React, { useState, useEffect } from "react";
import { API } from "../../common/API";
import "../style/LogsReport.css";
import LogsTable from "../tables/LogsTable";
import DatePicker from "react-multi-date-picker";

function LogsReport() {
  const [logs, setLogs] = useState([]);
  const [logsPerMachine, setLogsPerMachine] = useState(false);
  const [logsPerSystem, setLogsPerSystem] = useState(false);
  const [alarms, setAlarms] = useState([]);
  const [devices, setDevices] = useState([]);
  const [chosenDevice, setChosenDevice] = useState(null);
  const [systems, setSystems] = useState([]);
  const [chosenSystem, setChosenSystem] = useState(null);
  const [datePickerValue, setDatePickerValue] = useState(null);

  useEffect(() => {
    fetchDevices();
    fetchSystems();
    fetchLogs();
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

  const fetchLogs = () => {
    axios({
      method: "GET",
      url: API + "logs",
    }).then((response) => {
      let temp = response.data;
      temp = prettifyDateTime(temp);
      setLogs(temp);
    });
  };

  const expandModal = (data) => {
    console.log(data);
  };

  const prettifyDateTime = (logs) => {
    for (let i = 0; i < logs.length; i++) {
      let timestamp = Date.parse(logs[i]["timestamp"]);
      let date = new Date(timestamp);
      logs[i]["timestamp"] =
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

    return logs;
  };

  const handleChosenMachine = (event) => {
    setChosenDevice(event.target.value);
  };

  const handleChosenSystem = (event) => {
    setChosenSystem(event.target.value);
  };

  const filterLogs = () => {
    let date = JSON.stringify(datePickerValue);
    const data = {
      logsPerMachine,
      logsPerSystem,
      chosenDevice,
      chosenSystem,
      date,
    };

    axios({
      url: API + 'logs/filter',
      method: 'POST',
      data: data
    }).then(response => {
      setLogs(response.data);
    }).catch(err => {
      console.log(err);
    });
  };

  return (
    <div className="reports-wrapper">
      <h1 className="activity-report"> Activity report </h1>
      <div className="logs-filter">
        <div className="logs-radio-1">
          <label className="logs-label">Logs per machine</label>
          <input
            type="radio"
            name="logs"
            value={logsPerMachine}
            onChange={() => {
              setLogsPerMachine(true);
              setLogsPerSystem(false);
            }}
          />
        </div>
        <div className="logs-radio-2">
          <label className="logs-label">Logs per system</label>
          <input
            type="radio"
            name="logs"
            value={logsPerSystem}
            onChange={() => {
              setLogsPerMachine(false);
              setLogsPerSystem(true);
            }}
          />
        </div>
        {devices && (
          <select
            className="machine-select"
            disabled={!logsPerMachine}
            onChange={handleChosenMachine}
            value={chosenDevice}
          >
            {devices.map((device) => {
              return (
                <option key={device.id} value={device.ipAddress}> {device.ipAddress} </option>
              );
            })}
          </select>
        )}
        {systems && (
          <select
            className="system-select"
            disabled={!logsPerSystem}
            onChange={handleChosenSystem}
            value={chosenSystem}
          >
            {systems.map((system) => {
              return <option key={system.id} value={system.name}> {system.name} </option>;
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
        <button className="search-logs" onClick={filterLogs}>
          Search
        </button>
      </div>
      {logs && <LogsTable logsData={logs} expandModal={expandModal} />}
    </div>
  );
}

export default LogsReport;
