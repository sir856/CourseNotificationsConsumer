import React, {useContext} from 'react';
import {AuthContext} from '../../context/AuthContext';

export default class Home extends React.Component {

    render() {
        return <div>Home page</div>
    }

}

Home.contextType = AuthContext;