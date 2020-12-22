import React from 'react';
import {AuthContext} from '../../context/AuthContext';
import {Link, Redirect} from "react-router-dom";
import ErrorClass from "./ErrorClass";
import SockJsClient from 'react-stomp';

export default class UserInfo extends React.Component {

    componentWillMount() {
        if (this.context.isLoggedIn){
            this.getInfo();
            this.setState({
                info: null,
            })

        }
    }

    getInfo() {
        fetch("http://localhost:8080/user/info/" + this.context.id, {
            method: 'GET', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'token': this.context.token
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer', // no-referrer, *client
        })
            .then((response) => {
                if (response.status === 200) {
                    response.json().then(value => {
                        this.setState({
                            info: value
                        })
                    })
                }
                else {
                    response.json().then(value => {
                        this.setState({
                                error: value.message
                            }
                        )
                    })
                }
            })
            .catch((error) => {
                this.setState({
                        error: error.toString()
                    }
                )
            });
    }

    logout(e) {
        e.preventDefault();
        this.context.logout();
    }

    handleMessage(message) {
        if (message.userId == this.context.id) {
            this.setState({
                info:{
                    ...this.state.info,
                    notifications: [
                            ...this.state.info.notifications,
                        {
                            id: message.userId + '_' + message.message.id,
                            message: message
                        }
                        ]
                }
            });
        }
    }

    getNotifications() {
        console.log(this.state);
        let result = [];

        this.state.info.notifications.forEach((notification) => {
            result.push(<div><span>{notification.message.message.name}</span> <button
                onClick={this.delete.bind(this, notification.id)}
            >X</button> </div>);
            let knowledge_li = [];
            notification.message.message.tags.forEach((knowledge) => {
                let tag_li = [];
                knowledge.tags.forEach((tag) => {
                    tag_li.push(<li>{tag}</li>);
                });
                knowledge_li.push(
                    <li>{knowledge.knowledge}
                        <ul>{tag_li}</ul>
                    </li>
                )
            });

            result.push(<ul>{knowledge_li}</ul>)
        });

        return result;
    }

    delete(id) {

        let notifications = this.state.info.notifications.filter(notification => {
            return id !== notification.id;
        });

        this.setState({
            info: {
                ...this.state.info,
                notifications: notifications
            }
        });

        fetch("http://localhost:8080/user/notification/" + this.context.id + "?id=" + id, {
            method: 'DELETE', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'token': this.context.token
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer', // no-referrer, *client
        })
            .then((response) => {
                if (response.status !== 200) {
                    response.json().then(value => {
                        this.setState({
                                error: value.message
                            }
                        )
                    })
                }
            })
            .catch((error) => {
                this.setState({
                        error: error.toString()
                    }
                )
            });
    }

    render() {
        console.log(this.state);
        if (!this.context.isLoggedIn) {
            return <Redirect to="/login"/>
        } else {
            if (this.state.info) {
                let user = this.state.info;
                const notifications = this.getNotifications();
                return (
                    <div>
                        <div>User Page</div>
                        <div><b>{user.name}</b></div>
                        <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                        {notifications}
                        <SockJsClient url = 'http://localhost:8080/notification' topics={['/course/notification']}
                                      onMessage={this.handleMessage.bind(this)}
                                      ref={ (client) => { this.clientRef = client }}/>
                        <ErrorClass message={this.state.error}/>
                        <div><Link to={{
                            pathname: "/user/interests/manage",
                            state: {
                                interests: this.state.info.interests
                            }
                        }}>Управлять подписками</Link></div>
                    </div>
                )
            }
            if (this.state.error) {
                return (
                    <div>
                        <ErrorClass message={this.state.error}/>
                        <button className="logout" onClick={this.logout.bind(this)}>Logout</button>
                    </div>
                )
            }
            return <div>Loading...</div>;
        }
    }
}

UserInfo.contextType = AuthContext;