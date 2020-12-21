import {AuthContext} from '../../context/AuthContext';
import React from 'react';
import { Error } from "./AuthForm";

export default class ErrorClass extends React.Component {
    render() {
        if (this.context.isError){
            const message = this.context.isError;
            return (
                <Error>{message}</Error>
            )
        }
        return (
            null
        )
    }
}

ErrorClass.contextType = AuthContext;