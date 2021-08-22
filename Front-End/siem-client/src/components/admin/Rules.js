import React from 'react'
import '../style/Rules.css'
import UserDeviceRule from './UserDeviceRule'

function Rules() {
  return (
    <div className="rules-wrapper">
      <h1 className="rules-h1"> Define custom rules </h1>
      <div className="user-device-container">
        <UserDeviceRule />
      </div>
    </div>
  )
}

export default Rules
