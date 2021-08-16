import React from 'react'
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Dashboard from '../admin/Dashboard'
import Login from '../login/Login'

function Wrapper() {
    return (
        <>
            <Router>
                <Switch>
                    <Route path="/admin" exact component={Dashboard}/>
                    <Route path="/login" component={Login}/>
                    <Route path="/" component={Login}/>
                </Switch>
            </Router>  
        </>
    )
}

export default Wrapper
