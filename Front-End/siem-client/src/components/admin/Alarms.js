import axios from "axios";
import React, { useEffect, useState } from "react";
import { API } from "../../common/API";
import AlarmModal from "../modals/AlarmModal";
import "../style/Alarms.css";
import AlarmsTable from "../tables/AlarmsTable";
import AlarmSearch from "../util/AlarmSearch";

function Alarms() {
  const [alarms, setAlarms] = useState([]);
  const [chosenAlarm, setChosenAlarm] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    axios({
      method: "GET",
      url: API + "alarms",
    })
      .then((response) => {
        const data = prettifyDateTime(response.data);
        setAlarms(data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

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

  const expandModal = (alarm) => {
    setChosenAlarm(alarm);
    setIsModalOpen(true);
  };

  const sendQuery = (data) => {
    if (data.factStatus == "Fact Status") data.factStatus = "";
    data.date = data.dateArray;

    axios({
      method: "POST",
      url: API + "alarms/search",
      data: data,
    })
      .then((response) => {
        const data = prettifyDateTime(response.data);
        setAlarms(data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="alarms-wrapper">
      <h1 className="alarms-title"> Alarms </h1>
      <AlarmSearch query={sendQuery} />
      {alarms && <AlarmsTable alarmsData={alarms} expandModal={expandModal} />}
      <AlarmModal
        alarm={chosenAlarm}
        isModalOpen={isModalOpen}
        setIsModalOpen={setIsModalOpen}
      />
    </div>
  );
}

export default Alarms;
