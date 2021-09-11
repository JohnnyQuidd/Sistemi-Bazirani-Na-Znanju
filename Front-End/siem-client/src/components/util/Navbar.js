import React, { useState, useEffect } from "react";
import "../style/Navbar.css";

function Navbar({ page }) {
  const [adminClass, setAdminClass] = useState("inactive");
  const [logsClass, setLogsClass] = useState("inactive");
  const [alarmsClass, setAlarmsClass] = useState("inactive");
  const [reportsClass, setReportsClass] = useState("inactive");
  const [alarmsReportClass, setAlarmsReportClass] = useState("inactive");
  const [rulesClass, setRulesClass] = useState("inactive");

  useEffect(() => {
    setPage(page);
  }, [page]);

  const setPage = (page) => {
    switch (page) {
      case "admin":
        setAdminClass("active");
        setLogsClass("inactive");
        setAlarmsClass("inactive");
        setReportsClass("inactive");
        setAlarmsReportClass("inactive");
        setRulesClass("inactive");
        break;
      case "logs":
        setAdminClass("inactive");
        setLogsClass("active");
        setAlarmsClass("inactive");
        setReportsClass("inactive");
        setAlarmsReportClass("inactive");
        setRulesClass("inactive");
        break;
      case "alarms":
        setAdminClass("inactive");
        setLogsClass("inactive");
        setAlarmsClass("active");
        setReportsClass("inactive");
        setAlarmsReportClass("inactive");
        setRulesClass("inactive");
        break;
      case "logs-report":
        setAdminClass("inactive");
        setLogsClass("inactive");
        setAlarmsClass("inactive");
        setReportsClass("active");
        setAlarmsReportClass("inactive");
        setRulesClass("inactive");
        break;
      case "alarms-report":
        setAdminClass("inactive");
        setLogsClass("inactive");
        setAlarmsClass("inactive");
        setReportsClass("inactive");
        setAlarmsReportClass("active");
        setRulesClass("inactive");
        break;
      case "rules":
        setAdminClass("inactive");
        setLogsClass("inactive");
        setAlarmsClass("inactive");
        setReportsClass("inactive");
        setAlarmsReportClass("inactive");
        setRulesClass("active");
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
      <li>
        <a className={reportsClass} href="/logs-report">
          Logs report
        </a>
      </li>
      <li>
        <a className={alarmsReportClass} href="/alarms-report">
          Alarms report
        </a>
      </li>
      <li>
        <a className={rulesClass} href="/rules">
          Rules
        </a>
      </li>
    </ul>
  );
}

export default Navbar;
