import axios from "axios";
import React, { useState, useEffect } from "react";
import { API } from "../../common/API";
import "../style/UsersReport.css";
import UsersReportTable from "../tables/UsersReportTable";

function UsersReport() {
  const [alarmsUsers, setAlarmsUsers] = useState([]);
  const [devicesUsers, setDevicesUsers] = useState([]);
  const [antivirusUsers, setAntivirusUsers] = useState([]);
  const [deviceNum, setDeviceNum] = useState(1);

  useEffect(() => {
    fetchAlarmsUsers();
    fetchDevicesUsers();
    fetchAntivirusUsers();
  }, []);

  const fetchAlarmsUsers = () => {
    axios({
      method: "GET",
      url: API + "users/alarms",
    })
      .then((response) => {
        setAlarmsUsers(prettifyDateTime(response.data));
      })
      .catch((err) => {
        console.log("Unable to fetch users for alarms");
        console.log(err);
      });
  };

  const fetchDevicesUsers = () => {
    axios({
      method: "GET",
      url: API + "users/failedLogin?deviceNum=" + deviceNum,
    })
      .then((response) => {
        setDevicesUsers(prettifyDateTime(response.data));
      })
      .catch((err) => {
        console.log("Unable to fetch users for alarms");
        console.log(err);
      });
  };

  const fetchAntivirusUsers = () => {
    axios({
      method: "GET",
      url: API + "users/alarms/past10days",
    })
      .then((response) => {
        setAntivirusUsers(prettifyDateTime(response.data));
      })
      .catch((err) => {
        console.log("Unable to fetch users for antivirus");
        console.log(err);
      });
  };

  const prettifyDateTime = (users) => {
    for (let i = 0; i < users.length; i++) {
      let timestamp = Date.parse(users[i]["lastTimeUserWasActive"]);
      let date = new Date(timestamp);
      users[i]["lastTimeUserWasActive"] =
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

    return users;
  };

  const generateReport = () => {
    fetchDevicesUsers();
  }

  return (
    <div className="users-report-wrapper">
      <div className="first-users-criteria">
        <h2> Users that triggered at least 6 alarms in past 6 months </h2>
        {alarmsUsers && <UsersReportTable usersData={alarmsUsers} />}
      </div>

      <div className="second-users-criteria">
        <h2>
          Users with failed login attempts from a certain number of devices{" "}
        </h2>
        <input
          id="devNum"
          type="number"
          min={1}
          max={100}
          placeholder="Number of devices"
          value={deviceNum}
          onChange={(e) => setDeviceNum(e.target.value)}
        />
        <button id="devSubmit" onClick={generateReport}> Generate report </button>
        {devicesUsers && <UsersReportTable usersData={devicesUsers} />}
      </div>

      <div className="third-users-criteria">
        <h2>
          {" "}
          Users that triggered at least 10 antivirus alarms past 10 days{" "}
        </h2>
        {antivirusUsers && <UsersReportTable usersData={antivirusUsers} />}
      </div>
    </div>
  );
}

export default UsersReport;
