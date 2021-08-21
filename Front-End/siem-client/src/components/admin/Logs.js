import axios from "axios";
import React, { useState, useEffect } from "react";
import "../style/Logs.css";
import LogsTable from "../tables/LogsTable";
import { API } from "../../common/API";
import LogModal from "../modals/LogModal";
import LogSearch from "../util/LogSearch";

function Logs() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [logs, setLogs] = useState([]);
  const [chosenLog, setChosenLog] = useState(null);

  useEffect(() => {
    axios({
      method: "GET",
      url: API + "logs",
    }).then((response) => {
      let temp = response.data;
      temp = prettifyDateTime(temp);
      setLogs(temp);
    });
  }, []);

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

  const expandModal = (log) => {
    setChosenLog(log);
    setIsModalOpen(true);
  };

  const changeModalState = (isOpen) => {
    setIsModalOpen(isOpen);
  };

  const filterLogs = (data) => {
    if (data.logType == "Log Type") data.logType = "";
    if (data.factStatus == "Fact Status") data.factStatus = "";

    data.date = data.dateArray;
    axios({
      method: "POST",
      url: API + "logs/search",
      data: data
    })
      .then((response) => {
        let temp = response.data;
        temp = prettifyDateTime(temp);
        setLogs(temp);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="logs-wrapper">
      <h1 className="logs-title"> Recorded logs </h1>
      <LogSearch query={filterLogs} />
      {logs && <LogsTable logsData={logs} expandModal={expandModal} />}
      <LogModal
        log={chosenLog}
        isModalOpen={isModalOpen}
        setIsModalOpen={changeModalState}
      />
    </div>
  );
}

export default Logs;
