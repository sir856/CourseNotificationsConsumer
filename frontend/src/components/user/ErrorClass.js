import React from "react";
import { Error} from "./UserForm";

export default class ErrorClass extends React.Component {
    render() {
        if (this.props.message){
            let message = this.props.message;
            return (
                <Error>{message}</Error>
            )
        }
        return (
            null
        )
    }
}