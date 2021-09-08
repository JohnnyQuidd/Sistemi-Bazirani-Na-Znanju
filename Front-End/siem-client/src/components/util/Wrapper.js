import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Alarms from "../admin/Alarms";
import AlarmsReport from "../admin/AlarmsReport";
import Dashboard from "../admin/Dashboard";
import Logs from "../admin/Logs";
import LogsReport from "../admin/LogsReport";
import Rules from "../admin/Rules";
import Login from "../login/Login";
import Navbar from "./Navbar";

function Wrapper() {
  const [url, setUrl] = useState("");
  useEffect(() => {
      setUrl(window.location.href.split('/')[3]);
  }, [window.location.href]);
  return (
    <>
      { url && url!== 'login' && <Navbar page={url} /> }
      <Router>
        <Switch>
          <Route path="/admin" exact component={Dashboard} />
          <Route path="/login" component={Login} />
          <Route path="/logs" component={Logs} />
          <Route path="/alarms" component={Alarms} />
          <Route path="/logs-report" component={LogsReport} />
          <Route path="/alarms-report" component={AlarmsReport} />
          <Route path="/rules" component={Rules} />
          <Route path="/" component={Login} />
        </Switch>
      </Router>
    </>
  );
}

export default Wrapper;
