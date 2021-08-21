import axios from 'axios';
import React, { useState, useEffect } from 'react'
import { API } from '../../common/API';
import '../style/Reports.css';

function Reports() {
  const [logs, setLogs] = useState([]);
  const [alarms, setAlarms] = useState([]);
  const [devices, setDevices] = useState([]);
  const [systems, setSystems] = useState([]);

  useEffect(() => {
    fetchDevices();
    fetchSystems();
  }, []);

  const fetchDevices = () => {
    axios({
      method: 'GET',
      url: API + 'devices'
    }).then(response => {
      console.log(response.data);
    }).catch(err => {
      console.log(err);
    });
  }

  const fetchSystems = () => {
    axios({
      method: 'GET',
      url: API + 'systems'
    }).then(response => {
      console.log(response.data);
    }).catch(err => {
      console.log(err);
    });
  }

  return (
    <div className="reports-wrapper">
        <h1 className="activity-report" > Activity report </h1>
    </div>
  )
}

export default Reports
