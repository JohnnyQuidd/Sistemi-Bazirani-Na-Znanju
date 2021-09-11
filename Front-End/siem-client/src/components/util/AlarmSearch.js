import React, { useState } from "react";
import DatePicker from "react-multi-date-picker";
import "../style/AlarmSearch.css";
import "react-multi-date-picker/styles/colors/purple.css";
import "react-multi-date-picker/styles/layouts/prime.css";

function AlarmSearch({ query }) {
  const [message, setMessage] = useState("");
  const [regex, setRegex] = useState(false);
  const [factStatus, setFactStatus] = useState("Fact Status");
  const [date, setDate] = useState(null);

  const sendQuery = () => {
    const dateArray = JSON.stringify(date);
    query({message, regex, factStatus, dateArray});
  };

  return (
    <div className="alarm-search-wrapper">
      <div className="regex-wrapper">
        <input id="search-message" placeholder="Message content/regex" value={message} onChange={e => setMessage(e.target.value)} />
        <label id="regex-label"> REGEX</label>
        <input id="regex-checkbox" type="checkbox" value={regex} onChange={e => setRegex(!regex)} />
      </div>
      <div className="criteria-wrapper">
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
      <button id="alarm-search" onClick={sendQuery}> Search </button>
    </div>
  );
}

export default AlarmSearch;
