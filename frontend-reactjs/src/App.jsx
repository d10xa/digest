import React from 'react';
import {connect} from 'react-redux';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            count: 0
        }
    }

    render() {
        return (
            <div>
                <button
                    onClick={this.props.btnClick}
                >
                    increment
                </button>
                <div>
                    {this.props.counter}
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        counter: state
    }
};

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        btnClick: () => {
            dispatch({type: "INCREMENT_COUNTER"})
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(App)
