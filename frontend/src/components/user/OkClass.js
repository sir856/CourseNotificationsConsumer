import React from "react";
import { Ok} from "./UserForm";

export default class OkClass extends React.Component {
    render() {
        if (this.props.message){
            let message = this.props.message;
            return (
                <Ok>{message}</Ok>
            )
        }
        return (
            null
        )
    }
}