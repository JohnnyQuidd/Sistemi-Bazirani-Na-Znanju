import React, { useState, useEffect } from "react";
import "../style/Navbar.css";

function Navbar({ page }) {
  const [adminClass, setAdminClass] = useState("inactive");
  const [logsClass, setLogsClass] = useState("inactive");
  const [alarmsClass, setAlarmsClass] = useState("inactive");

  useEffect(() => {
    setPage(page);
  }, [page]);

  const setPage = (page) => {
    switch (page) {
      case "admin":
        setAdminClass("active");
        setLogsClass("inactive");
        setAlarmsClass("inactive");
        break;
      case "logs":
        setAdminClass("inactive");
        setLogsClass("active");
        setAlarmsClass("inactive");
        break;
      case "alarms":
        setAdminClass("inactive");
        setLogsClass("inactive");
        setAlarmsClass("active");
        break;
    }
  };

  return (
    <ul>
      <li>
        <a className={adminClass} href="/admin">
          Home
        </a>
      </li>
      <li>
        <a className={logsClass} href="/logs">
          Logs
        </a>
      </li>
      <li>
        <a className={alarmsClass} href="/alarms">
          Alarms
        </a>
      </li>
    </ul>
  );
}

export default Navbar;
