import React from 'react'
import '../style/login.css'

function Login() {
    return (
        <div className="login-wrapper">
            <div id="username-wrapper">
                <label htmlFor="username">Username</label>
                <input id="username" type="text" />
            </div>
            <div id="password-wrapper">
                <label htmlFor="password"> Password </label>
                <input id="password" type="password" />
            </div>
            <div id="login-button-wrapper">
                <button id="login"> Login </button>
            </div>
        </div>
    )
}

export default Login
