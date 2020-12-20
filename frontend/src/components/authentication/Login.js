import {AuthContext} from '../../context/AuthContext';
import React from 'react';
import {Redirect } from "react-router-dom";
import { Card, Form, Input, Button, Error } from "./AuthForm";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import ErrorClass from "./ErrorClass";

export default class Login extends React.Component {

    componentDidMount() {
        this.setState({
                userData: {
                    name: "",
                    password: ""
                }
            }
        )
    }

    login(e) {
        e.preventDefault();
        this.context.login(this.state.userData);
    }

    setUserName(name) {
        this.setState({
        userData: {
            name: name,
            password: this.state.userData.pass
        }
        })
    }

    setPassword(pass) {
        this.setState({
        userData: {
            name: this.state.userData.name,
            password: pass
        }
        })
    }

    render() {
        if (this.context.isLoggedIn) {
            return <Redirect to="/user"/>
        }
        else {
            return (
            <Card>
                <Form>
                    <Input
                        type="username"
                        // value={userName}
                        onChange={e => {
                            this.setUserName(e.target.value);
                        }}
                        placeholder="email"
                    />
                    <Input
                        type="password"
                        // value={password}
                        onChange={e => {
                            this.setPassword(e.target.value);
                        }}
                        placeholder="password"
                    />
                    <Button onClick={this.login.bind(this)}>Sign In</Button>
                </Form>
                <Link to="/signup">Don't have an account?</Link>
                <ErrorClass/>
            </Card>);
            // return <button className="login" onClick={this.login.bind(this)}>Login</button>
        }

    }
}

Login.contextType = AuthContext;