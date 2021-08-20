import axios from "axios";
import React, { useState, useEffect } from "react";
import { API } from "../../common/API";
import { useHistory } from "react-router-dom";
import "../style/Dashboard.css";

import UsersTable from "../tables/UsersTable";

function Dashboard() {
  const [usersData, setUsersData] = useState([]);
  const history = useHistory();

  useEffect(() => {
    axios({
      method: "GET",
      url: API + "users",
    })
      .then((response) => {
        let users = response.data;
        users = prettifyDateTime(users);
        setUsersData(users);
      })
      .catch((err) => {
        console.log("Error fetching users");
        console.log(err);
      });
  }, []);

  const updateUser = (user) => {
    axios({
      method: "PUT",
      url: API + "users",
      data: user,
    })
      .then((response) => {
        console.log("User updated successfully");
        history.push("/admin");
      })
      .catch((err) => {
        console.log("User cannot be updated");
        console.log(err);
      });
  };

  const prettifyDateTime = (users) => {
    for (let i = 0; i < users.length; i++) {
      let timestamp = Date.parse(users[i]["lastTimeUserWasActive"]);
      let date = new Date(timestamp);
      users[i]["lastTimeUserWasActive"] =
        date.getDate() +
        1 +
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

  return (
    <div className="dashboard-wrapper">
      <h1 id="dashboard-title"> User management </h1>
      <div className="users-table-wrapper">
        <UsersTable usersData={usersData} updateUser={updateUser} />
      </div>
    </div>
  );
}

export default Dashboard;
