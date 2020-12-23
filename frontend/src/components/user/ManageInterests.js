import React from 'react';
import {AuthContext} from '../../context/AuthContext';
import styled from 'styled-components';
import ErrorClass from "./ErrorClass";
import {Redirect } from "react-router-dom";
import OkClass from "./OkClass";

export default class ManageInterests extends React.Component {
    componentWillMount() {
        this.setState({
            selected: [],
            saved: null
        });
        this.getData();
        this.getInfo();
    };

    getInfo() {
        fetch("http://167.99.254.87:8080/user/info/" + this.context.id, {
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
                        let selected = [];
                        value.interests.forEach((knowledge, index, array) => {
                            knowledge.tags.forEach((tag, index, array) => {
                                selected.push({knowledgeId: knowledge.id, tagId: tag.id});
                            })
                        });

                        this.setState({
                            selected: selected
                        });
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

    getData() {
        fetch("http://167.99.254.87:8080/interest/knowledge", {
            method: 'GET', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'omit', // include, *same-origin, omit
            referrerPolicy: 'no-referrer', // no-referrer, *client
        })
            .then((response) => {
                if (response.status === 200) {
                    const readMessage = value => {
                        this.setState({
                            interests: value
                        })
                    };
                    response.json().then(readMessage);
                }
                else {
                    response.json().then(value => {
                        this.setState({
                            error: value
                        })
                    });
                }
            })
            .catch((error) => {
                console.log(error.toString());
                this.setState({
                    error: error.toString()
                })
            });
    }

    getKnowledge() {
        let result = [];
        result.push(<option value="">Выберете область знаний</option>) ;
        this.state.interests.forEach((knowledge, index, array) => {
            result.push(<option value={knowledge.id}> {knowledge.name} </option>);
        }
    );

        return result;
    }

    getTags(knowledgeId) {
        const knowledge = this.state.interests.find(knowledge => {
            console.log(knowledge.id);
            return knowledge.id == knowledgeId
        });

        // let result = [];
        let result = '<option value=\'\'>Выберете тэг</option>)';
        knowledge.tags.forEach((tag, index, array) => {
            result += '<option value=' + tag.id + '>' +  tag.name + '</option>';
        });
        return result;

    }

    setKnowledge(e) {
        this.setState({
            selectedKnowledge: e.target.value,
            selectedTag: "",
            saved: null
        });
        const tagSelect = e.target.ownerDocument.getElementById("tag");
        if (!e.target.value) {
            tagSelect.parentElement.hidden = true;
            tagSelect.innerHTML = "";
        } else {
            tagSelect.parentElement.hidden = false;
            tagSelect.innerHTML = this.getTags(e.target.value);
        }
        tagSelect.value = "";


    }

    setTag(e) {
        this.setState({
            selectedTag: e.target.value,
            saved: null
        })
    }

    addInterest(e) {
        const knowledge = this.state.selectedKnowledge;
        const tag = this.state.selectedTag;
        const selected = this.state.selected.find((interest) => {
            return interest.knowledgeId == knowledge && interest.tagId == tag
        });
        console.log(this.state.selected);
        console.log(knowledge);
        console.log(tag);
        if (!selected && knowledge && tag) {
            this.setState({
                selected: [
                    ...this.state.selected,
                    {
                        knowledgeId: knowledge,
                        tagId: tag
                    }
                ],
                saved: null
            });
        }
    }

    getInterests() {
        console.log(this.state);
        let result =[];

        this.state.selected.forEach((interest, index, array) => {
            const knowledge = this.state.interests.find(knowledge => {
                return knowledge.id == interest.knowledgeId;
            });

            const tag = knowledge.tags.find((tag) => {
                return tag.id == interest.tagId;
            });

            const id = knowledge.id + '_' + tag.id;

            result.push(
                <div id={id}>
                    <span>{knowledge.name + ': ' + tag.name}</span>
                    <button
                        onClick={this.delete.bind(this, id)}
                    >X</button>
                </div>
            )
        });
        console.log(result);

        return result;

    }

    delete(id, e) {
        let knowledge = id.split('_')[0];
        let tag = id.split('_')[1];

        let selected = this.state.selected.filter(interest => {
            return interest.knowledgeId != knowledge || interest.tagId != tag;
        });

        this.setState({
            selected: selected,
            saved: null
        })

    }

    save() {
        this.setState({
            loading: "Loading..."
        });
        fetch("http://167.99.254.87:8080/user/interests/add/" + this.context.id, {
            method: 'PUT', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, *cors, same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
            credentials: 'include', // include, *same-origin, omit
            headers: {
                'token': this.context.token,
                'Content-Type': 'application/json'
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            referrerPolicy: 'no-referrer',
            body: JSON.stringify(this.state.selected)// no-referrer, *client
        })
            .then((response) => {
                if (response.status === 200) {
                    this.setState({
                            saved: "Saved",
                            loading: null
                        }
                    );

                }
                else {
                    response.json().then(value => {
                        this.setState({
                                saved: null,
                                error: value.error,
                                loading: null
                            }
                        )
                    });

                }
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                        saved: null,
                        error: error.toString(),
                        loading: null
                    }
                )
            });
    }

    render() {
        if (!this.context.isLoggedIn) {
            return <Redirect to="/login"/>
        }
        if (!this.props.location.state) {
            return <Redirect to="/user"/>
        }
        if (this.state.interests) {
            const knowledgeOptions = this.getKnowledge();
            const interests = this.getInterests();
            const saved = this.state.saved;
            const error = this.state.error;
            const loading = this.state.loading;

            return (
                <div>
                    <div className="input-field">
                        <select id="knowledge" onChange={this.setKnowledge.bind(this)}>
                            {knowledgeOptions}
                        </select>
                        <label htmlFor="knowledge">Область знаний</label>
                    </div>
                    <div className="input-field" hidden={true}>
                        <select id="tag" onChange={this.setTag.bind(this)}>
                        </select>
                        <label htmlFor="tag">Тэги</label>
                    </div>
                    <button onClick={this.addInterest.bind(this)}>add</button>
                    <button onClick={this.save.bind(this)}>save</button>
                    <div>{loading}</div>
                    <div id="selected">{interests}</div>
                    <OkClass message={saved}/>
                    <ErrorClass message={error}/>
                </div>
            )
        }

        return null;

    }
}

ManageInterests.contextType = AuthContext;