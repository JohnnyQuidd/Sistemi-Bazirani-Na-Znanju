import React from 'react'
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Login from '../login/Login'

function Wrapper() {
    return (
        <>
            <Router>
                <Switch>
                    <Route component={Login}/>
                </Switch>
            </Router>  
        </>
    )
}

export default Wrapper
