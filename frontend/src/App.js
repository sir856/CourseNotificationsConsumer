import './App.css';
import Home from './components/authentication/Home'
import Login from './components/authentication/Login'
import UserInfo from './components/user/UserInfo'
import SignUp from './components/authentication/SignUp'
import {AuthContext} from './context/AuthContext';
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import {useState} from "react";
import {useCookies} from 'react-cookie';
import ManageInterests from "./components/user/ManageInterests";

function App() {
    const [token, setToken] = useState(localStorage.getItem("token"));
    const [isError, setError] = useState(null);
    const [id, setId] = useState(localStorage.getItem("id"));
    const [userInfo, setInfo] = useState(null);
    const [isLoggedIn, setLoggedIn] = useState(token != null & id != null);
    const [cookies, setCookies, removeCookies] = useCookies();

    const random = require('crypto-random-string');

    if (!cookies.sessionId) {
        setCookies("sessionId", random({length: 20}));
    }

    const login = (userData) => {
        fetch("http://167.99.254.87:8080/login", {
            method: 'PUT', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'Content-Type': 'application/json'
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer', // no-referrer, *client
            body: JSON.stringify(userData) // body data type must match "Content-Type" header
        })
            .then((response) => {
                if (response.status === 200) {
                    setError(null);
                    const readMessage = value => {
                        console.log(value);
                        localStorage.setItem("token", value.token);
                        localStorage.setItem("id", value.userId);
                        setToken(value.token);
                        setId(value.userId);
                        setLoggedIn(true);
                    };
                    response.json().then(readMessage);
                }
                else {
                    response.json().then(value => {
                        setLoggedIn(false);
                        setToken(null);
                        setId(null);
                        localStorage.removeItem("token");
                        localStorage.removeItem("id");
                        setError(value.message);
                    });
                }
            })
            .catch((error) => {
                console.log(error.toString());
                setError(error.toString());
                setLoggedIn(false);
                setToken(null);
                localStorage.removeItem("token");
                localStorage.removeItem("id");
            });
    };
    const register = (userData) => {
        fetch("http://167.99.254.87:8080/login", {
            method: 'POST', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'Content-Type': 'application/json'
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer', // no-referrer, *client
            body: JSON.stringify(userData) // body data type must match "Content-Type" header
        })
            .then((response) => {
                if (response.status === 200) {
                    setError(null)
                }
                else {
                    response.json().then(value => {
                        setError(value.message);
                    })
                }
            })
            .catch((error) => {
                setError(error.toString());
            });
    };
    const logout = () => {
        setLoggedIn(false);
        setToken(null);
        setInfo(null);
        setId(null);
        localStorage.removeItem(
            "token"
        );
        localStorage.removeItem("id");
    };

    const getUserInfo = () => {
        return fetch("http://167.99.254.87:8080/user/info/" + id, {
            method: 'GET', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'token': token
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer', // no-referrer, *client
        })
            .then((response) => {
                if (response.status === 200) {
                    setError(null);
                    response.json().then(value => {
                        setInfo(value);
                    })
                }
                else {
                    response.json().then(value => {
                        setError(value.message);
                        setInfo(null);
                    })
                }
            })
            .catch((error) => {
                setError(error.toString());
                setInfo(null);
            });
    };

    return (
      <AuthContext.Provider value={{
          isLoggedIn: isLoggedIn,
          token: token,
          login: login,
          logout: logout,
          register: register,
          isError: isError,
          getInfo: getUserInfo,
          info: userInfo,
          id: id
      }}>
          <Router>
              <div>
                  <ul>
                      <li>
                          <Link to="/">Home Page</Link>
                      </li>
                      <li>
                          <Link to="/login">Login Page</Link>
                      </li>
                  </ul>
                  <Route exact path="/" component={Home} />
                  <Route path="/login" component={Login} />
                  <Route path="/user" component={UserInfo} />
                  <Route path="/signup" component={SignUp} />
                  <Route path="/user/interests/manage" component={ManageInterests} />
              </div>
          </Router>
      </AuthContext.Provider>
  );
}

export default App;
