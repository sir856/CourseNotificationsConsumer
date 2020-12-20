import React, {useContext} from 'react';
import {AuthContext} from '../../context/AuthContext';
import { Redirect } from "react-router-dom";

export default class UserInfo extends React.Component {

    componentDidMount() {
        if (this.context.isLoggedIn){
            this.context.getInfo();
        }
    }

    logout(e) {
        e.preventDefault();
        this.context.logout();
    }

    render() {
        if (!this.context.isLoggedIn) {
            return <Redirect to="/login"/>
        } else {
            const user = JSON.stringify(this.context.info);
            return (
                <div>
                    <div>User Page</div>
                    <div>{user}</div>
                    <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                </div>
            )
        }
    }
}

UserInfo.contextType = AuthContext;