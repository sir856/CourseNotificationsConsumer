import React from 'react';
import {AuthContext} from '../../context/AuthContext';
import {Link, Redirect} from "react-router-dom";
import ErrorClass from "../authentication/ErrorClass";

export default class UserInfo extends React.Component {

    componentWillMount() {
        if (this.context.isLoggedIn){
            this.context.getInfo();
        }
    }

    logout(e) {
        e.preventDefault();
        this.context.logout();
    }

    render() {
        console.log(this.context);
        if (!this.context.isLoggedIn) {
            return <Redirect to="/login"/>
        } else {
            if (!this.context.isError && this.context.info) {
                const user = this.context.info;
                return (
                    <div>
                        <div>User Page</div>
                        <div><b>{user.name}</b></div>
                        <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                        <div><Link to="/user/interests/manage">Управлять подписками</Link></div>
                    </div>
                )
            }
            else {
                return<div>
                    <ErrorClass/>
                    <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                </div>
            }
        }
    }
}

UserInfo.contextType = AuthContext;