import React, { useState } from 'react'
import axios from 'axios'
import { useHistory } from "react-router-dom"
import '../style/login.css'
import { API } from '../../common/API';

function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    const submitData = () => {
        axios({
            method: "post",
            url: API + 'users/login',
            data: {username, password},
        }).then(response => {
            localStorage.setItem("role", "ADMIN");   
            history.push("/admin");
        }).catch(err => {
            console.log('An error occurred');
            console.log(err);
        });
    }

    return (
        <div className="login-wrapper">
            <div id="username-wrapper">
                <label htmlFor="username">Username</label>
                <input id="username"
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)} />
            </div>
            <div id="password-wrapper">
                <label htmlFor="password"> Password </label>
                <input id="password"
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)} />
            </div>
            <div id="login-button-wrapper">
                <button id="login" onClick={submitData}> Login </button>
            </div>
        </div>
    )
}

export default Login
