import {AuthContext} from '../../context/AuthContext';
import {Redirect } from "react-router-dom";
import ErrorClass from './ErrorClass'
import { Card, Form, Input, Button, Error } from "./AuthForm";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import React from 'react';

export default class SignUp extends React.Component {
    componentDidMount() {
        this.setState({
                userData: {
                    name: "",
                    password: "",
                },
                isError: ""
            }
        )
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

    setPasswordAgain(pass) {
        this.setState({
            passAgain: pass
        })
    }

    register(e) {
        e.preventDefault();
        if (this.state.userData.password === this.state.passAgain) {
            this.context.register(this.state.userData)
        }
        else {
            this.context.isError = "Passwords don't match"
        }
    }



    render() {
        console.log(this.context);
        console.log(this.state);
        if (this.context.isLoggedIn) {
            return <Redirect to="/user"/>
        }
        else {
            return (
                <Card>
                    <Form>
                        <Input type="username" placeholder="email" onChange={e => {
                            this.setUserName(e.target.value);
                        }} />
                        <Input type="password" placeholder="password" onChange={e => {
                            this.setPassword(e.target.value);
                        }} />
                        <Input type="password" placeholder="password again" onChange={e => {
                            this.setPasswordAgain(e.target.value);
                        }}/>
                        <Button onClick={this.register.bind(this)}>Sign Up</Button>
                    </Form>
                    <Link to="/login">Already have an account?</Link>
                    <ErrorClass/>
                </Card>
            );

            // if (this.context.isError )
        }

    }
}

SignUp.contextType = AuthContext;