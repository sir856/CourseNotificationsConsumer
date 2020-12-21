import React from 'react';
import {AuthContext} from '../../context/AuthContext';
import {Link, Redirect} from "react-router-dom";
import ErrorClass from "../authentication/ErrorClass";
import SockJsClient from 'react-stomp';

export default class UserInfo extends React.Component {

    componentWillMount() {
        if (this.context.isLoggedIn){
            this.context.getInfo();
            this.setState({
                notifications: []
            })
        }
    }

    logout(e) {
        e.preventDefault();
        this.context.logout();
    }

    handleMessage(message) {
        console.log(message);
        console.log(this.context);
        if (message.userId == this.context.id) {
            this.setState({
                notifications: [
                    ...this.state.notifications,
                    message.message
                ]
            })
        }
    }

    getNotifications() {
        let result = [];

        this.state.notifications.forEach((notification) => {
            result.push(<div><span>{notification}</span> <button
                onClick={this.delete.bind(this, notification)}
            >X</button> </div>)
        });

        return result;
    }

    delete(notificationToDelete) {

        let notifications = this.state.notifications.filter(notification => {
            return notificationToDelete !== notification;
        });

        this.setState({
            notifications: notifications
        })

    }

    render() {
        console.log(this.state);
        if (!this.context.isLoggedIn) {
            return <Redirect to="/login"/>
        } else {
            if (!this.context.isError && this.context.info) {
                const user = this.context.info;
                const notifications = this.getNotifications();
                return (
                    <div>
                        <div>User Page</div>
                        <div><b>{user.name}</b></div>
                        <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                        {notifications}
                        <div><Link to="/user/interests/manage">Управлять подписками</Link></div>
                        <SockJsClient url = 'http://localhost:8080/notification' topics={['/course/notification']}
                                      onMessage={this.handleMessage.bind(this)}
                                      ref={ (client) => { this.clientRef = client }}/>
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