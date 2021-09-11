import React, { useState } from "react";
import DatePicker from "react-multi-date-picker";
import "../style/LogSearch.css";

function LogSearch({ query }) {
  const [message, setMessage] = useState("");
  const [regex, setRegex] = useState(false);
  const [logType, setLogType] = useState("Log Type");
  const [factStatus, setFactStatus] = useState("Fact Status");
  const [date, setDate] = useState(null);

  const sendQuery = () => {
    const dateArray = JSON.stringify(date);
    query({message, regex, logType, factStatus, dateArray});
  };

  return (
    <div className="log-search-wrapper">
      <div className="regex-wrapper">
        <input id="search-message" placeholder="Message content/regex" value={message} onChange={e => setMessage(e.target.value)} />
        <label id="regex-label"> REGEX</label>
        <input id="regex-checkbox" type="checkbox" value={regex} onChange={e => setRegex(!regex)} />
      </div>
      <div className="criteria-wrapper">
        <select className="log-type" value={logType} onChange={e => setLogType(e.target.value)}>
          <option value="">Log Type</option>
          <option value="WARNING">WARNING</option>
          <option value="ERROR">ERROR</option>
          <option value="INFORMATION">INFORMATION</option>
        </select>
        <select className="fact-status" value={factStatus} onChange={e => setFactStatus(e.target.value)}>
          <option value="">Fact Status</option>
          <option value="PENDING">PENDING</option>
          <option value="ACTIVE">ACTIVE</option>
          <option value="ARCHIVED">ARCHIVED</option>
        </select>
        <DatePicker
          value={date}
          onChange={setDate}
          format="DD/MM/YYYY"
          isClearable
          range
        />
      </div>
      <button id="log-search" onClick={sendQuery}> Search </button>
    </div>
  );
}

export default LogSearch;
